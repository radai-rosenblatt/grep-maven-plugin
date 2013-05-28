package net.radai;

public class Grep {
    private String file;
    private String filePattern;
    private String grepPattern;
    private String outputPattern;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public String getGrepPattern() {
        return grepPattern;
    }

    public void setGrepPattern(String grepPattern) {
        this.grepPattern = grepPattern;
    }

    public String getOutputPattern() {
        return outputPattern;
    }

    public void setOutputPattern(String outputPattern) {
        this.outputPattern = outputPattern;
    }

    @Override
    public String toString() {
        return "grep for \""+ grepPattern +"\" in "+ filePattern!=null ? filePattern : file;
    }
}
