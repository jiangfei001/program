package com.uiModel;

public class Jihuo {
    /*"deptId": null,
            "status": 0,
            "createUser": null,
            "createTime": null,
            "updateUser": null,
            "updateTime": null,
            "filterSql": null,
            "id": null,
            "terminalIdentity": "C82983937024",
            "secretKey": "3A37F8CF49F79F1BE5CFAE3424921AB4",
            "token": "9b3a9e323a85aa4c9f26972012ad3f2b7dc6678057379cd3151c42bb1e999380",
            "timeStamp": 1591527062006,
            "batchName": null,
            "batchNo": null*/
    String terminalIdentity;
    String secretKey;
    String token;
    String timeStamp;

    public String getTerminalIdentity() {
        return terminalIdentity;
    }

    public void setTerminalIdentity(String terminalIdentity) {
        this.terminalIdentity = terminalIdentity;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
