package com.sgs.programModel.entity;


public class ProListVo {
/*[{'terminalIdentity':'faf','programId':12,'timeQuantum':'2019-1-3','type':0},
 {'terminalIdentity':'643543','programId':454,'timeQuantum':'2019-1-3~2019-2-3'},'type':1]*/

    public String terminalIdentity;
    public int programId;
    public String timeQuantum;
    public int type;
    //发布时间
    public String publicationTime;
    //过期时间
    public String limitTime;

    public String getTerminalIdentity() {
        return terminalIdentity;
    }

    public void setTerminalIdentity(String terminalIdentity) {
        this.terminalIdentity = terminalIdentity;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getTimeQuantum() {
        return timeQuantum;
    }

    public void setTimeQuantum(String timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

}
