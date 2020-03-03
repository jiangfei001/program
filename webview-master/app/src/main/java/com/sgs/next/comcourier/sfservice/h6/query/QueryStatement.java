package com.sgs.next.comcourier.sfservice.h6.query;


import com.sgs.next.comcourier.sfservice.fourlevel.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryStatement extends SqlStatement {

    public static final String DESCENDING = "DESC";
    public static final String ASCENDING = "ASC";
    public static final String IN = "IN";

    protected QueryStatement(boolean isDistinct, CharSequence... columns) {
        super();
        List<String> lst = new ArrayList<String>();
        for (CharSequence charSequence : columns) {
            lst.add(charSequence.toString());
        }
        builder
                .append(isDistinct ? "SELECT DISTINCT " : "SELECT ")
                .append(StringUtils.join(lst, ","));
    }

    protected QueryStatement() {
        super();

        builder.append("SELECT COUNT (*) ");
    }

    public static QueryStatement select(CharSequence... columns) {
        return new QueryStatement(false, columns);
    }

    public static QueryStatement selectDistinct(CharSequence... columns) {
        return new QueryStatement(true, columns);
    }

    public static QueryStatement selectCount() {
        return new QueryStatement();
    }

    public QueryStatement from(CharSequence tableName) {
        builder
                .append(" FROM ")
                .append(tableName);

        return this;
    }

    public QueryStatement fromChildTable(CharSequence tableName) {
        builder
                .append(" FROM (")
                .append(tableName)
                .append(" )");

        return this;
    }

    public QueryStatement join(CharSequence tableName) {
        builder
                .append(" JOIN ")
                .append(tableName);

        return this;
    }

    public QueryStatement leftJoin(CharSequence tableName) {
        builder
                .append(" LEFT JOIN ")
                .append(tableName);

        return this;
    }

    public QueryStatement leftOuterJoin(CharSequence tableName){
        builder
                .append(" LEFT OUTER JOIN ")
                .append(tableName);
        return this;
    }

    public QueryStatement on(CharSequence leftHand, CharSequence rightHand) {
        builder
                .append(" ON ")
                .append(leftHand).append(" = ").append(rightHand);
        return this;
    }

    public QueryStatement where(CharSequence criteria) {
        builder
                .append(" WHERE ")
                .append("(")
                .append(criteria)
                .append(")");

        return this;
    }

    public QueryStatement or(CharSequence criteria) {
        builder
                .append(" OR ")
                .append("(")
                .append(criteria)
                .append(")");

        return this;
    }

    public QueryStatement and(CharSequence criteria) {
        builder
                .append(" AND ")
                .append("(")
                .append(criteria)
                .append(")");

        return this;
    }

    public QueryStatement groupBy(CharSequence column) {
        builder
                .append(" GROUP BY ")
                .append(column);

        return this;
    }

    public QueryStatement having() {
        builder
                .append(" HAVING COUNT(*) ");

        return this;
    }

    public QueryStatement orderBy(CharSequence column, CharSequence orderType) {
        builder
                .append(" ORDER BY ")
                .append(column)
                .append(" ")
                .append(orderType);

        return this;
    }

    public QueryStatement orderBy(CharSequence[] columns, CharSequence[] orderTypes) {
        builder
                .append(" ORDER BY ");
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i])
                    .append(" ")
                    .append(orderTypes[i])
                    .append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return this;
    }

    public QueryStatement limit(int numberOfResults) {
        builder
                .append(" LIMIT ")
                .append(numberOfResults);

        return this;
    }

    public SqlQuery toQuery() {
        String sql = builder.append(";").toString();

        return new SqlQuery(sql);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public QueryStatement unionAll(String s) {
        builder
                .append(" union all ")
                .append(s);
        return this;
    }
}
