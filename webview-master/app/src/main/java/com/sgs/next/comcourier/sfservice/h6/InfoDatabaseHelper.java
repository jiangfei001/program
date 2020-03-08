package com.sgs.next.comcourier.sfservice.h6;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sgs.next.comcourier.sfservice.fourlevel.LogUtils;
import com.sgs.next.comcourier.sfservice.fourlevel.StringUtils;
import com.sgs.next.comcourier.sfservice.h6.domain.Location;
import com.sgs.next.comcourier.sfservice.h6.domain.LocationType;
import com.sgs.next.comcourier.sfservice.h6.domain.TableVersion;
import com.sgs.next.comcourier.sfservice.h6.query.SqlQuery;
import com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions;
import com.sgs.next.comcourier.sfservice.h6.utils.DeviceUtil;

import java.io.File;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS;
import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.CITY_8981_CODE;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.CITY_8981_NAME;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.CITY_898_CODE;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.CITY_898_NAME;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.LOCATION_MODEL_BUILDER;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.LOCATION_MODEL_PATCHER;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.LOCATION_NOT_COVERED;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_CITIES_BY_CODE_FUZZY;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_CITIES_BY_NAME_FUZZY;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_CITY_BY_8981_CODE;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_CITY_BY_898_CODE;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_CITY_BY_CODE_2;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_CITY_BY_CODE_AND_NAME;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_CITY_PRE_BY_ID;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_DISTRICT_BY_CODE_AND_NAME;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_LOCATIONS_BY_CITY_CODE;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_LOCATIONS_BY_PARENT_ID;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_LOCATIONS_BY_PARENT_ID_AND_TYPE_ORDER_BY_ID;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_LOCATION_BY_ID;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_MULTI_CITY_BY_898_8981_CODE;
import static com.sgs.next.comcourier.sfservice.h6.domain.Location.QUERY_LOAD_MULTI_CITY_BY_CODE;
import static com.sgs.next.comcourier.sfservice.h6.utils.Clock.formatToYMD;
import static com.sgs.next.comcourier.sfservice.h6.utils.Clock.now;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.loadList;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.loadOne;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.patchModel;
import static com.sgs.next.comcourier.sfservice.h6.utils.DeviceUtil.releaseServiceDatabaseFile;
import static java.lang.String.valueOf;

/**
 * 所有调用这个类的地方都必须放到子线程调用
 */
public class InfoDatabaseHelper {

    private static InfoDatabaseHelper instance;
    private File databaseFile;
    private SQLiteDatabase _database;

    public static synchronized InfoDatabaseHelper infoDatabaseHelper() {
        if (instance == null || instance.getDatabase() == null) {
            synchronized (InfoDatabaseHelper.class) {
                if (instance == null || instance.getDatabase() == null) {
                    instance = new InfoDatabaseHelper();
                }
            }
        }

        return instance;
    }

    protected InfoDatabaseHelper() {
        databaseFile = DeviceUtil.getServiceDatabaseFile();
        try {
            _database = SQLiteDatabase.openDatabase(databaseFile.getPath(), null, NO_LOCALIZED_COLLATORS | OPEN_READWRITE);
        } catch (Exception ex) {
            LogUtils.e("Info", ex);
            releaseServiceDatabaseFile();
            resetDatabase();
        }
        initDatabase();
    }

