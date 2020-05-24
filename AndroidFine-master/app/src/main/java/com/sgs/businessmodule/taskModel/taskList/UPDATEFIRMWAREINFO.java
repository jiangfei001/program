package com.sgs.businessmodule.taskModel.taskList;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.zhangke.zlog.ZLog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.AppContext;
import com.sgs.businessmodule.downloadModel.DownLoadListener;
import com.sgs.businessmodule.downloadModel.DownLoadManager;
import com.sgs.businessmodule.downloadModel.DownLoadService;
import com.sgs.businessmodule.downloadModel.TaskInfo;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.businessmodule.downloadModel.dbcontrol.bean.SQLDownLoadInfo;
import com.sgs.businessmodule.taskModel.TVTask;
import com.sgs.middle.UpdateApk;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventEnum;
import com.sgs.middle.utils.InstallUtil;
import com.sgs.middle.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

public class UPDATEFIRMWAREINFO extends TVTask {

    private DownLoadManager manager;
    TaskInfo info = new TaskInfo();

    @Override
    public void runTv() {
        String prog = super.instructionRequest.getData();
        ZLog.e(TAG, "progJson:" + prog);
        isNeedSend = false;
        UpdateApk updateApk = JSON.parseObject(prog, new TypeReference<UpdateApk>() {
        });

        String url = updateApk.getUrl();
        String AppVersion = updateApk.getAppVersion();
        String filename = updateApk.getFilename();

        if (!StringUtil.isEmpty(AppVersion)) {
            try {
                ZLog.e(TAG, "url:" + url + "AppVersion" + AppVersion);
                int serVersionStr = Integer.parseInt(AppVersion);
                if (!compareVersion(getVersion(), serVersionStr)) {
                    ZLog.e(TAG, "return111:");
                    return;
                }
            } catch (Exception e) {
                ZLog.e(TAG, e.getMessage() + "");
            }
        }

        ZLog.e(TAG, "url:" + url + "AppVersion" + AppVersion + "filename" + filename);
        /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
        /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
        /*服务器一般会有个区分不同文件的唯一ID，用以处理文件重名的情况*/
        //获取当前版本号
        if (!StringUtil.isEmpty(url)) {

            if (StringUtil.isEmpty(filename)) {
                String filenamestr = url.substring(url.lastIndexOf("/") + 1, url.length());
                filename = filenamestr;
            }
            File file = new File(FileHelper.getFileDefaultPath() + "/" + filename);
            if (file.exists()) {
                file.delete();
            }
            manager = DownLoadService.getDownLoadManager();
            String taskId = url;
            info.setFileName(filename);
            info.setTaskID(taskId);
            info.setOnDownloading(true);
            /*将任务添加到下载队列，下载器会自动开始下载*/
            manager.addTask(taskId, url, filename);
            manager.setSingleTaskListener(taskId, new DownloadManagerListener());
        }
    }

    public int getVersion() {
        int version = 0;
        try {
            PackageManager manager = AppContext.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(AppContext.getInstance().getPackageName(), 0);
            version = info.versionCode;
            ZLog.e(TAG, "url:" + version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            ZLog.e(TAG, "Exception:" + e);
            return version = 0;
        }
    }

    /**
     * 版本号比较
     *
     * @param nowVersion
     * @param serVersionStr
     * @return
     */
    public boolean compareVersion(int nowVersion, int serVersionStr) {
       /* if (nowVersion >= serVersionStr) {
            return false;
        } else {
            return true;
        }*/
        return true;
    }

    private class DownloadManagerListener implements DownLoadListener {

        @Override
        public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {

        }

        @Override
        public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            //根据监听到的信息查找列表相对应的任务，更新相应任务的进度
            ZLog.e(TAG, "sqlDownLoadInfo" + sqlDownLoadInfo.getDownloadSize());
           /* for(TaskInfo taskInfo : listdata){
                if(taskInfo.getTaskID().equals(sqlDownLoadInfo.getTaskID())){
                    taskInfo.setDownFileSize(sqlDownLoadInfo.getDownloadSize());
                    taskInfo.setFileSize(sqlDownLoadInfo.getFileSize());
                    ListAdapter.this.notifyDataSetChanged();
                    break;
                }
            }*/
        }

        @Override
        public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {

        }

        @Override
        public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，删除对应的任务
            if (info.getTaskID().equals(sqlDownLoadInfo.getTaskID())) {
                //下载成功进行安装
                ZLog.e(TAG, "url:" + "sqlDownLoadInfo");
                Event event = new Event();
                HashMap<EventEnum, Object> params = new HashMap();
                params.put(EventEnum.EVENT_TEST_MSG1_KEY_PATH, sqlDownLoadInfo.getFilePath());
                event.setParams(params);
                event.setId(EventEnum.EVENT_TEST_INSTALL);
                EventBus.getDefault().post(event);
                System.out.println("getFilePath.toString()" + sqlDownLoadInfo.getFilePath());

                //InstallUtil.installSilent(AppContext.getInstance(), sqlDownLoadInfo.getFilePath());
            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，停止该任务
            ZLog.e(TAG, "url errro:" + "sqlDownLoadInfo");
            if (info.getTaskID().equals(sqlDownLoadInfo.getTaskID())) {

            }
        }
    }
}
