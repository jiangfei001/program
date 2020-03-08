package com.sgs.next.comcourier.sfservice.h6.domain;

import android.database.Cursor;

import com.example.app.R;
import com.sgs.next.comcourier.sfservice.fourlevel.ResUtil;
import com.sgs.next.comcourier.sfservice.fourlevel.StringUtils;
import com.sgs.next.comcourier.sfservice.h6.model.ModelBuilder;
import com.sgs.next.comcourier.sfservice.h6.model.ModelPatcher;
import com.sgs.next.comcourier.sfservice.h6.query.SqlQuery;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.sgs.next.comcourier.sfservice.fourlevel.FourlevelAddressUtil.CITY;
import static com.sgs.next.comcourier.sfservice.h6.domain.LocationType.COUNTY;
import static com.sgs.next.comcourier.sfservice.h6.domain.ServiceStatus.NOT_COVERED;
import static com.sgs.next.comcourier.sfservice.h6.domain.ServiceStatus.NO_DATA;
import static com.sgs.next.comcourier.sfservice.h6.query.QueryStatement.ASCENDING;
import static com.sgs.next.comcourier.sfservice.h6.query.QueryStatement.DESCENDING;
import static com.sgs.next.comcourier.sfservice.h6.query.QueryStatement.select;
import static com.sgs.next.comcourier.sfservice.h6.query.SqlColumn.ALL_COLUMNS;
import static com.sgs.next.comcourier.sfservice.h6.query.SqlColumn.column;
import static com.sgs.next.comcourier.sfservice.h6.query.SqlExpression.LIKE;
import static com.sgs.next.comcourier.sfservice.h6.query.SqlExpression.QUERY_ARG;
import static com.sgs.next.comcourier.sfservice.h6.query.SqlExpression.equal;
import static com.sgs.next.comcourier.sfservice.h6.query.SqlExpression.expression;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.readCursorBold;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.readCursorEnum;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.readCursorLong;
import static com.sgs.next.comcourier.sfservice.h6.utils.DatabaseActions.readCursorString;

public class Location implements Serializable {
    public static final String TABLE_LOCATIONS = "pd_service_range_cur";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_CODE = "city_code";
    public static final String COL_TYPE = "type";
    public static final String COL_PARENT = "parent";

    public static final String COL_STATUS_COLLECTION = "pick_up";
    public static final String COL_STATUS_DISPATCH = "dispatch";
    public static final String COL_SERVICE_POINTS = "dept_code";

    public static final String TABLE_LOCATIONS_PRE = "pd_service_range_pre";
    public static final String COL_VALIDATE_TIME = "validate_tm";

    public static final String CITY_898_CODE = "898";
    public static final String CITY_898_NAME = "海口%";
    public static final String CITY_8981_CODE = "8981";
    public static final String CITY_8981_NAME = "三亚%";
    public static final String CITY_CODE_CHINA = "CN";
    public static final String CITY_CODE_HK = "852";
    public static final String CITY_CODE_MC = "853";
    public static final String CITY_CODE_TW = "886";
    public static final String CITY_TW = "澳门";

