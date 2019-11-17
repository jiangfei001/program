package com.taskModel.taskFactory;

import com.commandModel.InstructionTypeEnum;
import com.dbModel.entity.InstructionRequest;
import com.taskModel.ITask;
import com.taskModel.TVTask;
import com.taskModel.taskList.CLOSE;

public class TaskFactory {

  /*  CLOSE(100, "close", "关闭"),
    CONTROLVOLUME(101, "controlVolume", "设置音量"),
    DELETEALLPROJECT(102, "deleteAllProject", "清除节目"),
    DELETEPROJECT(103, "deleteProject", "删除节目"),
    DISPATCHERRELEASE(104, "dispatcherRelease", "分发发布"),
    FTPPUTPOLICY(105, "FTPPutPolicy", "设置终端上传策略"),
    GETINFO(106, "getInfo", "获取终端信息"),
    INSERTMESSAGE(107, "insertMessage", "插播消息"),
    PLAY(108, "play", "插播节目"),
    POWEROFF(109, "powerOff", "关闭终端"),
    RELEASE(110, "release", "发布"),
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
    UPDATESERVERINFO(123, "updateServerInfo", "更新分发服务器信息"),*/


    // 简单工厂，根据字符串创建相应的对象
    public static TVTask createTask(InstructionRequest instructionRequest) {
        InstructionTypeEnum instructionTypeEnum = (InstructionTypeEnum.getById(instructionRequest.getCode()));
        TVTask operationObj = null;
        try {
            Class operationObj1 = Class.forName("com.taskModel.taskList." + instructionTypeEnum.getType().toUpperCase());
            try {
                operationObj = (TVTask) operationObj1.newInstance();
                operationObj.setInstructionRequest(instructionRequest);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return operationObj;
    }


    private static ITask closeTask() {
        System.out.println("加法运算");
        return new CLOSE();
    }
}