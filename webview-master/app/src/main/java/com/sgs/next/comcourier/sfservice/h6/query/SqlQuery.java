package com.sgs.next.comcourier.sfservice.h6.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.sgs.next.comcourier.sfservice.h6.utils.SqlHelper.toQueryValue;

public class SqlQuery {
    private final String sql;

    public SqlQuery(String sql) {
        this.sql = sql;
    }

    public Cursor execute(SQLiteDatabase database, String... args) {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, args);
        } catch (Exception e) {
        }
        return cursor;
    }

    public Cursor execute(SQLiteDatabase database, Object... args) {
        Cursor cursor = null;
        try {
            String[] arg = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                arg[i] = toQueryValue(args[i]);
            }

            cursor = database.rawQuery(sql, arg);
        } catch (Exception e) {
        }
        return cursor;
    }

    @Override
    public String toString() {
        return sql;
    }
}
