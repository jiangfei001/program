package com.sgs.programModel;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sgs.businessmodule.downloadModel.DownLoadListener;
import com.sgs.businessmodule.downloadModel.DownLoadManager;
import com.sgs.businessmodule.downloadModel.DownLoadService;
import com.sgs.businessmodule.downloadModel.dbcontrol.FileHelper;
import com.sgs.businessmodule.downloadModel.dbcontrol.bean.SQLDownLoadInfo;
import com.sgs.middle.utils.FileUtil;
import com.sgs.programModel.entity.ProgarmPalyInstructionVo;
import com.sgs.programModel.entity.ProgramResource;
import com.sgs.programModel.entity.PublicationPlanVo;
import com.sgs.AppContext;
import com.sgs.middle.utils.StringUtils;
import com.sgs.middle.utils.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProgramScheduledManager {

    private static final String TAG = "ProgramDbManager";

    private Context context;

    //加载数据库中所有的List 包括接受命令获取的List
    List<ProgarmPalyInstructionVo> list;

    //已经成功下载 和 今天需要进行下载的任务
    LinkedList<ProgarmPalyInstructionVo> progarmPalyInstructionVos;
    LinkedList<ProgarmPalyInstructionVo> progarmPalyInstructionVosPri;

    DownLoadManager manager;

    private ProgramScheduledManager(Context context) {
        this.context = context;
        manager = DownLoadService.getDownLoadManager();
        /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
        manager.changeUser("luffy");
        /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
        manager.setSupportBreakpoint(true);
        Log.e(TAG, "initAllProgramTask");
        initAllProgramTask();
    }

    public void initAllProgramTask() {
        //从数据库中获取所有的节目数据
        list = ProgramDbManager.getInstance().getAllProgarmPalyInstructionVo();

        progarmPalyInstructionVos = new LinkedList<>();
        progarmPalyInstructionVosPri = new LinkedList<>();

        if (programTaskManager != null) {
            Log.e(TAG, "programTaskManager.stopLooper");
            programTaskManager.stopLooper();
            programTaskManager = null;
        }

        //判断资源是否已经下载，并且是在今天的下载范围
        checkResouce(list);
        //设置下载监听机
        manager.setAllTaskListener(new DownloadManagerListener());

        if (programTaskManager != null) {
            programTaskManager = null;
        }
        //开始轮播
        programTaskManager = new ProgramTaskManager(context, progarmPalyInstructionVos, progarmPalyInstructionVosPri);

    }

    private static ProgramScheduledManager instance;

    public void clearLooperAndDBAndResource() {

        list = null;
        progarmPalyInstructionVos = null;
        progarmPalyInstructionVosPri = null;

        Log.e(TAG, "clearLooperAndDBAndResource programTaskManager.stopLooper");
        ProgramDbManager.getInstance().delectAllProgarmPalyInstructionVoRequest();
        programTaskManager.stopLooper();
        //CommandHelper.deleteDir(FileHelper.getFileDefaultPath());
        Log.e(TAG, "initAllProgramTask clearLooperAndDBAndResource");
        initAllProgramTask();
    }

    public void clearLooperAndDBById(int id) {

        ProgramDbManager.getInstance().delectProgarmPalyInstructionVoRequestById(id);
        programTaskManager.removeByid(id);

        for (int i = 0; i < progarmPalyInstructionVos.size(); i++) {
            if (progarmPalyInstructionVos.get(i).getId() == 1) {
                progarmPalyInstructionVos.remove(i);
                break;
            }
        }

        for (int i = 0; i < progarmPalyInstructionVosPri.size(); i++) {
            if (progarmPalyInstructionVosPri.get(i).getId() == 1) {
                progarmPalyInstructionVosPri.remove(i);
                break;
            }
        }
    }


    public static ProgramScheduledManager getInstance() {
        if (instance == null) {
            synchronized (ProgramDbManager.class) {
                if (instance == null) {
                    Log.e(TAG, "getInstance");
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

    public void saveToDB(ProgarmPalyInstructionVo progarmPalyInstructionVo) {
        Log.e(TAG, "saveProgarmPalyInstructionVoRequest");
        //将数据保存到节目播放数据库，并通知节目播放，进行插入播放
        ProgramDbManager.getInstance().saveProgarmPalyInstructionVoRequest(progarmPalyInstructionVo);
    }

    public void doProgarm(ProgarmPalyInstructionVo response, boolean isInsert) {

        if (isInsert) {
            list.add(response);
            response.setProgramZipName(FileUtil.getFileNameByVirtualPath(response.getProgramZip()));
            //保存到节目数据中
            saveToDB(response);
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

                        //String resourceurlFilename = programResourceList.get(i).getFileName();
                        //"virtualPath": "admin/201912/01/fdf63c7160f0493f864d9cd3a7c053bb.mp4"
                        String resourceurlFilename = FileUtil.getFileNameByVirtualPath(programResourceList.get(i).getVirtualPath());

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
            if (programMusicList != null && programMusicList.size() > 0) {
                for (int i = 0; i < programMusicList.size(); i++) {
                    if (programMusicList.get(i).getDownStatus() != 1) {
                        String resourceurl = programMusicList.get(i).getUrl();
                        String taskResourceId;
                        taskResourceId = resourceurl;
                        String resourceurlFilename = FileUtil.getFileNameByVirtualPath(programMusicList.get(i).getVirtualPath());
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
            }

            if (fileStatue == 1) {
                Log.e(TAG, "fileStatue == 1：");
                //判断今天是否播放
                addProgramToTask(response, isInsert);
                Log.e(TAG, "doProgarm 所有资源都存在：" + response.getId());
                //轮询的时候，只有所有的资源都准备好了，才算整体成功
                response.setTotalStatus(1);
                list.remove(response);
            }
        }
        System.out.println(response.toString());
        System.out.println(publicationPlanVo);
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
                if (response1.getProgramResourceListArray() != null && response1.getProgramResourceListArray().size() > 0) {
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
                    addProgramToTask(response1, true);
                }
            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，停止该任务
            Log.e("sqlDownLoadInfo ", "sqlDownLoadInfo11 onError" + sqlDownLoadInfo.getTaskID());
        }
    }

    private void addProgramToTask(ProgarmPalyInstructionVo response, boolean isInsert) {
        if (ProgramUtil.getWeekPalySchedule(response)) {
            Log.e(TAG, "getWeekPalySchedule：");
            if (isInsert) {
                Log.e(TAG, "progarmPalyInstructionVos.size() == 1 && isInsert");
                if (response.getPublicationPlanObject().isExclusive()) {
                    programTaskManager.insertTask(response, true);
                } else {
                    programTaskManager.insertTask(response, false);
                }
            } else {
            }

            progarmPalyInstructionVos.add(response);
            progarmPalyInstructionVosPri.add(response);

        }
    }
}
