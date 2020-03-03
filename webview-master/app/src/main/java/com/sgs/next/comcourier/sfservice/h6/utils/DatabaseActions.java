package com.sgs.next.comcourier.sfservice.h6.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sgs.next.comcourier.sfservice.h6.model.ModelBuilder;
import com.sgs.next.comcourier.sfservice.h6.model.ModelFactory;
import com.sgs.next.comcourier.sfservice.h6.model.ModelPatcher;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActions {
    private DatabaseActions() {
    }


    public static <T> int insertOne(SQLiteDatabase database, String tableName, ModelFactory<T> modelFactory, T tInstance) {
        try {
            database.insert(tableName, null, modelFactory.extractFromModel(tInstance));
        } catch (Exception ex) {

            return 0;
        }
        return 1;
    }

    public static <T> int insert(SQLiteDatabase database, String tableName, ModelFactory<T> modelFactory, List<T> list) {
        database.beginTransaction();

        try {
            for (T tInstance : list) {
                database.insert(tableName, null, modelFactory.extractFromModel(tInstance));
            }

            database.setTransactionSuccessful();

            return list.size();
        } catch (Exception ex) {

            return 0;
        } finally {
            database.endTransaction();
        }
    }

    public static int delete(SQLiteDatabase database, String table, String whereClause, String... args) {
        int rows = 0;
        try {
            rows = database.delete(table, whereClause, args);
        } catch (Exception e) {

        }
        return rows;
    }

    public static int delete(SQLiteDatabase database, String table) {
        int rows = 0;
        try {
            rows = database.delete(table, "1=1", null);
        } catch (Exception e) {

        }
        return rows;
    }

    public static boolean update(SQLiteDatabase database, String tableName, ContentValues values, String whereClause, String... whereArgs) {
        boolean succeeded = false;
        try {
            succeeded = database.update(tableName, values, whereClause, whereArgs) > 0;
        } catch (Exception e) {

        }
        return succeeded;
    }

    public static <T> List<T> loadList(ModelBuilder<T> modelBuilder, Cursor cursor) {
        ArrayList<T> result = new ArrayList<T>();

        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    result.add(modelBuilder.buildModel(cursor));
                }
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }

    public static <T> T loadOne(ModelBuilder<T> modelBuilder, Cursor cursor) {
        return loadOne(modelBuilder, cursor, null);
    }

    public static <T> T loadOne(ModelBuilder<T> modelBuilder, Cursor cursor, T defaultValue) {
        T result = defaultValue;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = modelBuilder.buildModel(cursor);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }

    public static int loadIntScalar(Cursor cursor) {
        int result = 0;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }

    public static String loadString(Cursor cursor) {
        String result = "";

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(0);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }

    public static int loadCount(Cursor cursor) {
        int count = 0;

        try {
            if (cursor != null) {
                count = cursor.getCount();
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return count;
    }

    public static long loadLongScalar(Cursor cursor) {
        long result = 0;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getLong(0);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }

    public static <T> void patchModel(Cursor cursor, T model, ModelPatcher<T> patcher) {
        try {
            if (cursor != null && cursor.moveToFirst()) {
                patcher.patchObject(cursor, model);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public static boolean loadBooleanWithDefault(Cursor cursor, boolean defaultValue) {
        int result = defaultValue ? 1 : 0;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result == 1;
    }

    public static int readCount(Cursor cursor) {
        int count = 0;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return count;
    }

    public static String readCursorString(Cursor cursor, String column) {
        String result = "";
        try {
            result = cursor.getString(getColumnIndex(cursor, column));
        } catch (Exception e) {
        }
        return result;
    }


    public static String readCursorString(Cursor cursor, String column, String defaultValue) {
        String value;
        int cityCodeIndex = getColumnIndex(cursor, column);
        if (cityCodeIndex != -1) {
            value = cursor.getString(cityCodeIndex);
        } else {
            value = defaultValue;
        }
        return value;
    }

    public static int readCursorInt(Cursor cursor, String column) {
        int result = 0;
        try {
            result = cursor.getInt(getColumnIndex(cursor, column));
        } catch (Exception e) {
        }
        return result;
    }

    public static long readCursorLong(Cursor cursor, String column) {
        long result = 0;
        try {
            result = cursor.getLong(getColumnIndex(cursor, column));
        } catch (Exception e) {
         Log.e("eException","eException"+e.getMessage());
        }
        return result;
    }

    public static long readCursorBold(Cursor cursor, String column) {
        long result = 0;
        try {
            byte[] b = cursor.getBlob(getColumnIndex(cursor, column));;
            /* Log.e("getBlob",b.toString());*/
            String code = new String(b);//这个却没有报错
            result=Long.parseLong(code);
        } catch (Exception e) {
            Log.e("eException","eException"+e.getMessage());
        }
        return result;
    }

    public static float readCursorFloat(Cursor cursor, String column) {
        float result = 0;
        try {
            result = cursor.getFloat(getColumnIndex(cursor, column));
        } catch (Exception e) {

        }
        return result;
    }

    public static double readCursorDouble(Cursor cursor, String column) {
        double result = 0;
        try {
            result = cursor.getDouble(getColumnIndex(cursor, column));
        } catch (Exception e) {

        }
        return result;
    }

    public static <T extends Enum<T>> T readCursorEnum(Cursor cursor, Class<T> enumClass, String column) {
        T t = null;
        try {
            t = enumClass.getEnumConstants()[cursor.getInt(getColumnIndex(cursor, column))];
        } catch (Exception e) {

        }
        return t;
    }

    public static boolean readCursorBoolean(Cursor cursor, String column) {
        boolean result = false;
        try {
            result = cursor.getInt(getColumnIndex(cursor, column)) == 1;
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * @see android.database.AbstractCursor sdk16
     */
    public static int getColumnIndex(Cursor cursor, String columnName) {
        try {
            final int periodIndex = columnName.lastIndexOf('.');
            if (periodIndex != -1) {
                columnName = columnName.substring(periodIndex + 1);
            }

            String[] columnNames = cursor.getColumnNames();
            int length = columnNames.length;
            for (int i = 0; i < length; i++) {
                if (columnNames[i].equalsIgnoreCase(columnName)) {
                    return i;
                }
            }
        } catch (Exception e) {

        }
        return -1;
    }

    public static double loadDouble(Cursor cursor) {
        double result = 0;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getDouble(0);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null)
                cursor.close();
        }

        return result;
    }
}
