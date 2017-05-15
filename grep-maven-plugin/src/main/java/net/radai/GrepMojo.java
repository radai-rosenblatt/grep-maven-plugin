package net.radai;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileReader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Mojo(name = "grep", threadSafe = true)
public class GrepMojo extends AbstractMojo {

    @Parameter(required = true)
    private List<Grep> greps;

    @Parameter(defaultValue = "${basedir}", required = true, readonly = true)
    private File basedir;

    @Parameter(required = false)
    private String outputPattern;

    private Log log = getLog();

    public List<Grep> getGreps() {
        return greps;
    }

    public void setGreps(List<Grep> greps) {
        this.greps = greps;
    }

    public File getBasedir() {
        return basedir;
    }

    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }

    public String getOutputPattern() {
        return outputPattern;
    }

    public void setOutputPattern(String outputPattern) {
        this.outputPattern = outputPattern;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            for (Grep grep : greps) {
                Pattern lookingFor = Pattern.compile(grep.getGrepPattern());
                List<TFile> files = getFiles(grep);
                for (TFile file : files) {
                    grepInFile(file, lookingFor, grep);
                }
            }
        } catch (MojoFailureException e) {
            //rethrow. this is a deliberate failure
            throw e;
        } catch (Exception e) {
            throw new MojoFailureException("error grepping", e);
        }
    }

    private List<TFile> getFiles(Grep grep) {
        Stream<String> file = streamifyNullable(grep.getFile(), Stream::of);
        Stream<String> filesMatchingPattern = streamifyNullable(grep.getFilePattern(), this::findFilesMatching);
        return Stream.concat(file, filesMatchingPattern).map(fileName -> new TFile(basedir, fileName)).collect(toList());
    }

    private Stream<String> streamifyNullable(String value, Function<String, Stream<String>> steamGenerator) {
        return Optional.ofNullable(value).map(steamGenerator).orElse(Stream.empty());
    }

    private Stream<String> findFilesMatching(String filePattern) {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{filePattern});
        scanner.setBasedir(basedir);
        scanner.setCaseSensitive(false);
        scanner.scan();
        return stream(scanner.getIncludedFiles());
    }

    private void grepInFile(TFile theFile, Pattern lookingFor, Grep grep) throws Exception {
        if (!theFile.exists()) {
            log.warn("specified file does not exist: " + theFile.getCanonicalPath());
            return;
        }
        if (!theFile.canRead()) {
            log.warn("cannot read from file " + theFile.getCanonicalPath());
            return;
        }
        log.info("grepping for " + lookingFor + " in " + theFile.getCanonicalPath());
        Matcher m;
        TFileReader reader = null;
        try {
            reader = new TFileReader(theFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            int lineNumber = 0;
            boolean found = false;
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                m = lookingFor.matcher(line);
                if (!(m.find())) {
                    continue;
                }
                processMatchingLine(theFile, line, grep, lineNumber);
                found = true;
            }
            if (!found) {
                failIfNotFound(theFile, grep);
            }
        } finally {
            if (reader != null) reader.close();
        }
    }

    private void processMatchingLine(TFile theFile, String theLine, Grep grep, int lineNumber) throws Exception {
        printMatch(theFile, theLine, grep, lineNumber);
        failIfFound(theFile, theLine, grep, lineNumber);
    }

    private void printMatch(TFile theFile, String theLine, Grep grep, int lineNumber) throws IOException, TemplateException {
        String templateToUse = grep.getOutputPattern();
        if (templateToUse == null) {
            templateToUse = outputPattern;
        }
        if (templateToUse == null) {
            log.info(theLine);
        } else {
            Template template = new Template("templateName", new StringReader(templateToUse), new Configuration());
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("line", theLine);
            parameters.put("fileName", theFile.getName());
            parameters.put("lineNumber", lineNumber + "");
            StringWriter output = new StringWriter();
            template.process(parameters, output);
            log.info(output.toString());
        }
    }

    private void failIfFound(TFile theFile, String theLine, Grep grep, int lineNumber) throws IOException, MojoFailureException {
        if (grep.isFailIfFound()) {
            String msg = grep.getGrepPattern() + " found in  " + theFile.getCanonicalPath() + ":" + lineNumber + " (" + theLine + ")";
            log.error(msg);
            throw new MojoFailureException(msg);
        }
    }

    private void failIfNotFound(TFile theFile, Grep grep) throws IOException, MojoFailureException {
        if (grep.isFailIfNotFound()) {
            String msg = grep.getGrepPattern() + " not found in  " + theFile.getCanonicalPath();
            log.error(msg);
            throw new MojoFailureException(msg);
        }
    }
}
