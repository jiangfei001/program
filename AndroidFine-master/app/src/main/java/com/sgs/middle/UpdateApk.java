package com.sgs.middle;

public class UpdateApk {
    String AppVersion;
    String filename;
    String url;

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UpdateApk{" +
                "AppVersion='" + AppVersion + '\'' +
                ", filename='" + filename + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
