package com.sgs.businessmodule.upReportModel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tb_scenceReport")
public class ScenceReport {
    /**
     * 指令Scence
     */
    @DatabaseField(generatedId = true)
    private int id;
    //播放日期
    @DatabaseField
    private String palyDate;
    //节目名称
    @DatabaseField
    private String programName;
    //场景名称
    @DatabaseField
    private String sceneName;
    //场景ID
    @DatabaseField
    private Integer sceneId;
    //播放次数
    @DatabaseField
    private Integer palyNum;
    //播放时长秒
    @DatabaseField
    private Integer palySecond;
    @DatabaseField
    //设备身份编码
    private String terminalIdentity;
    //设备名称
    @DatabaseField
    private String terminalName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPalyDate() {
        return palyDate;
    }

    public void setPalyDate(String palyDate) {
        this.palyDate = palyDate;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getPalyNum() {
        return palyNum;
    }

    public void setPalyNum(Integer palyNum) {
        this.palyNum = palyNum;
    }

    public Integer getPalySecond() {
        return palySecond;
    }

    public void setPalySecond(Integer palySecond) {
        this.palySecond = palySecond;
    }

    public String getTerminalIdentity() {
        return terminalIdentity;
    }

    public void setTerminalIdentity(String terminalIdentity) {
        this.terminalIdentity = terminalIdentity;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    @Override
    public String toString() {
        return "ScenceReport{" +
                "id=" + id +
                ", palyDate='" + palyDate + '\'' +
                ", programName='" + programName + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", sceneId=" + sceneId +
                ", palyNum=" + palyNum +
                ", palySecond=" + palySecond +
                ", terminalIdentity='" + terminalIdentity + '\'' +
                ", terminalName='" + terminalName + '\'' +
                '}';
    }
}