    public static final SqlQuery QUERY_LOAD_CITY_BY_CODE = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, CITY))
            .orderBy(COL_TYPE, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_CITY_BY_CODE_2 = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, QUERY_ARG))
            .orderBy(COL_TYPE, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_CITY_BY_CODE_AND_NAME = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(expression(COL_NAME, LIKE, QUERY_ARG))
            .and(equal(COL_TYPE, CITY))
            .orderBy(COL_TYPE, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_DISTRICT_BY_CODE_AND_NAME = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(expression(COL_NAME, LIKE, QUERY_ARG))
            .and(equal(COL_TYPE, COUNTY))
            .orderBy(COL_TYPE, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_MULTI_CITY_BY_CODE = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(expression(COL_CODE, LIKE, QUERY_ARG))
            .and(equal(COL_TYPE, CITY))
            .orderBy(COL_TYPE, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_MULTI_CITY_BY_898_8981_CODE = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(expression(COL_CODE, LIKE, QUERY_ARG))
            .and(equal(COL_TYPE, CITY))
            .and(expression(COL_NAME, LIKE, QUERY_ARG)
                    .or(expression(COL_NAME, LIKE, QUERY_ARG)))
            .toQuery();

    public static final SqlQuery QUERY_LOAD_CITY_BY_898_CODE = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, CITY))
            .and(expression(COL_NAME, LIKE, QUERY_ARG))
            .toQuery();

    public static final SqlQuery QUERY_LOAD_CITY_BY_8981_CODE = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, CITY))
            .and(expression(COL_NAME, LIKE, QUERY_ARG))
            .toQuery();

    public static final SqlQuery QUERY_LOAD_CITIES_BY_CODE_FUZZY = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(expression(COL_CODE, LIKE, QUERY_ARG))
            .and(equal(COL_TYPE, LocationType.CITY.ordinal()))
            .orderBy(COL_TYPE, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_CITIES_BY_NAME_FUZZY = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(expression(COL_NAME, LIKE, QUERY_ARG))
            .and(equal(COL_TYPE, LocationType.CITY.ordinal()))
            .toQuery();

    public static final SqlQuery QUERY_LOAD_LOCATIONS_BY_PARENT_ID = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_PARENT, QUERY_ARG))
            .orderBy(COL_STATUS_DISPATCH, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_LOCATIONS_BY_CITY_CODE = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, QUERY_ARG))
            .orderBy(COL_STATUS_DISPATCH, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_LOCATIONS_BY_PARENT_ID_ORDER_BY_ID = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_PARENT, QUERY_ARG))
            .orderBy(COL_ID, ASCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_LOCATIONS_BY_PARENT_ID_AND_TYPE_ORDER_BY_ID = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_PARENT, QUERY_ARG))
            .and(equal(COL_TYPE, QUERY_ARG))
            .orderBy(COL_ID, ASCENDING)
            .toQuery();

    public static final SqlQuery QUERY_LOAD_LOCATION_BY_ID = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS)
            .where(equal(COL_ID, QUERY_ARG))
            .toQuery();

    public static final SqlQuery QUERY_LOAD_CITY_PRE_BY_ID = select(column(ALL_COLUMNS))
            .from(TABLE_LOCATIONS_PRE)
            .where(equal(COL_ID, QUERY_ARG))
            .and(expression(COL_VALIDATE_TIME, "<=", QUERY_ARG))
            .toQuery();

    public static final SqlQuery QUERY_LOAD_LOCATIONS_BY_PARENT_ID_AND_NAME = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_PARENT, QUERY_ARG))
            .and(expression(COL_NAME, LIKE, QUERY_ARG))
            .orderBy(COL_STATUS_DISPATCH, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_REGION_LOCATIONS_BY_PARENT_ID = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_PARENT, QUERY_ARG))
            .and(equal(COL_TYPE, LocationType.COUNTY))
            .toQuery();

    public static final SqlQuery QUERY_REGION_LOCATIONS_BY_CITY_CODE = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, LocationType.COUNTY))
            .toQuery();

    public static final SqlQuery QUERY_LOAD_LOCATIONS_BY_CITY_CODE_AND_NAME = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_PARENT, QUERY_ARG))
            .and(equal(COL_TYPE, QUERY_ARG))
            .orderBy(COL_STATUS_DISPATCH, DESCENDING)
            .toQuery();

    public static final SqlQuery QUERY_COUNTRY_LOCATION_BY_CITY_CODE = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, LocationType.COUNTRY))
            .toQuery();

    public static final SqlQuery QUERY_PROVINCE_LOCATION_BY_CITY_CODE = select(ALL_COLUMNS)
            .from(TABLE_LOCATIONS)
            .where(equal(COL_CODE, QUERY_ARG))
            .and(equal(COL_TYPE, LocationType.PROVINCE))
            .toQuery();


    public static final ModelPatcher<Location> LOCATION_MODEL_PATCHER = new ModelPatcher<Location>() {
        @Override
        public void patchObject(Cursor cursor, Location model) {
            model.setServiceStatus(readCursorEnum(cursor, ServiceStatus.class, COL_STATUS_DISPATCH));
        }
    };

    public static final ModelBuilder<Location> LOCATION_MODEL_BUILDER = new ModelBuilder<Location>() {
        @Override
        public Location buildModel(Cursor cursor) {
          //long id1 = readCursorLong(cursor, COL_ID);
            long id1 = readCursorBold(cursor, COL_ID);
            String name1 = readCursorString(cursor, COL_NAME);
            LocationType type1 = readCursorEnum(cursor, LocationType.class, COL_TYPE);
            String cityCode1 = readCursorString(cursor, COL_CODE, null);

            ServiceStatus status = getServiceStatus(cursor);

            Location location = new Location(id1, name1, type1, cityCode1, status);
            location.setDeptCode(readCursorString(cursor, COL_SERVICE_POINTS));
            location.setParent(readCursorLong(cursor, COL_PARENT));
            return location;
        }

        private ServiceStatus getServiceStatus(Cursor cursor) {
            String dispatchStatus = readCursorString(cursor, COL_STATUS_DISPATCH);
            if (StringUtils.isBlank(dispatchStatus)) {
                return NOT_COVERED;
            }
            return readCursorEnum(cursor, ServiceStatus.class, COL_STATUS_DISPATCH);
        }
    };

    public static final Location EMPTY = new LocationEmpty(NO_DATA);
    public static final Location LOCATION_NOT_COVERED = new LocationEmpty(ResUtil.getString(R.string.takex_no_match_city), NOT_COVERED);

    private long id;
    private String name;
    private LocationType type;
    private long parent;
    private String cityCode;
    private ServiceStatus serviceStatus;
    private String deptCode;

    public Location(long id, String name, LocationType type, String cityCode, ServiceStatus status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cityCode = cityCode;
        this.serviceStatus = status;
    }

    public Location(long id, String name, LocationType type, long parent, String cityCode, ServiceStatus serviceStatus) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.cityCode = cityCode;
        this.serviceStatus = serviceStatus;
    }

    public Location() {
        //Do nothing
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public LocationType getType() {
        return type;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public static List<Location> pushNotCoveredLocationToEnd(List<Location> locationList) {
        Collections.sort(locationList, new Comparator<Location>() {
            @Override
            public int compare(Location locationFirst, Location locationSecond) {
                ServiceStatus serviceStatusFirst = locationFirst.getServiceStatus();
                ServiceStatus serviceStatusSecond = locationSecond.getServiceStatus();
                if (locationFirst.getServiceStatus() != NOT_COVERED && locationSecond.getServiceStatus() != NOT_COVERED) {
                    return -1;
                }

                int result = serviceStatusSecond.ordinal() - serviceStatusFirst.ordinal();

                if (result > 0) {
                    return 1;
                }

                if (result < 0) {
                    return -1;
                }

                return 0;
            }
        });

        return locationList;
    }

    private static class LocationEmpty extends Location {

        public LocationEmpty(ServiceStatus status) {
            super(0, "", LocationType.CITY, "", status);
        }

        public LocationEmpty(String name, ServiceStatus status) {
            super(0, "", LocationType.CITY, "", status);
        }

        @Override
        public void setCityCode(String cityCode) {
            //Do nothing
        }

        @Override
        public void setServiceStatus(ServiceStatus serviceStatus) {
            //Do nothing
        }
    }

    public boolean isLocationEmpty() {
        return id == 0 && StringUtils.isBlank(name) && StringUtils.isBlank(cityCode) && CITY.equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Location) {
            Location location = (Location) o;
            if (location.cityCode == null || location.name == null) {
                return super.equals(o);
            }
            if (location.cityCode.equals(cityCode) && location.name.equals(name)) {
                return true;
            }
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return this.cityCode.hashCode() + this.name.hashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Location{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append(", parent=").append(parent);
        sb.append(", cityCode='").append(cityCode).append('\'');
        sb.append(", serviceStatus=").append(serviceStatus);
        sb.append(", deptCode='").append(deptCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