    public SQLiteDatabase getDatabase(File dbFile) {
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openDatabase(dbFile.getPath(), null, NO_LOCALIZED_COLLATORS | OPEN_READWRITE);
        } catch (Exception ex) {
            LogUtils.e("Info", ex);
        }
        return database;
    }

    private void initDatabase() {
        if (_database != null) {
            Cursor cursor = null;
            try {
                //查询表pd_tm_price_weight_1 是否有plan字段
                cursor = _database.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                        , new String[]{"pd_tm_price_weight_1", "%plan%"});
                if (cursor == null || !cursor.moveToFirst()) {
                    //为表添加plan字段
                    _database.execSQL(" alter table pd_tm_price_weight_1 add plan int DEFAULT -1 ");
                }
            } catch (Exception e) {
                LogUtils.w(e);
            } finally {
                if (null != cursor && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
    }

    private void resetDatabase() {
        if (_database == null)
            return;
        _database.close();
        _database = null;
    }

    public SQLiteDatabase getDatabase() {
        return _database;
    }

    public boolean isDatabaseExists() {
        return databaseFile.exists() && databaseFile.isFile() && _database != null;
    }

    public void close() {
        instance = null;
        releaseServiceDatabaseFile();
        resetDatabase();
    }

    public List<Location> loadCitiesByCodeOrNameFuzzy(String cityCode) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor;
        if (StringUtils.isNumber(cityCode)) {
            cursor = QUERY_LOAD_CITIES_BY_CODE_FUZZY.execute(database, cityCode + "%");
        } else {
            cursor = QUERY_LOAD_CITIES_BY_NAME_FUZZY.execute(database, "%" + cityCode + "%");
        }

        List<Location> locations = loadList(LOCATION_MODEL_BUILDER, cursor);

        for (Location location : locations) {
            patchLocation(database, location);
        }

        return locations;
    }

    public List<Location> loadMultiCityByCode(String cityCode) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor;
        if (CITY_898_CODE.equals(cityCode) || CITY_8981_CODE.equals(cityCode)) {
            cursor = QUERY_LOAD_MULTI_CITY_BY_898_8981_CODE.execute(database, cityCode + "%", CITY_898_NAME, CITY_8981_NAME);
        } else {
            cursor = QUERY_LOAD_MULTI_CITY_BY_CODE.execute(database, cityCode);
        }

        List<Location> locations = loadList(LOCATION_MODEL_BUILDER, cursor);

        for (Location location : locations) {
            patchLocation(database, location);
        }
        return locations;
    }


    public Location loadSingleCityByCode(String cityCode) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor;
        if (CITY_898_CODE.equals(cityCode)) {
            cursor = QUERY_LOAD_CITY_BY_898_CODE.execute(database, cityCode, CITY_898_NAME);
        } else if (CITY_8981_CODE.equals(cityCode)) {
            cursor = QUERY_LOAD_CITY_BY_8981_CODE.execute(database, cityCode, CITY_8981_NAME);
        } else {
            cursor = QUERY_LOAD_CITY_BY_CODE_2.execute(database, cityCode, LocationType.PROVINCE.ordinal());
        }

        Location location = loadOne(LOCATION_MODEL_BUILDER, cursor, LOCATION_NOT_COVERED);

        patchLocation(database, location);
        return location;
    }

    public Location loadSingleCityByCodeAndName(String cityCode, String cityName) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = QUERY_LOAD_CITY_BY_CODE_AND_NAME.execute(database, cityCode, "%" + cityName + "%");
        Location location = loadOne(LOCATION_MODEL_BUILDER, cursor, Location.EMPTY);
        patchLocation(database, location);
        return location;
    }

    public Location loadSingleDistrictByCodeAndName(String cityCode, String districtName) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = QUERY_LOAD_DISTRICT_BY_CODE_AND_NAME.execute(database, cityCode, "%" + districtName + "%");
        Location location = loadOne(LOCATION_MODEL_BUILDER, cursor, Location.EMPTY);
        patchLocation(database, location);
        return location;
    }


    private void patchLocation(SQLiteDatabase database, Location location) {
        Cursor preCursor = QUERY_LOAD_CITY_PRE_BY_ID.execute(database, location.getId(), formatToYMD(now()));
        patchModel(preCursor, location, LOCATION_MODEL_PATCHER);
    }

    public List<Location> loadLocationsByParentId(long parentId) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = QUERY_LOAD_LOCATIONS_BY_PARENT_ID.execute(database, valueOf(parentId));
        return loadList(LOCATION_MODEL_BUILDER, cursor);
    }


    public List<Location> loadLocationsByParentIdAndTypeOrderById(long parentId, int type) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = QUERY_LOAD_LOCATIONS_BY_PARENT_ID_AND_TYPE_ORDER_BY_ID.execute(database, valueOf(parentId), type);
        return loadList(LOCATION_MODEL_BUILDER, cursor);
    }

    public Location loadLocationById(long id) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = QUERY_LOAD_LOCATION_BY_ID.execute(database, valueOf(id));
        return loadOne(LOCATION_MODEL_BUILDER, cursor);
    }

    public List<Location> loadLocationsByCityCode(String cityCode, int type) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = QUERY_LOAD_LOCATIONS_BY_CITY_CODE.execute(database, cityCode, type);

        return loadList(LOCATION_MODEL_BUILDER, cursor);
    }


    public Location loadLocationsByParentIdAndName(long parentId, String name) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = Location.QUERY_LOAD_LOCATIONS_BY_PARENT_ID_AND_NAME.execute(database, valueOf(parentId), "%" + name + "%");
        return loadOne(LOCATION_MODEL_BUILDER, cursor, Location.EMPTY);
    }

    public List<Location> loadRegionLocationByParentId(long parentId) {
        Cursor cursor = Location.QUERY_REGION_LOCATIONS_BY_PARENT_ID.execute(getDatabase(), parentId);
        return loadList(LOCATION_MODEL_BUILDER, cursor);
    }

    public List<Location> loadRegionLocationByCityCode(String cityCode) {
        Cursor cursor = Location.QUERY_REGION_LOCATIONS_BY_CITY_CODE.execute(getDatabase(), cityCode);
        return loadList(LOCATION_MODEL_BUILDER, cursor);
    }

    public Location loadCountryLocationByCityCode(String cityCode) {
        Cursor cursor = Location.QUERY_COUNTRY_LOCATION_BY_CITY_CODE.execute(getDatabase(), cityCode);
        return loadOne(LOCATION_MODEL_BUILDER, cursor, Location.EMPTY);
    }

    public Location loadProvinceLocationByCityCode(String cityCode) {
        Cursor cursor = Location.QUERY_PROVINCE_LOCATION_BY_CITY_CODE.execute(getDatabase(), cityCode);
        return loadOne(LOCATION_MODEL_BUILDER, cursor, Location.EMPTY);
    }

