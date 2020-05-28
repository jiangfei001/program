package com.uiModel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.jf.fine.R;
import com.sgs.AppManager;

public class BaseActivity extends FragmentActivity {

    private AlertDialog alertDialog;

    public void showLoadingDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
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
        // 结束Activity从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

}
