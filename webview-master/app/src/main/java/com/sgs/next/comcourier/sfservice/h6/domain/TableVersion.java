package com.sgs.next.comcourier.sfservice.h6.domain;

import android.database.Cursor;

import com.sgs.next.comcourier.sfservice.h6.model.ModelBuilder;
import java.io.Serializable;

import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.readCursorInt;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.readCursorString;

/**
 * Created by hewenyu on 2018/5/16.
 */
public class TableVersion implements Serializable {

    public static final String PD_TABLE_VERSION = "pd_table_version";
    public static final String TABLE_NAME = "table_name";
    public static final String AREA_CODE = "area_code";
    public static final String FILE_VERSION = "file_version";
    public static final String FILE_MD5 = "file_md5";

    public static final String QUERY_ALL_TABLE_VERSION = "select * from pd_table_version";

    public static TableVersion tableVersionEmpty = new TableVersion();

    public String tableName;
    public String areaCode;
    public int fileVersion;
    public String fileMd5;

    public TableVersion() {
    }

    public TableVersion(String tableName, String areaCode, int fileVersion, String fileMd5) {
        this.tableName = tableName;
        this.areaCode = areaCode;
        this.fileVersion = fileVersion;
        this.fileMd5 = fileMd5;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(int fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public static ModelBuilder<TableVersion> TABLE_VERSION_MODEL_BUILDER = new ModelBuilder<TableVersion>() {
        @Override
        public TableVersion buildModel(Cursor cursor) {
            TableVersion tableVersion = new TableVersion();
            tableVersion.setTableName(readCursorString(cursor, TABLE_NAME));
            tableVersion.setAreaCode(readCursorString(cursor, AREA_CODE));
            tableVersion.setFileVersion(readCursorInt(cursor, FILE_VERSION));
            tableVersion.setFileMd5(readCursorString(cursor, FILE_MD5));
            return tableVersion;
        }
    };
}
