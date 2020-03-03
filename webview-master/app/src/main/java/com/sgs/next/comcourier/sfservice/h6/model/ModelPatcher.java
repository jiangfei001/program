package com.sgs.next.comcourier.sfservice.h6.model;

import android.database.Cursor;

public interface ModelPatcher<T> {
    void patchObject(Cursor cursor, T model);
}