//    public Location loadProvinceLocationByCityCode(String cityCode) {
//        Cursor cursor = Location.QUERY_PROVINCE_LOCATION_BY_CITY_CODE.execute(getDatabase(), cityCode);
//        return loadOne(LOCATION_MODEL_BUILDER, cursor, Location.EMPTY);
//    }
//
//    public Location loadLocationsByCityCodeAndName(String cityCode, String name) {
//        SQLiteDatabase database = getDatabase();
//        Cursor cursor = Location.QUERY_LOAD_LOCATIONS_BY_CITY_CODE_AND_NAME.execute(database, cityCode, "%" + name + "%");
//        return loadOne(LOCATION_MODEL_BUILDER, cursor, Location.EMPTY);
//    }


////    public boolean isUseNewCashierAccount() {
//        String nowTime = formatToYMD(now());
//        boolean isUseNewCashier = false;
//        try {
//            Cursor cursor = QUERY_IS_USE_NEW_CASHIER.execute(getDatabase(), UserInfoUtils.getUsername(), nowTime);
//            if (cursor != null) {
//                int count = DatabaseActions.loadCount(cursor);
//                isUseNewCashier = count > 0;
//            }
//        } catch (Exception e) {
//            ComCourierLogUtils.e(e);
//            isUseNewCashier = false;
//        }
//        return isUseNewCashier;
//    }

//    public List<MatrixProductFlow> loadMatrixProductFlow(String districtId, String workDay) {
//        try {
//            String zoneCode = covertHeiKeCode(UserInfoUtils.getOriginDeptCode());
//            Cursor cursor = QUERY_MATRIX_PRODUCT_FLOW.execute(getDatabase(), zoneCode, districtId, workDay);
//            return DatabaseActions.loadList(MATRIX_PRODUCT_FLOW_MODEL_BUILDER, cursor);
//        } catch (Exception e) {
//            ComCourierLogUtils.e("loadMatrixProductFlow", e);
//            return new ArrayList<MatrixProductFlow>();
//        }
//    }


    public boolean updateTableVersion(String tableName, String areaCode, int fileVersion, String fileMd5) {
        SQLiteDatabase database = getDatabase();
        ContentValues values = new ContentValues();
        values.put(TableVersion.TABLE_NAME, tableName);
        values.put(TableVersion.AREA_CODE, areaCode);
        values.put(TableVersion.FILE_VERSION, fileVersion);
        values.put(TableVersion.FILE_MD5, fileMd5);

        boolean success = DatabaseActions.update(database, TableVersion.PD_TABLE_VERSION, values, TableVersion.TABLE_NAME + "=?", tableName);
        if (!success) {
            success = database.insert(TableVersion.PD_TABLE_VERSION, null, values) > 0;
        }
        return success;
    }

    /**
     * 判断表中是否含有某字段
     *
     * @param tableName
     * @param column
     * @return
     */
    public boolean isColumnExist(String tableName, String column) {
        String sql = "select * from " + tableName + " where 1=2";
        Cursor cursor = new SqlQuery(sql).execute(getDatabase());
        if (cursor != null && !TextUtils.isEmpty(column)) {
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; ++i) {
                String columnName = cursor.getColumnName(i);
                if (column.equals(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {
        Gson gson = new Gson();
        H5UserInfo h5DataBean = new H5UserInfo("120929", "asdf", "123123123", "755A", "755", "jiang");

        System.out.println(gson.toJson(new H5DataBean<H5UserInfo>(true, h5DataBean)));


        H5TokenBean h5DataBean1 = new H5TokenBean("1gasgasdf3asdf");

        System.out.println(gson.toJson(new H5DataBean<H5TokenBean>(true, h5DataBean1)));
    }

    public static class H5TokenBean {
        private String token;

        public H5TokenBean(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "H5TokenBean{" +
                    "token='" + token + '\'' +
                    '}';
        }
    }


    public static class H5UserInfo {
        private String id;//用户编号(userId,对应res_user表里的id,对应u_user表里的resourceId)
        private String userName;//工号
        private String mobile;//手机号
        private String netCode;//网点代码
        private String areaCode;//大区代码
        private String fullName;//客户名

        public H5UserInfo(String id, String userName, String mobile, String netCode, String areaCode, String fullName) {
            this.id = id;
            this.userName = userName;
            this.mobile = mobile;
            this.netCode = netCode;
            this.areaCode = areaCode;
            this.fullName = fullName;
        }

        @Override
        public String toString() {
            return "H5UserInfo{" +
                    "id='" + id + '\'' +
                    ", userName='" + userName + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", netCode='" + netCode + '\'' +
                    ", areaCode='" + areaCode + '\'' +
                    ", fullName='" + fullName + '\'' +
                    '}';
        }
    }

    public static class H5DataBean<T> {

        private boolean success;

        private T data;

        public H5DataBean(boolean success, T data) {
            this.data = data;
            this.success = success;
        }

        @Override
        public String toString() {
            return "H5DataBean{" +
                    "success=" + success +
                    ", Data=" + data +
                    '}';
        }
    }


}
