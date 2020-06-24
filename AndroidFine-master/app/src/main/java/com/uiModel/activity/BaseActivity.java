package com.uiModel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jf.fine.R;
import com.sgs.AppManager;
import com.sgs.middle.eventControlModel.Event;
import com.sgs.middle.eventControlModel.EventManager;

import org.greenrobot.eventbus.Subscribe;

public class BaseActivity extends FragmentActivity {

    private AlertDialog alertDialog;
    public void showLoadingDialog(String loadingtext) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.show();

        View view = LayoutInflater.from(this).inflate(R.layout.loading_alert, null);
        TextView tvTitle = view.findViewById(R.id.loadintext);
        tvTitle.setText(loadingtext);
        alertDialog.getWindow().setContentView(view);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);
        // 结束Activity从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        EventManager.register(this);
    }

    @Subscribe
    public void onEvent(Event mEvent) {
    }
}
