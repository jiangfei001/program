package com.sgs.next.comcourier.sfservice.h6.model;

import android.database.Cursor;

public interface ModelBuilder<T> {
    T buildModel(Cursor cursor);
}
