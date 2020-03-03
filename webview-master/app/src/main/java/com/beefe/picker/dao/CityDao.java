package com.beefe.picker.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.beefe.picker.cityBean.AllBean;
import com.beefe.picker.cityBean.CityBean;
import com.beefe.picker.cityBean.PriBean;
import com.beefe.picker.cityDBhelper.CityDBhelper;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CityDao {
    private String TAG = "serDao";
    private CityDBhelper cityDBhelper;
    private SQLiteDatabase db;
    private List<CityBean> mProvinceList, mCityList, mDistrictList;

    public CityDao(Context context) {
        Log.e(TAG, "获取数据库连接");
        cityDBhelper = new CityDBhelper(context);
        //获取数据库连接
        /**
         * getWritableDatabase() 方法以读写方式打开数据库，一旦数据库的磁盘空间满了，数据库就只能读而不能写，
         * getReadableDatabase()方法先以读写方式打开数据库，如果数据库的磁盘空间满了，就会打开失败，当打开失败后会继续尝试以只读方式打开数据库。
         */
//		 db = mDBhelp.getReadableDatabase();

        mProvinceList = new ArrayList<>();
        mCityList = new ArrayList<>();
        mDistrictList = new ArrayList<>();
    }

    public AllBean selectProvinceAll() {
        db = cityDBhelper.getReadableDatabase();
        mProvinceList.clear();
        //第二个参数表示查询的列名，第三个参数表示where条件，第四个参数表示注入的where条件占位符的值，第五个参数表示gourpby列，第六个参数表示having条件，第七个参数表示orderby的列
//		Cursor cursor = db.query("provincetable", new String[]{"provinceName","provinceCode"},null, null, null, null, null);
        AllBean allBean = new AllBean();
        allBean.PriBeans = new LinkedList<PriBean>();
        Cursor cursor = db.rawQuery("SELECT * FROM pd_service_range_cur where parent=11 or parent=12 or parent=13 or parent=14", null);
        while (cursor.moveToNext()) {
            PriBean priBean = new PriBean();
            priBean.city = new LinkedList<CityBean>();
            allBean.PriBeans.add(priBean);
           /* byte[] b = cursor.getBlob(0);
              Log.e("getBlob",b.toString());
            String code = new String(b); */
            String code = cursor.getString(0);
            /*  Log.e("getBlob", b.toString());*/
            priBean.name = cursor.getString(1);
            Log.e("pribean", priBean.name);
            selectCity(code, db, priBean.city);
        }
        cursor.close();
        db.close();
        return allBean;
    }


    /**
     * @param parent 省的code
     * @return
     */
    public void selectCity(String parent, SQLiteDatabase db, LinkedList<CityBean> cb) {
        String sql = "select id,name,city_code from pd_service_range_cur where parent='" + parent + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            CityBean bean = new CityBean();
            cb.add(bean);
            bean.area = new LinkedList<>();
   /*         byte[] b = cursor.getBlob(0);*/
            /* Log.e("getBlob",b.toString());*/

            String code = cursor.getString(0);//这个却没有报错
            bean.name = cursor.getString(1);
            selectDistrict(code, db, bean.area);
        }
    }

    /**
     * @param parent
     * @return歲
     */
    public void selectDistrict(String parent, SQLiteDatabase db, LinkedList<String> areas) {
        String sql = "select id,name,dept_code  from pd_service_range_cur where parent='" + parent + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            areas.add(name);
        }
    }
}
