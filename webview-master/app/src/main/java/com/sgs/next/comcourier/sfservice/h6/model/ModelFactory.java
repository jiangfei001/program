package com.sgs.next.comcourier.sfservice.h6.model;

import android.content.ContentValues;

public interface ModelFactory<T> extends ModelBuilder<T> {
    ContentValues extractFromModel(T model);
}
