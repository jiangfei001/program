package com.sgs.businessmodule.taskModel.taskFactory;

public enum InstructionTypeEnum {

    CLOSE(100, "close", "关闭"),
    CONTROLVOLUME(101, "controlVolume", "设置音量"),
    DELETEALLPROJECT(102, "deleteAllProject", "清除节目"),
    DELETEPROJECT(103, "deleteProject", "删除节目"),
    DISPATCHERRELEASE(104, "dispatcherRelease", "分发发布"),
    FTPPUTPOLICY(105, "FTPPutPolicy", "设置终端上传策略"),
    GETINFO(106, "getInfo", "获取终端信息"),
    INSERTMESSAGE(107, "insertMessage", "插播消息"),
    PLAY(108, "play", "插播节目"),
    POWEROFF(109, "powerOff", "关闭终端"),
    RELEASE(110, "release", "发布节目"),
    RELEASEMUSIC(111, "releaseMusic", "音乐节目发布"),
    RESTARTCOMPUTER(112, "restartComputer", "重启终端"),
    SETPLAYLIST(113, "setPlayList", "设置播放列表"),
    SETPLAYSCHEDULAR(114, "setPlaySchedular", "设置播放日程"),
    SETPRIORITY(115, "setPriority", "设置节目优先级"),
    START(116, "start", "开始"),
    STOP(117, "stop", "停止"),
    TAKESCREEN(118, "takeScreen", "截屏"),
    UPDATAPLAYERBANDWIDTH(119, "upDataPlayerBandWidth", "设置播放端下载带宽"),
    UPDATAPLAYERSDATASOURCEINFO(120, "upDataPlayersDataSourceInfo", "更新叫号系统信息"),
    UPDATEDATA(121, "updateData", "更新数据信息"),
    UPDATEPLAYERINFO(122, "updatePlayerInfo", "更新播放端信息"),
    UPDATESERVERINFO(123, "updateServerInfo", "更新分发服务器信息"),
    UPDATEFIRMWAREINFO(124, "updateFirmwareInfo", "更新分发服务器信息"),
    UNINSTALL(125, "uninstall", "远程卸载APK"),
    SETOSTERMINAL(127, "setOsTerminal", "设置每天开关机时间"),
    GETALLPROGRAM(128, "sendGetProgramOrder", "获取终端节目列表"),
    GETNEWPROGRAMMESSAGE(129, "getNewProgramMessage", "获取正在播放节目信息"),
    CUTMSG(130, "cutMsg", "插播消息"),
    CLEARCUTMSG(131, "clearCutMsg", "清空插播消息"),
    DELETECUTMSG(132, "deleteCutMsg", "删除插播消息"),
    GETDAYPROGRAM(133, "getDayProgram", "获取当天节目"),
    INSTALL(134, "install", "应用安装"),
    ;

    /* {id=2652, code=101, type='controlVolume', data='{"ids":["38"],"volumeValue":0,"taskCard":"volumeTab"
             ,"taskVolumeTime":"00:02:04","volumenum":32,"weekList2":["星期二"],
         "userEntity":{"createTime":1478833871000,"deptId":2,
             "email":"root@axzq.com","mobile":"18888888888",
             "password":"e1153123d7d180ceeb820d577ff119876678732a68eef4e6ffc0b1f06a01f91b",
             "salt":"YzcmCZNvbXocrsz9dm8e","status":1,"userId":1,"username":"admin"}}',
             expirationTime=null, priority=1, beginTime=null}*/
    //InstructionRequest{id=2504, code=134, type='install', data='{"apkPath":"http://qiniu.xinlianchuangmei.com/admin/202002/08/38025e02884a4a5fb71290ffbada46cd.apk"}', expirationTime=null, priority=1, beginTime=null}
    InstructionTypeEnum(int code, String type, String name) {
        this.code = code;
        this.type = type;
        this.name = name;
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
        for (InstructionTypeEnum instructionType : values()) {
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
