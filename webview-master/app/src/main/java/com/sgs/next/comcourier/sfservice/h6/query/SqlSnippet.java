package com.sgs.next.comcourier.sfservice.h6.query;

import static java.lang.String.format;

public class SqlSnippet implements CharSequence {
    protected StringBuilder builder;

    @Override
    public int length() {
        return builder.length();
    }

    @Override
    public char charAt(int index) {
        return builder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    protected SqlSnippet(CharSequence content) {
        this.builder = new StringBuilder(content);
    }

    protected SqlSnippet() {
        this.builder = new StringBuilder();
    }

    protected SqlSnippet(String format, Object... args) {
        this(format(format, args));
    }
}
