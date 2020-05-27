package com.sgs.businessmodule.upReportModel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_repHotReport")
public class RepHotReport {
    /**
     * 指令RepHotReport
     */
    @DatabaseField(generatedId = true)
    private int id;
    //设备身份编码
    @DatabaseField
    private String terminalIdentity;
    //设备名称
    @DatabaseField
    private String terminalName;
    //节目名称
    @DatabaseField
    private String programName;
    //场景名称
    @DatabaseField
    private String sceneName;
    //场景ID
    @DatabaseField
    private Integer sceneId;
    //点击次数
    @DatabaseField
    private Integer clickNum;
    //区域名称
    @DatabaseField
    private String areaName;
    //页面名称
    @DatabaseField
    private String pageName;
    // 开始时间 格式年月日时：2020022412
    @DatabaseField
    private String startTime;
    @DatabaseField
    private String createTime;
    //结束时间 格式年月日时：2020022412
    @DatabaseField
    private String endTime;
    //播放日期
    @DatabaseField
    private String palyDate;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPalyDate() {
        return palyDate;
    }

    public void setPalyDate(String palyDate) {
        this.palyDate = palyDate;
    }

    @Override
    public String toString() {
        return "RepHotReport{" +
                "id=" + id +
                ", terminalIdentity='" + terminalIdentity + '\'' +
                ", terminalName='" + terminalName + '\'' +
                ", programName='" + programName + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", sceneId=" + sceneId +
                ", clickNum=" + clickNum +
                ", areaName='" + areaName + '\'' +
                ", pageName='" + pageName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
