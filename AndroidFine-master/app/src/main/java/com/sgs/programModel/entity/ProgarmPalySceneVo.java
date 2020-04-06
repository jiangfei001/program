package com.sgs.programModel.entity;

/**
 * 节目发布命令中的场景信息
 *
 * @author Administrator
 */
public class ProgarmPalySceneVo {

    private int sceneId;

    /**
     * html 名称
     */
    private String html;
    /**
     * 播放时间,秒
     */
    private int playTime;


    private boolean palyMusic;

    private String sceneName;

    public ProgarmPalySceneVo() {
        super();
    }

    public ProgarmPalySceneVo(String html, int playTime) {
        super();
        this.html = html;
        this.playTime = playTime;
    }

    public boolean isPalyMusic() {
        return palyMusic;
    }

    public void setPalyMusic(boolean palyMusic) {
        this.palyMusic = palyMusic;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    @Override
    public String toString() {
        return "ProgarmPalySceneVo{" +
                "sceneId=" + sceneId +
                ", html='" + html + '\'' +
                ", playTime=" + playTime +
                ", palyMusic=" + palyMusic +
                ", sceneName='" + sceneName + '\'' +
                '}';
    }
}
