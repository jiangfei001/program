package com.sgs.taskModel;

// 做一件打印自己的id的事。
public class PrintTask extends BasicTask {

    private int id;

    public PrintTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        // 为了尽量模拟窗口办事的速度，我们这里停顿两秒。
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        System.out.println("我的id是：" + id);
    }
}