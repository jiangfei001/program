package com.sgs.programModel.entity;

public class ProgramResource {
    /*"fileName": "1.jpg",
            "id": 24,
            "status": 0,
            "url": "http://q0u8hijil.bkt.clouddn.com/admin/201911/12/63cc0096294246e0a40efec375b44199.jpg",
            "virtualPath": "admin/201911/12/63cc0096294246e0a40efec375b44199.jpg"*/

    private String fileName;
    private int id;
    private String url;
    private String virtualPath;
    private int status;
    private int downStatus;
    private String realfileName;

    public String getRealfileName() {
        return realfileName;
    }

    public void setRealfileName(String realfileName) {
        this.realfileName = realfileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDownStatus() {
        return downStatus;
    }

    public void setDownStatus(int downStatus) {
        this.downStatus = downStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVirtualPath() {
        return virtualPath;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }
}
