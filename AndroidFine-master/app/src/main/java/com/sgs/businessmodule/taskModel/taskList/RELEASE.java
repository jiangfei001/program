package com.sgs.businessmodule.taskModel.taskList;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.businessmodule.downloadModel.DownLoadManager;
import com.sgs.businessmodule.downloadModel.DownLoadService;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.programModel.ProgramDbManager;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgramResource;
import com.sgs.businessmodule.taskModel.TVTask;

import java.io.File;
import java.util.List;

public class RELEASE extends TVTask {

    ProgarmPalyInstructionVo response;
    List<ProgramResource> programResourceList;

    @Override
    public void runTv() {

        String prog = super.instructionRequest.getData();
        response = JSON.parseObject(prog, new TypeReference<ProgarmPalyInstructionVo>() {
        });
        //保存到节目数据中
        saveToDB();
        //启动节目资源下载
        DownLoadManager downLoadManager = DownLoadService.getDownLoadManager();
        //doneProgarm(prog, downLoadManager);
        //加入排期播放列表
        //在节目计划Manager里面插入该排期，并决定是否进行下载还是播放
    }

    public void saveToDB() {
        Log.e(TAG, "saveProgarmPalyInstructionVoRequest");
        //将数据保存到节目播放数据库，并通知节目播放，进行插入播放
        ProgramDbManager.getInstance().saveProgarmPalyInstructionVoRequest(response);
    }

    public void doneProgarm(String progJson, DownLoadManager downLoadManager) {
        Log.e(TAG, "progJson:" + progJson);
        //启动下载
        String programResourceListJson = response.getProgramResourceList();
        programResourceList = JSON.parseArray(programResourceListJson, ProgramResource.class);
        /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
        downLoadManager.changeUser("luffy");
        /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
        downLoadManager.setSupportBreakpoint(true);

        if (response.getTotalStatus() != 1) {
            //判断预设目录下 是否有对应zip包
            /*服务器一般会有个区分不同文件的唯一ID，用以处理文件重名的情况*/
            String taskId;
            String url;
            url = response.getProgramZip();
            taskId = url;
            /*将任务添加到下载队列，下载器会自动开始下载*/
            //判断 Download 数据库是否是完成状态，是就检查文件是否存在  没有启动下载文件  下载成功则copy到预设目录
            int fileStatue = 1;
            if (response.getProgramZipStatus() != 1) {
                File newfile = new File(FileHelper.getFileDefaultPath() + "/" + response.getProgramZipName());
                if (!newfile.exists()) {
                    int download = downLoadManager.addTask(taskId, url, response.getProgramZipName());
                    if (download == 0) {
                        //下载成功则copy到预设目录
                        Log.e(TAG, "getProgramZipName:" + response.getProgramZipName() + "已经存在下载库里面了！");
                    } else if (download == 1) {
                        //需要下载
                        fileStatue = 0;
                        Log.e(TAG, "getProgramZipName:" + response.getProgramZipName() + "需要下载！,正在监听");
                    } else {
                        Log.e(TAG, "getProgramZipName:" + response.getProgramZipName() + "数据库框架判断文件存在，但是实际不在！");
                    }
                } else {
                    response.setProgramZipStatus(1);
                    Log.e(TAG, "getProgramZipName:" + response.getProgramZipName() + "已经存在！");
                }
            } else {
                response.setProgramZipStatus(1);
                Log.e(TAG, "getProgramZipName:" + response.getProgramZipName() + "已经存在！");
            }

            //判断预设目录下 是否有对应 视频/图片文件
            //循环判断 Download 数据库是否是完成状态，是就检查 视频/图片文件   是否存在  没有启动下载  下载成功则copy到预设目录

            for (int i = 0; i < programResourceList.size(); i++) {
                if (programResourceList.get(i).getDownStatus() != 1) {
                    String resourceurl = programResourceList.get(i).getUrl();
                    String taskResourceId;
                    taskResourceId = resourceurl;
                    String resourceurlFilename = programResourceList.get(i).getFileName();
                    /*将任务添加到下载队列，下载器会自动开始下载*/
                    //判断 Download 数据库是否是完成状态，是就检查文件是否存在  没有启动下载文件  下载成功则copy到预设目录
                    File newfile = new File(FileHelper.getFileDefaultPath() + "/" + resourceurlFilename);
                    if (!newfile.exists()) {
                        int resourcedownload = downLoadManager.addTask(taskResourceId, resourceurl, resourceurlFilename);
                        if (resourcedownload == 0) {
                            //文件已经存在
                            Log.e(TAG, "resourceurlFilename：" + resourceurlFilename + "已经存在下载库里面！");
                        } else if (resourcedownload == 1) {
                            Log.e(TAG, "resourceurlFilename：" + resourceurlFilename + "需要下载！,正在监听");
                            //需要下载
                            fileStatue = 0;
                            //manager.setSingleTaskListener(taskResourceId, new TaskProgarm.DownloadManagerListener());
                        } else {
                            Log.e(TAG, "resourceurlFilename:" + resourceurlFilename + "数据库框架判断文件存在，但是实际不在！");
                        }
                    } else {
                        Log.e(TAG, "resourceurlFilename：" + resourceurlFilename + "已经存在！");
                        programResourceList.get(i).setDownStatus(1);
                    }
                }
            }
            Log.e(TAG, response.toString());
        }
        //创建节目任务，并进行排期处理
    }
}
