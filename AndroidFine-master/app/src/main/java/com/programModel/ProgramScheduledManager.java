package com.programModel;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.downloadModel.DownLoadListener;
import com.downloadModel.DownLoadManager;
import com.downloadModel.DownLoadService;
import com.downloadModel.dbcontrol.FileHelper;
import com.downloadModel.dbcontrol.bean.SQLDownLoadInfo;
import com.programModel.entity.ProgarmPalyInstructionVo;
import com.programModel.entity.ProgramResource;
import com.programModel.entity.PublicationPlanVo;
import com.sgs.jfei.common.AppContext;
import com.utils.StringUtils;
import com.utils.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProgramScheduledManager {

    private final String TAG = "ProgramDbManager";

    private Context context;

    //所有没有过期的节目单
    List<ProgarmPalyInstructionVo> list;

    LinkedList<ProgarmPalyInstructionVo> progarmPalyInstructionVos;

    DownLoadManager manager;

    private ProgramScheduledManager(Context context) {
        this.context = context;
        manager = DownLoadService.getDownLoadManager();
        /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
        manager.changeUser("luffy");
        /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
        manager.setSupportBreakpoint(true);
        initAllProgramTask();
    }

    //从数据库中获取所有的节目数据
    public void initAllProgramTask() {

        list = ProgramDbManager.getInstance().getAllProgarmPalyInstructionVo();

        progarmPalyInstructionVos = new LinkedList<>();

        checkResouce(list);

        manager.setAllTaskListener(new DownloadManagerListener());
        //启动播放拉
        programTaskManager = new ProgramTaskManager(context, progarmPalyInstructionVos);

    }

    private static ProgramScheduledManager instance;

    public static ProgramScheduledManager getInstance() {
        if (instance == null) {
            synchronized (ProgramDbManager.class) {
                if (instance == null) {
                    instance = new ProgramScheduledManager(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    //开机的时候，进行节目排期任务启动
    ProgramTaskManager programTaskManager;

    public void checkResouce(List<ProgarmPalyInstructionVo> prolist) {
        if (prolist != null && prolist.size() > 0) {
            for (int i = 0; i < prolist.size(); i++) {
                doProgarm(prolist.get(i), false);
            }
        }
    }

    public void doProgarm(ProgarmPalyInstructionVo response, boolean isInsert) {

        if (isInsert) {
            list.add(response);
        }

        String publicationPlanJson = response.getPublicationPlan();

        PublicationPlanVo publicationPlanVo = JSON.parseObject(publicationPlanJson, new TypeReference<PublicationPlanVo>() {
        });
        response.setPublicationPlanObject(publicationPlanVo);

        Log.e(TAG, "response.getProgramResourceList:" + response.getProgramResourceList());

        response.setProgramResourceListArray(JSON.parseArray(response.getProgramResourceList(), ProgramResource.class));

        List<ProgramResource> programResourceList = response.getProgramResourceListArray();

        List<ProgramResource> programMusicList = null;
        if (!StringUtils.isEmpty(response.getProgramMusicList())) {
            Log.e(TAG, "response.getProgramMusicList:" + response.getProgramMusicList());
            response.setProgramMusicListArray(JSON.parseArray(response.getProgramMusicList(), ProgramResource.class));
            programMusicList = response.getProgramMusicListArray();
        }
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
                    //需要下载
                    fileStatue = 0;
                    int download = manager.addTask(taskId, url, response.getProgramZipName());
                    if (download == 0) {
                        //下载成功则copy到预设目录
                        Log.e("sqlDownLoadInfo", "getProgramZipName:" + response.getProgramZipName() + "已经存在下载库里面了！");
                    } else if (download == 1) {
                        Log.e("sqlDownLoadInfo", "getProgramZipName:" + response.getProgramZipName() + "需要下载！,正在监听");
                    } else {
                        Log.e("sqlDownLoadInfo", "getProgramZipName:" + response.getProgramZipName() + "数据库框架判断文件存在，但是实际不在！");
                    }
                } else {
                    response.setProgramZipStatus(1);
                    Log.e("sqlDownLoadInfo", "getProgramZipName:" + response.getProgramZipName() + "已经存在！");
                }
            } else {
                response.setProgramZipStatus(1);
                Log.e("sqlDownLoadInfo", "getProgramZipName:" + response.getProgramZipName() + "已经存在！");
            }

            //判断预设目录下 是否有对应 视频/图片文件
            //循环判断 Download 数据库是否是完成状态，是就检查 视频/图片文件   是否存在  没有启动下载  下载成功则copy到预设目录
            if (programResourceList != null && programResourceList.size() > 0) {
                for (int i = 0; i < programResourceList.size(); i++) {
                    if (programResourceList.get(i).getDownStatus() != 1) {
                        String resourceurl = programResourceList.get(i).getUrl();
                        String taskResourceId;
                        taskResourceId = resourceurl;
                        String resourceurlFilename = programResourceList.get(i).getFileName();
                        String resourceurlFilenameVirPath = programResourceList.get(i).getVirtualPath();
                        //*将任务添加到下载队列，下载器会自动开始下载
                        //判断 Download 数据库是否是完成状态，是就检查文件是否存在  没有启动下载文件  下载成功则copy到预设目录
                        File newfile = new File(FileHelper.getFileDefaultPath() + "/" + resourceurlFilenameVirPath);
                        if (!newfile.exists()) {
                            //需要下载
                            fileStatue = 0;
                            int resourcedownload = manager.addTask(taskResourceId, resourceurl, resourceurlFilename, newfile.getPath());
                            if (resourcedownload == 0) {
                                //文件已经存在
                                Log.e("sqlDownLoadInfo", "resourceurlFilename：" + resourceurlFilename + "已经存在下载库里面！");
                            } else if (resourcedownload == 1) {
                                Log.e("sqlDownLoadInfo", "resourceurlFilename：" + resourceurlFilename + "需要下载！,正在监听");
                            } else {
                                Log.e("sqlDownLoadInfo", "resourceurlFilename:" + resourceurlFilename + "数据库框架判断文件存在，但是实际不在！");
                            }
                        } else {
                            Log.e("sqlDownLoadInfo", "resourceurlFilename：" + resourceurlFilename + "已经存在！");
                            programResourceList.get(i).setDownStatus(1);
                        }
                    }
                }
            }

            for (int i = 0; i < programMusicList.size(); i++) {
                if (programMusicList.get(i).getDownStatus() != 1) {
                    String resourceurl = programMusicList.get(i).getUrl();
                    String taskResourceId;
                    taskResourceId = resourceurl;
                    String resourceurlFilename = programMusicList.get(i).getFileName();
                    String resourceurlFilenameVirPath = programMusicList.get(i).getVirtualPath();
                    //*将任务添加到下载队列，下载器会自动开始下载
                    //判断 Download 数据库是否是完成状态，是就检查文件是否存在  没有启动下载文件  下载成功则copy到预设目录
                    File newfile = new File(FileHelper.getFileDefaultPath() + "/" + resourceurlFilenameVirPath);
                    if (!newfile.exists()) {
                        //需要下载
                        fileStatue = 0;
                        int resourcedownload = manager.addTask(taskResourceId, resourceurl, resourceurlFilename, newfile.getPath());
                        if (resourcedownload == 0) {
                            //文件已经存在
                            Log.e("sqlDownLoadInfo", "resourceurlFilename：" + resourceurlFilename + "已经存在下载库里面！");
                        } else if (resourcedownload == 1) {
                            Log.e("sqlDownLoadInfo", "resourceurlFilename：" + resourceurlFilename + "需要下载！,正在监听");
                        } else {
                            Log.e("sqlDownLoadInfo", "resourceurlFilename:" + resourceurlFilename + "数据库框架判断文件存在，但是实际不在！");
                        }
                    } else {
                        Log.e("sqlDownLoadInfo", "resourceurlFilename：" + resourceurlFilename + "已经存在！");
                        programMusicList.get(i).setDownStatus(1);
                    }
                }
            }

            if (fileStatue == 1) {
                Log.e(TAG, "fileStatue == 1：");
                //判断今天是否播放
                if (ProgramUtil.getWeekPalySchedule(response)) {
                    Log.e(TAG, "getWeekPalySchedule：");
                    progarmPalyInstructionVos.add(response);
                    if (progarmPalyInstructionVos.size() == 1 && isInsert) {
                        Log.e(TAG, "progarmPalyInstructionVos.size() == 1 && isInsert");
                        programTaskManager.addTask(response);
                        programTaskManager.startLooperTask();
                    }
                }

                Log.e(TAG, "doProgarm 所有资源都存在：" + response.getId());
                //轮询的时候，只有所有的资源都准备好了，才算整体成功
                response.setTotalStatus(1);
                list.remove(response);
            }
        }

        System.out.println(response.toString());
        System.out.println(publicationPlanVo);
    }

    //获取之后，进行资源统一判断与处理，下载资源完毕状态下才会进行统一排期
    //临时插入一条节目数据，并进行资源处理
    public void insertProgram() {

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

            //下载完毕检查是否完全下载完毕

            //根据监听到的信息查找列表相对应的任务，删除对应的任务
            Log.e("sqlDownLoadInfo", "下载成功通知：" + sqlDownLoadInfo.getTaskID());

            int resourceTotle = 1;

            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                //如果是ProgramZip
                ProgarmPalyInstructionVo response1 = (ProgarmPalyInstructionVo) iterator.next();
                if (response1.getProgramZipStatus() != 1 && response1.getProgramZip().equals(sqlDownLoadInfo.getTaskID())) {
                    //无需copy到文件
                    response1.setProgramZipStatus(1);
                    Log.e("sqlDownLoadInfo", "setProgramZipStatus下载成功：" + sqlDownLoadInfo.getTaskID());
                    File newfile = new File(FileHelper.getFileDefaultPath() + "/" + response1.getProgramZipName());
                    try {
                        Log.e("sqlDownLoadInfo", "开始解压：" + FileHelper.getFileDefaultPath());
                        ZipUtil.upZipFile(newfile, FileHelper.getFileDefaultPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                for (int i = 0; i < response1.getProgramResourceListArray().size(); i++) {
                    if (response1.getProgramResourceListArray().get(i).getUrl().equals(sqlDownLoadInfo.getTaskID())) {
                        Log.e("sqlDownLoadInfo", "programResourceList1下载成功：" + sqlDownLoadInfo.getTaskID());
                        response1.getProgramResourceListArray().get(i).setDownStatus(1);
                    }
                    if (response1.getProgramResourceListArray().get(i).getDownStatus() != 1) {
                        Log.e("sqlDownLoadInfo", "programResourceList1我还没有下载成功：" + response1.getProgramResourceListArray().get(i).getUrl());
                        resourceTotle = 0;
                    }
                }
                
                if (response1.getProgramMusicListArray() != null && response1.getProgramMusicListArray().size() > 0) {
                    for (int i = 0; i < response1.getProgramMusicListArray().size(); i++) {
                        if (response1.getProgramMusicListArray().get(i).getUrl().equals(sqlDownLoadInfo.getTaskID())) {
                            Log.e("sqlDownLoadInfo", "programMusicLis1下载成功：" + sqlDownLoadInfo.getTaskID());
                            response1.getProgramMusicListArray().get(i).setDownStatus(1);
                        }
                        if (response1.getProgramMusicListArray().get(i).getDownStatus() != 1) {
                            Log.e("sqlDownLoadInfo", "programMusicLis1我还没有下载成功：" + response1.getProgramMusicListArray().get(i).getUrl());
                            resourceTotle = 0;
                        }
                    }
                }


                if (response1.getProgramZipStatus() != 1) {
                    Log.e("DownloadManagerListener", "zip还没有下载成功：" + sqlDownLoadInfo.getTaskID());
                    resourceTotle = 0;
                }
                if (resourceTotle == 1) {
                    response1.setTotalStatus(1);
                    Log.e("DownloadManagerListener", "onSuccess所有资源都存在：" + response1.getId());
                    iterator.remove();

                    if (ProgramUtil.getWeekPalySchedule(response1)) {
                        Log.e("DownloadManagerListener", "getWeekPalySchedule：" + response1.getId());
                        progarmPalyInstructionVos.add(response1);
                        programTaskManager.addTask(response1);
                        if (progarmPalyInstructionVos.size() == 1) {
                            Log.e("DownloadManagerListener", "startLooperTask：" + response1.getId());
                            programTaskManager.startLooperTask();
                        }
                    }
                }
            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，停止该任务
            Log.e("sqlDownLoadInfo ", "sqlDownLoadInfo11 onError" + sqlDownLoadInfo.getTaskID());
        }
    }
}
