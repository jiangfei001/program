package com.sgs.programModel.entity;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * 节目播放日程
 */
@DatabaseTable(tableName = "tb_progarmPalyInstructionVo")
public class ProgarmPalyInstructionVo {
    @DatabaseField(id = true, canBeNull = false, columnName = "id")
    private int id;
    @DatabaseField
    private String programName;
    @DatabaseField
    private int playTime;
    @DatabaseField
    private int sceneNum;
    @DatabaseField
    private String resolution;
    @DatabaseField
    private String sceneList;
    @DatabaseField
    private String programZipName;
    /**
     * 节目压缩包
     */
    @DatabaseField
    private String programZip;

    /**
     *
     */
    @DatabaseField
    private int programZipStatus;

    private int totalStatus;
    @DatabaseField
    private boolean isDownloadSuccess;

    /**
     * 节目资源列表 数据库中只保存json
     */
    @DatabaseField
    private String programResourceList;
    @DatabaseField
    private String publicationPlan;

    PublicationPlanVo publicationPlanObject;

    /**
     * 节目资源列表 数据库中只保存json
     */
    @DatabaseField
    private String programMusicList;

    List<ProgramResource> programResourceListArray;

    List<ProgramResource> programMusicListArray;

    public String getProgramMusicList() {
        return programMusicList;
    }

    public void setProgramMusicList(String programMusicList) {
        this.programMusicList = programMusicList;
    }

    public List<ProgramResource> getProgramMusicListArray() {
        return programMusicListArray;
    }

    public void setProgramMusicListArray(List<ProgramResource> programMusicListArray) {
        this.programMusicListArray = programMusicListArray;
    }

    public PublicationPlanVo getPublicationPlanObject() {
        return publicationPlanObject;
    }

    public void setPublicationPlanObject(PublicationPlanVo publicationPlanObject) {
        this.publicationPlanObject = publicationPlanObject;
    }

    public void setProgramResourceList(String programResourceList) {
        this.programResourceList = programResourceList;
        programResourceListArray = JSON.parseArray(programResourceList, ProgramResource.class);
    }


    public List<ProgramResource> getProgramResourceListArray() {
        return programResourceListArray;
    }

    public void setProgramResourceListArray(List<ProgramResource> programResourceListArray) {
        this.programResourceListArray = programResourceListArray;
    }

    public String getProgramResourceList() {
        return programResourceList;
    }

    public int getTotalStatus() {
        return totalStatus;
    }

    public void setTotalStatus(int totalStatus) {
        this.totalStatus = totalStatus;
    }


    public int getProgramZipStatus() {
        return programZipStatus;
    }

    public void setProgramZipStatus(int programZipStatus) {
        this.programZipStatus = programZipStatus;
    }

    public String getProgramZipName() {
        return programZipName;
    }

    public void setProgramZipName(String programZipName) {
        this.programZipName = programZipName;
    }

    public String getSceneList() {
        return sceneList;
    }

    public void setSceneList(String sceneList) {
        this.sceneList = sceneList;
    }

    public String getPublicationPlan() {
        return publicationPlan;
    }

    public void setPublicationPlan(String publicationPlan) {
        this.publicationPlan = publicationPlan;
    }

    /**
     * 场景列表（包括场景对于的xml名称，播放几秒） 数据库中只保存json
     *//*
    private List<ProgarmPalySceneVo> sceneList;
	*/

    /**
     * 发布计划 数据库中只保存json
     *//*
	private PublicationPlanVo publicationPlan;*/
    public String getProgramZip() {
        return programZip;
    }

    public void setProgramZip(String programZip) {
        this.programZip = programZip;
    }

    public boolean isDownloadSuccess() {
        return isDownloadSuccess;
    }

    public void setDownloadSuccess(boolean downloadSuccess) {
        isDownloadSuccess = downloadSuccess;
    }


    /*public PublicationPlanVo getPublicationPlan() {
        return publicationPlan;
    }

    public void setPublicationPlan(PublicationPlanVo publicationPlan) {
        this.publicationPlan = publicationPlan;
    }

    public List<ProgarmPalySceneVo> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<ProgarmPalySceneVo> sceneList) {
        this.sceneList = sceneList;
    }*/

/*    public List getProgramResourceList() {
        return programResourceList;
    }

    public void setProgramResourceList(List programResourceList) {
        this.programResourceList = programResourceList;
    }*/


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getSceneNum() {
        return sceneNum;
    }

    public void setSceneNum(int sceneNum) {
        this.sceneNum = sceneNum;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

}
