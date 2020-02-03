package com.sgs.businessmodule.taskUtil.cutMsg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class MuTerminalMsg {

    private Integer playTimes;

    //优先级 0：慢  1：正常 2：快
    private Integer speed;
    //背景色
    private String backGroundColor;
    //字体颜色
    private String fontColor;
    //字体
    private String fontName;
    //字号
    private String fontSize;
    //透明度
    private Integer opacity;
    //位置：0顶部 1：底部
    private Integer position;
    //追加播放 0：否 1：是
    private Integer append;
    //终端设备ID，逗号,分隔
    private String terminalIds;
    //播报终端数量
    private Integer terminalNum;
    //消息内容
    private String msgContent;

    //方向
    private Integer direction;

    private Integer id;

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


    public static void main(String[] args) {
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
                "\t\"status\": 0,\n" +
                "\t\"terminalIds\": \"16,18\"\n" +
                "}";

        MuTerminalMsg orderProgarmPalyInstructionVo = JSON.parseObject(orgin, new TypeReference<MuTerminalMsg>() {
        });

        System.out.println("orderProgarmPalyInstructionVo.toString()" + orderProgarmPalyInstructionVo.toString());

    }
}
