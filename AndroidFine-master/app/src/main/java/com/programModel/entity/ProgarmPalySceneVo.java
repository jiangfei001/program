package com.programModel.entity;

/**
 * 节目发布命令中的场景信息
 * @author Administrator
 *
 */
public class ProgarmPalySceneVo {

	/**
	 * xml 名称
	 */
	private String xml;
	/**
	 * 播放时间,秒
	 */
	private int playTime;
	
	
	public ProgarmPalySceneVo() {
		super();
	}
	public ProgarmPalySceneVo(String xml, int playTime) {
		super();
		this.xml = xml;
		this.playTime = playTime;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public int getPlayTime() {
		return playTime;
	}
	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}
	
}
