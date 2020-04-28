package com.sgs.businessmodule.httpModel;

public class RestApiResponse {

    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_FAILURE = 500;

    public Head head;
    public String body;

    public static class Head {
        public int status;
    }

}
