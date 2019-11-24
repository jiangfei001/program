package com.programModel.entity;

import java.util.List;

/**
 * 节目播放日程
 * @author Administrator
 *
 */
public class ProgarmPalyInstructionVo {

	private int id;
	
	private String programName;
	
	private int playTime;
	
    private int sceneNum;
    
    private String resolution;

	/**
	 * 场景列表（包括场景对于的xml名称，播放几秒） 数据库中只保存json
	 */
    private List<ProgarmPalySceneVo> sceneList;
	/**
	 * 发布计划 数据库中只保存json
	 */
	private PublicationPlanVo publicationPlan;

	/**
	 * 节目资源列表 数据库中只保存json
	 */
	private List programResourceList;
	/**
	 * 节目压缩包
	 */
	private Object programZip;

	private boolean isDownloadSuccess;
	
	public PublicationPlanVo getPublicationPlan() {
		return publicationPlan;
	}
	public void setPublicationPlan(PublicationPlanVo publicationPlan) {
		this.publicationPlan = publicationPlan;
	}
	public List getProgramResourceList() {
		return programResourceList;
	}
	public void setProgramResourceList(List programResourceList) {
		this.programResourceList = programResourceList;
	}
	public Object getProgramZip() {
		return programZip;
	}
	public void setProgramZip(Object programZip) {
		this.programZip = programZip;
	}
	
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
	public List<ProgarmPalySceneVo> getSceneList() {
		return sceneList;
	}
	public void setSceneList(List<ProgarmPalySceneVo> sceneList) {
		this.sceneList = sceneList;
	}
}
