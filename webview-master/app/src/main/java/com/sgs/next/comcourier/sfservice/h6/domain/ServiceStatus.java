package com.sgs.next.comcourier.sfservice.h6.domain;


import com.example.app.R;
import com.sgs.next.comcourier.sfservice.fourlevel.ResUtil;

import static android.graphics.Color.parseColor;

public enum ServiceStatus {
    NOT_COVERED(ResUtil.getString(R.string.takex_service_status_not_covered), parseColor("#EC2223"), R.color.transparent),
    FULL(ResUtil.getString(R.string.takex_service_status_full), parseColor("#000000"), R.mipmap.icon_takex_service_status_full),
    PARTIAL(ResUtil.getString(R.string.takex_service_status_partial), parseColor("#ff5700"), R.mipmap.icon_takex_service_status_partial),
    SELF_SERVICE(ResUtil.getString(R.string.takex_service_status_self_service), parseColor("#009fd5"), R.mipmap.icon_takex_service_status_self_service),
    NO_DATA("", parseColor("#000000"), R.color.transparent);

    public final String status;
    public final int color;
    public final int statusImage;


    ServiceStatus(String status, int color, int statusImage) {
        this.status = status;
        this.color = color;
        this.statusImage = statusImage;
    }
}