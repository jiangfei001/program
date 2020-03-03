package com.sgs.next.comcourier.sfservice.h6.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sgs.next.comcourier.sfservice.fourlevel.LogUtils;
import com.sgs.next.comcourier.sfservice.fourlevel.StringUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

public class SqlHelper {
    private SqlHelper() {
    }

    public static final String TYPE_ID = "INTEGER PRIMARY KEY";
    public static final String TYPE_TEXT_ID = "TEXT PRIMARY KEY";
    public static final String TYPE_TEXT_UNIQUE = "TEXT UNIQUE";
    public static final String TYPE_FOREIGN_KEY = "INTEGER NOT NULL";
    public static final String TYPE_ENUMERATION = "INTEGER NOT NULL";
    public static final String TYPE_DATETIME = "INTEGER NOT NULL";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_REAL = "REAL";
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_TEXT_LIMIT_NOT_NULL = "TEXT NOT NULL";
    public static final String LIMIT_PRIMARY_KEY = "PRIMARY KEY";
    public static final String LIMIT_DEFAULT_NULL = "DEFAULT NULL";
    public static final String LIMIT_NOT_NULL = "NOT NULL";
    public static final String LIMIT_DEFAULT_0_NOT_NULL = "DEFAULT 0 NOT NULL";
    public static final String LIMIT_INTEGER_DEFAULT_0_NOT_NULL = "INTEGER DEFAULT 0 NOT NULL";
    public static final String TYPE_BOOL = "BOOL";
    public static final String TYPE_AUTO_INCREMENT_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_BOOL_DEFAULT_0 = "BOOL DEFAULT 0";
    public static final String LIMIT_DEFAULT_0 = "DEFAULT 0";
    public static final String TYPE_DATETIME_DEFAULT_0 = "INTEGER DEFAULT 0";

    public static final String LOGGER_TAG = "SQL";

    private static final List<ValueTranslator> TRANSLATORS = new ArrayList<ValueTranslator>();


    static {
        TRANSLATORS.add(new ValueTranslator() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return value == null;
            }

            @Override
            public String translateValue(Object value) {
                return "NULL";
            }
        });

        TRANSLATORS.add(new StringValueTranslator<String>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return CharSequence.class.isAssignableFrom(type);
            }

            @Override
            public String translateValue(String value) {
                return value;
            }
        });

        TRANSLATORS.add(new ValueTranslator<Enum>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return Enum.class.isAssignableFrom(type);
            }

            @Override
            public Integer translateValue(Enum value) {
                return value.ordinal();
            }
        });

        TRANSLATORS.add(new ValueTranslator<Date>() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return Date.class.isAssignableFrom(type);
            }

            @Override
            public Long translateValue(Date value) {
                return value.getTime();
            }
        });

        TRANSLATORS.add(new ValueTranslator() {
            @Override
            public boolean isApplicable(Class type, Object value) {
                return true;
            }

            @Override
            public Object translateValue(Object value) {
                return value;
            }
        });
    }

    public static void createTable(SQLiteDatabase database, String tableName, String... columns) {
        String columnsDefinition = StringUtils.join(Arrays.asList(columns), ",");
        String sql = format("CREATE TABLE IF NOT EXISTS %s ( %s );", tableName, columnsDefinition);

        Log.d(LOGGER_TAG, sql);
        database.execSQL(sql);
    }

    public static String columnDef(String columnName, String columnDeclaration) {
        return format("%s %s", columnName, columnDeclaration);
    }

    public static String columnDef(String... columnDeclarations) {
        return StringUtils.join(Arrays.asList(columnDeclarations), " ");
    }

    public static void alterTableAddColumn(SQLiteDatabase database, String tableName, String column, String typeAndDefaultValue) {
        database.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + column + " " + typeAndDefaultValue);
    }

    public static boolean checkColumnExist(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            LogUtils.d("checkColumnExists..." + e);
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public static boolean checkTableExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT count(*) FROM sqlite_master where type='table' AND name='" + tableName + "'"
                    , null);
            cursor.moveToFirst();
            result = cursor.getInt(0) > 0;
        } catch (Exception e) {
            LogUtils.d("checkTableExist..." + e);
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    private static ValueTranslator findValueTranslator(final Object value) {
        final Class type = value == null ? null : value.getClass();
        for (ValueTranslator valueTranslator : TRANSLATORS) {
            if (valueTranslator.isApplicable(type, value)) {
                return valueTranslator;
            }
        }
        return null;
    }

    public static String toQueryValue(Object value) {
        return findValueTranslator(value).toQueryValue(value);
    }

    public static String toSqlValue(Object value) {
        return findValueTranslator(value).toSqlValue(value);
    }

    public abstract static class ValueTranslator<T> {
        public abstract boolean isApplicable(Class type, Object value);

        protected abstract Object translateValue(T value);

        public String toQueryValue(Object value) {
            //noinspection unchecked
            return translateValue((T) value).toString();
        }

        public String toSqlValue(Object value) {
            return toQueryValue(value);
        }
    }

    public abstract static class StringValueTranslator<T> extends ValueTranslator<T> {
        @Override
        public String toSqlValue(Object value) {
            return "\"" + super.toQueryValue(value) + "\"";
        }
    }


    public static void insertIntoTable(SQLiteDatabase database, String tableName, Object... values) {
        String insertSql = "INSERT INTO %s VALUES (%s);";

        List<String> valueStrings = new ArrayList<String>();

        for (Object value : values) {
            String stringValue = toSqlValue(value);
            valueStrings.add(stringValue);
        }
        String valuesSql = StringUtils.join(valueStrings, ",");

        String sql = format(insertSql, tableName, valuesSql);

        database.execSQL(sql);
    }

}

