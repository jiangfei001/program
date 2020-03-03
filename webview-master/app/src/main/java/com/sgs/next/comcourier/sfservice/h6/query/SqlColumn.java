package com.sgs.next.comcourier.sfservice.h6.query;

public class SqlColumn extends SqlSnippet {
    public static final String ALL_COLUMNS = "*";

    protected SqlColumn(CharSequence content) {
        super(content);
    }

    protected SqlColumn(String format, Object... args) {
        super(format, args);
    }

    public SqlSnippet as(CharSequence aliasName) {
        builder
                .append(" AS ")
                .append(aliasName);

        return this;
    }

    public static SqlColumn column(CharSequence columnName) {
        return new SqlColumn(columnName);
    }

    public static SqlColumn column(CharSequence tableName, CharSequence columnName) {
        return new SqlColumn("%s.%s", tableName, columnName);
    }

    public static SqlColumn count(CharSequence columnName) {
        return new SqlColumn("COUNT(%s)", columnName);
    }

    public static SqlColumn count(CharSequence tableName, CharSequence columnName) {
        return new SqlColumn("COUNT(%s.%s)", tableName, columnName);
    }

    public static SqlColumn quote(CharSequence content) {
        return new SqlColumn("\"%s\"", content);
    }
}
