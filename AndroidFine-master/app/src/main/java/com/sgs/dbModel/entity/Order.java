package com.sgs.dbModel.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_article")
public class Order {

    /**
     * 命令ID
     */
    @DatabaseField
    private int id;
    /**
     * type
     */
    @DatabaseField
    private int code;
    /**
     * type
     */
    @DatabaseField
    private String type;

    /**
     *
     */
    @DatabaseField
    private String object;
    /**
     *
     */
    @DatabaseField
    private String endtime;
    /**
     *
     */
    @DatabaseField
    private int priority = 0;

    public Order() {
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", code=" + code +
                ", type='" + type + '\'' +
                ", object='" + object + '\'' +
                ", endtime='" + endtime + '\'' +
                ", priority=" + priority +
                '}';
    }
}
