package com.dalong.androidmarqueeview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dalong.marqueeview.HorizontalMarqueeView;
import com.dalong.marqueeview.MarqueeView;

public class MainActivity extends AppCompatActivity {

    private MarqueeView mMarqueeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMarqueeView = (MarqueeView) findViewById(R.id.mMarqueeView);
        mMarqueeView.setSizeAndColor("122","#00CCFF");
        mMarqueeView.setText("依据赫兹接触强度计算理论，着重研究了圆柱滚子轴承内、外圈及滚动体的接触应力");

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarqueeView.startScroll();
                mMarqueeView.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
                    @Override
                    public void onRollOver() {
                        Log.e("onRollOver","onRollOver");
                        Toast.makeText(MainActivity.this, "滚动完毕", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarqueeView.stopScroll();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
