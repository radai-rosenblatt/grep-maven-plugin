package net.radai;

public class Grep {
    private String file;
    private String filePattern;
    private String grepPattern;
    private String outputPattern;
    private boolean failIfFound;
    private boolean failIfNotFound;

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

    public boolean isFailIfFound() {
        return failIfFound;
    }

    public void setFailIfFound(boolean failIfFound) {
        this.failIfFound = failIfFound;
    }

    public boolean isFailIfNotFound() {
        return failIfNotFound;
    }

    public void setFailIfNotFound(boolean failIfNotFound) {
        this.failIfNotFound = failIfNotFound;
    }

    @Override
    public String toString() {
        String failDesc = failIfFound ? ", fail if found" : "";
        if (failIfNotFound) {
            failDesc += ", fail if not found";
        }
        return ("grep for \""+ grepPattern +"\" in "+ (filePattern!=null ? filePattern : file)) + failDesc;
    }
}
