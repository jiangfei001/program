package com.commandModel;

public enum InstructionTypeEnum {

    CLOSE(100, "close", "关闭", "CLOSE"),
    CONTROLVOLUME(101, "controlVolume", "设置音量", "CONTROLVOLUME"),
    DELETEALLPROJECT(102, "deleteAllProject", "清除节目", "DELETEALLPROJECT"),
    DELETEPROJECT(103, "deleteProject", "删除节目", "DELETEPROJECT"),
    DISPATCHERRELEASE(104, "dispatcherRelease", "分发发布", "DISPATCHERRELEASE"),
    FTPPUTPOLICY(105, "FTPPutPolicy", "设置终端上传策略", "FTPPUTPOLICY"),
    GETINFO(106, "getInfo", "获取终端信息", "GETINFO"),
    INSERTMESSAGE(107, "insertMessage", "插播消息", "INSERTMESSAGE"),
    PLAY(108, "play", "插播节目", "PLAY"),
    POWEROFF(109, "powerOff", "关闭终端", "POWEROFF"),
    RELEASE(110, "release", "发布", "RELEASE"),
    RELEASEMUSIC(111, "releaseMusic", "音乐节目发布", "RELEASEMUSIC"),
    RESTARTCOMPUTER(112, "restartComputer", "重启终端", "RESTARTCOMPUTER"),
    SETPLAYLIST(113, "setPlayList", "设置播放列表", "SETPLAYLIST"),
    SETPLAYSCHEDULAR(114, "setPlaySchedular", "设置播放日程", "SETPLAYSCHEDULAR"),
    SETPRIORITY(115, "setPriority", "设置节目优先级", "SETPRIORITY"),
    START(116, "start", "开始", "START"),
    STOP(117, "stop", "停止", "STOP"),
    TAKESCREEN(118, "takeScreen", "截屏", "TAKESCREEN"),
    UPDATAPLAYERBANDWIDTH(119, "upDataPlayerBandWidth", "设置播放端下载带宽", "UPDATAPLAYERBANDWIDTH"),
    UPDATAPLAYERSDATASOURCEINFO(120, "upDataPlayersDataSourceInfo", "更新叫号系统信息", "UPDATAPLAYERSDATASOURCEINFO"),
    UPDATEDATA(121, "updateData", "更新数据信息", "UPDATEDATA"),
    UPDATEPLAYERINFO(122, "updatePlayerInfo", "更新播放端信息", "UPDATEPLAYERINFO"),
    UPDATESERVERINFO(123, "updateServerInfo", "更新分发服务器信息", "UPDATESERVERINFO"),
    ;

    InstructionTypeEnum(int code, String type, String name, String bigstr) {
        this.code = code;
        this.type = type;
        this.name = name;
        this.bigStr = bigstr;
    }

    /**
     * 指令code，主要用于区分指令范围
     */
    private int code;

    public String getBigStr() {
        return bigStr;
    }

    public void setBigStr(String bigStr) {
        this.bigStr = bigStr;
    }

    private String bigStr;

    /**
     * 指令类型
     */
    private String type;

    /**
     * 指令描述
     */
    private String name;

    /**
     * 根据code获取类型
     *
     * @param code
     * @return
     */
    public static String getTypeByCode(int code) {
        for (InstructionTypeEnum instructionType : InstructionTypeEnum.values()) {
            if (code == instructionType.getCode()) {
                return instructionType.getType();
            }
        }
        return null;
    }

    public static InstructionTypeEnum getById(Integer id) {
        for (InstructionTypeEnum transactType : values()) {
            if (transactType.getCode() == id) {
                //获取指定的枚举
                return transactType;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
