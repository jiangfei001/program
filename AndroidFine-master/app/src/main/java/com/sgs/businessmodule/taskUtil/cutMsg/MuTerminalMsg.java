package com.sgs.businessmodule.taskUtil.cutMsg;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tb_muTerminalMsg")
public class MuTerminalMsg {
    @DatabaseField(id = true, canBeNull = false, columnName = "id")
    private int id;
    @DatabaseField
    private Integer playTimes;

    @DatabaseField
    private Long hasplay = 0l;

    //优先级 0：慢  1：正常 2：快
    @DatabaseField
    private Integer speed;
    //背景色
    @DatabaseField
    private String backGroundColor;
    //字体颜色
    @DatabaseField
    private String fontColor;
    //字体
    @DatabaseField
    private String fontName;
    //字号
    @DatabaseField
    private String fontSize;
    //透明度
    @DatabaseField
    private Integer opacity;
    //位置：0顶部 1：底部
    @DatabaseField
    private Integer position;
    //追加播放 0：否 1：是
    @DatabaseField
    private Integer append;
    //终端设备ID，逗号,分隔
    @DatabaseField
    private String terminalIds;
    //播报终端数量
    @DatabaseField
    private Integer terminalNum;
    //消息内容
    @DatabaseField
    private String msgContent;

    //方向 1 从左到右
    @DatabaseField
    private Integer direction;

    @DatabaseField
    private String msgStatus;

    /**
     * 完成时间
     */
    //消息内容
    @DatabaseField
    private String finishTime;
    /**
     * 完成时间
     */
    //消息内容
    @DatabaseField
    private String beginTime;

    //消息内容
    @DatabaseField
    private String endDate;

    //finishTime
    //beginTime


    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Long getHasplay() {
        return hasplay;
    }

    public void setHasplay(Long hasplay) {
        this.hasplay = hasplay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(Integer playTimes) {
        this.playTimes = playTimes;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getOpacity() {
        return opacity;
    }

    public void setOpacity(Integer opacity) {
        this.opacity = opacity;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getAppend() {
        return append;
    }

    public void setAppend(Integer append) {
        this.append = append;
    }

    public String getTerminalIds() {
        return terminalIds;
    }

    public void setTerminalIds(String terminalIds) {
        this.terminalIds = terminalIds;
    }

    public Integer getTerminalNum() {
        return terminalNum;
    }

    public void setTerminalNum(Integer terminalNum) {
        this.terminalNum = terminalNum;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "MuTerminalMsg{" +
                "id=" + id +
                ", playTimes=" + playTimes +
                ", hasplay=" + hasplay +
                ", speed=" + speed +
                ", backGroundColor='" + backGroundColor + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", fontName='" + fontName + '\'' +
                ", fontSize='" + fontSize + '\'' +
                ", opacity=" + opacity +
                ", position=" + position +
                ", append=" + append +
                ", terminalIds='" + terminalIds + '\'' +
                ", terminalNum=" + terminalNum +
                ", msgContent='" + msgContent + '\'' +
                ", direction=" + direction +
                ", msgStatus='" + msgStatus + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
/*public static void main(String[] args) {
        String orgin = "{\n" +
                "\t\"append\": 0,\n" +
                "\t\"createTime\": 1580473823521,\n" +
                "\t\"createUser\": \"admin\",\n" +
                "\t\"deptId\": 2,\n" +
                "\t\"fontColor\": \"#92e3ed\",\n" +
                "\t\"fontSize\": \"20\",\n" +
                "\t\"id\": 2,\n" +
                "\t\"msgContent\": \"gdd 大范甘迪高蛋白\",\n" +
                "\t\"playTimes\": 60,\n" +
                "\t\"position\": 1,\n" +
                "\t\"speed\": 2,\n" +
                "\t\"msgStatus\": 0,\n" +
                "\t\"terminalIds\": \"16,18\"\n" +
                "}";

        MuTerminalMsg orderProgarmPalyInstructionVo = JSON.parseObject(orgin, new TypeReference<MuTerminalMsg>() {
        });

        System.out.println("orderProgarmPalyInstructionVo.toString()" + orderProgarmPalyInstructionVo.toString());

    }*/
}
