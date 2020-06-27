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

/*    {
        "deptId":null, "status":0, "createUser":null, "createTime":
        "2020-06-27 21:02:38", "updateUser":null, "updateTime":"2020-06-27 21:02:38", "filterSql":
        null, "id":27, "terminalIdentity":"094869178368", "secretKey":
        "917DB594088F1366C0376AFD50BCBAD2", "token":
        "a4673a0147efffc7f892d2026e2a68f712b1c1c0d8ba7d385e399ca484fcc520", "timeStamp":
        1593262958366, "batchName":null, "batchNo":null
    }*/

    String terminalIdentity;
    String secretKey;
    String token;
    String timeStamp;
    String status;
    String createTime;
    String updateTime;
    String batchName;
    String deptId;
    String id;
    String updateUser;
    String batchNo;
    String filterSql;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getFilterSql() {
        return filterSql;
    }

    public void setFilterSql(String filterSql) {
        this.filterSql = filterSql;
    }


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
