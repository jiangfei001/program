package com.programModel;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.downloadModel.DownLoadListener;
import com.downloadModel.DownLoadManager;
import com.downloadModel.dbcontrol.FileHelper;
import com.downloadModel.dbcontrol.bean.SQLDownLoadInfo;
import com.eventControlModel.Event;
import com.eventControlModel.EventEnum;
import com.programModel.entity.ProgarmPalyInstructionVo;
import com.utils.ZipUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskProgarm {

    public static void main(String... args) {

    }

    public static void progarmTest1(DownLoadManager manager) {
        String orgin = "{\n" +
                "\t\"id\": 27,\n" +
                "\t\"playTime\": 29,\n" +
                "\t\"programName\": \"节目27\",\n" +
                "\t\"programResourceList\": [{\n" +
                "\t\t\"fileName\": \"0e5e4cc4f12a72f37a0019707d333f49.jpg\",\n" +
                "\t\t\"id\": 24,\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"url\": \"http://yanxuan.nosdn.127.net/0e5e4cc4f12a72f37a0019707d333f49.jpg\",\n" +
                "\t\t\"virtualPath\": \"admin/201911/12/0e5e4cc4f12a72f37a0019707d333f49.jpg\"\n" +
                "\t}, {\n" +
                "\t\t\"fileName\": \"0e5e4cc4f12a72f37a0019707d333f49.jpg\",\n" +
                "\t\t\"id\": 19,\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"url\": \"http://yanxuan.nosdn.127.net/0e5e4cc4f12a72f37a0019707d333f49.jpg\",\n" +
                "\t\t\"virtualPath\": \"admin/201911/12/0e5e4cc4f12a72f37a0019707d333f49.jpg\"\n" +
                "\t}],\n" +
                "\t\"programZipName\": \"c86fc77688e44183b0e1c991cfc6fb57.zip\",\n" +
                "\t\"programZip\": \"http://q0u8hijil.bkt.clouddn.com/system/20191129/c86fc77688e44183b0e1c991cfc6fb57.zip\",\n" +
                "\t\"publicationPlan\": {\n" +
                "\t\t\"customizedPalySchedule\": [],\n" +
                "\t\t\"deadline\": \"2019-12-31 00:00\",\n" +
                "\t\t\"deadlineV\": \"2019-12-31 00:00\",\n" +
                "\t\t\"exclusive\": false,\n" +
                "\t\t\"planType\": 1,\n" +
                "\t\t\"weekPalySchedule\": [{\n" +
                "\t\t\t\"dateStr\": \"星期一\",\n" +
                "\t\t\t\"id\": 1,\n" +
                "\t\t\t\"times\": \"08:00-10:00|12:00-16:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期二\",\n" +
                "\t\t\t\"id\": 2,\n" +
                "\t\t\t\"times\": \"08:00-10:00|12:00-16:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期三\",\n" +
                "\t\t\t\"id\": 3,\n" +
                "\t\t\t\"times\": \"08:00-10:00|12:00-16:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期四\",\n" +
                "\t\t\t\"id\": 4,\n" +
                "\t\t\t\"times\": \"08:00-10:00|12:00-16:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期五\",\n" +
                "\t\t\t\"id\": 5,\n" +
                "\t\t\t\"times\": \"08:00-10:00|12:00-16:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期六\",\n" +
                "\t\t\t\"id\": 6,\n" +
                "\t\t\t\"times\": \"09:00-10:00|14:00-18:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期日\",\n" +
                "\t\t\t\"id\": 7,\n" +
                "\t\t\t\"times\": \"09:00-12:00|14:00-18:00\"\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"resolution\": \"1376*800\",\n" +
                "\t\"sceneList\": [{\n" +
                "\t\t\"playTime\": 11,\n" +
                "\t\t\"html\": \"9e4a7e1f45b32b1d1372cbd997d326d7.html\"\n" +
                "\t}, {\n" +
                "\t\t\"playTime\": 18,\n" +
                "\t\t\"html\": \"6884533148ed4f9ed8bbf1a1b891f1bd.html\"\n" +
                "\t}],\n" +
                "\t\"sceneNum\": 2\n" +
                "}";
        String orgin2 = "{\n" +
                "\t\"id\": 29,\n" +
                "\t\"playTime\": 35,\n" +
                "\t\"programName\": \"节目29\",\n" +
                "\t\"programMusicList\": [{\n" +
                "\t\t\"fileName\": \"fdf63c7160f0493f864d9cd3a7c053bb.mp4\",\n" +
                "\t\t\"id\": 29,\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"url\": \"http://q0u8hijil.bkt.clouddn.com/admin/201912/01/fdf63c7160f0493f864d9cd3a7c053bb.mp4\",\n" +
                "\t\t\"virtualPath\": \"admin/201912/01/fdf63c7160f0493f864d9cd3a7c053bb.mp4\"\n" +
                "\t}, {\n" +
                "\t\t\"fileName\": \"337a38b7bf13416180b661eef3a10c00.mp4\",\n" +
                "\t\t\"id\": 21,\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"url\": \"https://test-1256466468.cos.ap-guangzhou.myqcloud.com/share/20181217/337a38b7bf13416180b661eef3a10c00.mp4\",\n" +
                "\t\t\"virtualPath\": \"admin/201911/12/337a38b7bf13416180b661eef3a10c00.mp4\"\n" +
                "\t}],\n" +
                "\t\"programName\": \"4444\",\n" +
                "\t\"programResourceList\": [{\n" +
                "\t\t\"fileName\": \"31e3b808a0bf400ea425d9984cc8402a.mp4\",\n" +
                "\t\t\"id\": 25,\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"url\": \"http://q0u8hijil.bkt.clouddn.com/admin/201912/01/31e3b808a0bf400ea425d9984cc8402a.mp4\",\n" +
                "\t\t\"virtualPath\": \"admin/201912/01/31e3b808a0bf400ea425d9984cc8402a.mp4\"\n" +
                "\t}, {\n" +
                "\t\t\"fileName\": \"ac3274a595b74c899798a80a5d5a2f87.jpg\",\n" +
                "\t\t\"id\": 20,\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"url\": \"http://yanxuan.nosdn.127.net/599ee624350ecb9e70c32375c0cd4807.jpg\",\n" +
                "\t\t\"virtualPath\": \"admin/201911/12/ac3274a595b74c899798a80a5d5a2f87.jpg\"\n" +
                "\t}, {\n" +
                "\t\t\"fileName\": \"0e5e4cc4f12a72f37a0019707d333f49.jpg\",\n" +
                "\t\t\"id\": 19,\n" +
                "\t\t\"status\": 0,\n" +
                "\t\t\"url\": \"http://yanxuan.nosdn.127.net/0e5e4cc4f12a72f37a0019707d333f49.jpg\",\n" +
                "\t\t\"virtualPath\": \"admin/201911/12/0e5e4cc4f12a72f37a0019707d333f49.jpg\"\n" +
                "\t}],\n" +
                "\t\"programZipName\": \"d4915ca9ac37463aa9b064fef203543a.zip\",\n" +
                "\t\"programZip\": \"http://q0u8hijil.bkt.clouddn.com/system/20191209/d4915ca9ac37463aa9b064fef203543a.zip\",\n" +
                "\t\"publicationPlan\": {\n" +
                "\t\t\"customizedPalySchedule\": [],\n" +
                "\t\t\"deadline\": \"2019-12-31 00:00\",\n" +
                "\t\t\"deadlineV\": \"2019-12-31 00:00\",\n" +
                "\t\t\"exclusive\": true,\n" +
                "\t\t\"planType\": 1,\n" +
                "\t\t\"weekPalySchedule\": [{\n" +
                "\t\t\t\"dateStr\": \"星期一\",\n" +
                "\t\t\t\"id\": 1,\n" +
                "\t\t\t\"times\": \"05:00-08:00|12:00-23:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期二\",\n" +
                "\t\t\t\"id\": 2,\n" +
                "\t\t\t\"times\": \"05:00-08:00|12:00-23:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期三\",\n" +
                "\t\t\t\"id\": 3,\n" +
                "\t\t\t\"times\": \"05:00-08:00|12:00-23:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期四\",\n" +
                "\t\t\t\"id\": 4,\n" +
                "\t\t\t\"times\": \"05:00-08:00|12:00-23:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期五\",\n" +
                "\t\t\t\"id\": 5,\n" +
                "\t\t\t\"times\": \"05:00-08:00|12:00-23:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期六\",\n" +
                "\t\t\t\"id\": 6,\n" +
                "\t\t\t\"times\": \"05:00-08:00|12:00-23:00\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"dateStr\": \"星期日\",\n" +
                "\t\t\t\"id\": 7,\n" +
                "\t\t\t\"times\": \"05:00-08:00|12:00-23:00\"\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"resolution\": \"1376*800\",\n" +
                "\t\"sceneList\": [{\n" +
                "\t\t\"palyMusic\": true,\n" +
                "\t\t\"playTime\": 15,\n" +
                "\t\t\"html\": \"6b7a2959a4a5565f711e0906a59fa7c2.html\"\n" +
                "\t}, {\n" +
                "\t\t\"palyMusic\": true,\n" +
                "\t\t\"playTime\": 20,\n" +
                "\t\t\"html\": \"e4cccedc659e1d6d2883755952a62144.html\"\n" +
                "\t}],\n" +
                "\t\"sceneNum\": 2\n" +
                "}";
        progarmTest(manager, orgin2);
        // progarmTest(manager, orgin2);

       /* manager.setAllTaskListener(new DownloadManagerListener());*/
    }

    static List<ProgarmPalyInstructionVo> progarmPalyInstructionVos = new ArrayList<>();

    public static String TAG = "TaskProgarm";

    public static void saveToDB() {
        Log.e(TAG, "saveProgarmPalyInstructionVoRequest");
        //将数据保存到节目播放数据库，并通知节目播放，进行插入播放
        ProgramDbManager.getInstance().saveProgarmPalyInstructionVoRequest(response);
    }

    static ProgarmPalyInstructionVo response;

    public static void progarmTest(DownLoadManager manager, String org) {

        String orgin = org;

        response = JSON.parseObject(orgin, new TypeReference<ProgarmPalyInstructionVo>() {
        });

        //保存到节目数据中
        saveToDB();

        ProgramScheduledManager.getInstance().doProgarm(response, true);

       /* List<ProgarmPalyInstructionVo> list = ProgramDbManager.getInstance().getAllProgarmPalyInstructionVo();

        Log.e(TAG, list == null ? "null" : list.size() + "");

        String publicationPlanJson = response.getPublicationPlan();

        PublicationPlanVo publicationPlanVo = JSON.parseObject(publicationPlanJson, new TypeReference<PublicationPlanVo>() {
        });

        List<ProgramResource> programResourceList = response.getProgramResourceListArray();


        if (response.getTotalStatus() != 1) {
            progarmPalyInstructionVos.add(response);
            //判断预设目录下 是否有对应zip包
            *//*服务器一般会有个区分不同文件的唯一ID，用以处理文件重名的情况*//*
            String taskId;
            String url;
            url = response.getProgramZip();
            taskId = url;
            *//*将任务添加到下载队列，下载器会自动开始下载*//*
            //判断 Download 数据库是否是完成状态，是就检查文件是否存在  没有启动下载文件  下载成功则copy到预设目录
            int fileStatue = 1;
            if (response.getProgramZipStatus() != 1) {
                File newfile = new File(FileHelper.getFileDefaultPath() + "/" + response.getProgramZipName());
                if (!newfile.exists()) {
                    fileStatue = 0;
                    int download = manager.addPriorsTask(taskId, url, response.getProgramZipName());
                    if (download == 0) {
                        //下载成功则copy到预设目录
                        Log.e("sqlDownLoadInfo", "getProgramZipName:" + response.getProgramZipName() + "已经存在下载库里面了！");
                    } else if (download == 1) {
                        //需要下载
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

            for (int i = 0; i < programResourceList.size(); i++) {
                if (programResourceList.get(i).getDownStatus() != 1) {
                    String resourceurl = programResourceList.get(i).getUrl();
                    String taskResourceId;
                    taskResourceId = resourceurl;
                    String resourceurlFilename = programResourceList.get(i).getFileName();
                    String resourceurlFilenameVirPath = programResourceList.get(i).getVirtualPath();
                    *//*将任务添加到下载队列，下载器会自动开始下载*//*
                    //判断 Download 数据库是否是完成状态，是就检查文件是否存在  没有启动下载文件  下载成功则copy到预设目录
                    File newfile = new File(FileHelper.getFileDefaultPath() + "/" + resourceurlFilenameVirPath);
                    if (!newfile.exists()) {
                        fileStatue = 0;
                        int resourcedownload = manager.addPriorsTask(taskResourceId, resourceurl, resourceurlFilename, newfile.getPath());
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

            if (fileStatue == 1) {
                Log.e("sqlDownLoadInfo", "所有资源都存在：" + response.getId());
                response.setTotalStatus(1);


            }
        }

        System.out.println(response.toString());
        System.out.println(publicationPlanVo);*/
    }

    private static class DownloadManagerListener implements DownLoadListener {

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


            Iterator iterator = progarmPalyInstructionVos.iterator();

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

                if (response1.getProgramZipStatus() != 1) {
                    Log.e("sqlDownLoadInfo", "zip还没有下载成功：" + sqlDownLoadInfo.getTaskID());
                    resourceTotle = 0;
                }
                if (resourceTotle == 1) {
                    response1.setTotalStatus(1);
                    Log.e("sqlDownLoadInfo", "onSuccess所有资源都存在：" + response1.getId());
                    iterator.remove();

                    Event event = new Event();

                    event.setId(EventEnum.EVENT_TEST_MSG1);
                    EventBus.getDefault().post(event);


                }
            }
        }

        @Override
        public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
            //根据监听到的信息查找列表相对应的任务，停止该任务
            Log.e("sqlDownLoadInfo", "sqlDownLoadInfo11" + sqlDownLoadInfo.getTaskID());
            /*if (info.getTaskID().equals(sqlDownLoadInfo.getTaskID())) {

            }*/
        }
    }

}
