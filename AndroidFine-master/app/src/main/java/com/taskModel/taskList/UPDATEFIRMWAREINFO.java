package com.taskModel.taskList;


import com.downloadModel.DownLoadListener;
import com.downloadModel.DownLoadManager;
import com.downloadModel.DownLoadService;
import com.downloadModel.TaskInfo;
import com.downloadModel.dbcontrol.bean.SQLDownLoadInfo;
import com.taskModel.TVTask;

public class UPDATEFIRMWAREINFO extends TVTask {

    private DownLoadManager manager;
    TaskInfo info = new TaskInfo();

    @Override
    public void runTv() {
        manager = DownLoadService.getDownLoadManager();
        /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
        manager.changeUser("luffy");
        /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
        manager.setSupportBreakpoint(true);
        info.setFileName("programm.apk");
        /*服务器一般会有个区分不同文件的唯一ID，用以处理文件重名的情况*/
        String taskId;
        taskId = "asdfasdfadf";
        String url;
        url = "http://sdf/ss.apk";
        String filename;
        filename = "asdfasdf.apk";
        info.setTaskID(taskId);
        info.setOnDownloading(true);
        /*将任务添加到下载队列，下载器会自动开始下载*/
        manager.addTask(taskId, url, filename);
        manager.setSingleTaskListener(taskId, new DownloadManagerListener());
    }

    private class DownloadManagerListener implements DownLoadListener {

        @Override
        public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {

        }

        @Override
        public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
            //根据监听到的信息查找列表相对应的任务，更新相应任务的进度
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

            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，停止该任务
            if (info.getTaskID().equals(sqlDownLoadInfo.getTaskID())) {

            }
        }
    }
}
