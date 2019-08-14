package org.elastos.hive.demo;

public class FileItem {
    private String fileName ;
    private String fileAbsPath ;
    private String fileSize ;
    private boolean isFolder ;

    public FileItem(String fileName) {
        this.fileName = fileName;
    }

    public String getFileAbsPath() {
        return fileAbsPath;
    }

    public void setFileAbsPath(String fileAbsPath) {
        this.fileAbsPath = fileAbsPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }
}
