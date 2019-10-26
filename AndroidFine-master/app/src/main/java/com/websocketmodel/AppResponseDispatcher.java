package com.websocketmodel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.dbModel.entity.InstructionRequest;
import com.zhangke.websocket.SimpleDispatcher;
import com.zhangke.websocket.dispatcher.ResponseDelivery;
import com.zhangke.websocket.response.ErrorResponse;
import com.zhangke.websocket.response.Response;
import com.zhangke.websocket.response.ResponseFactory;

public class AppResponseDispatcher extends SimpleDispatcher {

    /**
     * JSON 数据格式错误
     */
    public static final int JSON_ERROR = 11;
    /**
     * code 码错误
     */
    public static final int CODE_ERROR = 12;

    @Override
    public void onMessage(String message, ResponseDelivery delivery) {
        try {
            InstructionRequest response = JSON.parseObject(message, new TypeReference<InstructionRequest>() {
            });
            if (response.getCode() >= 100 && response.getCode() < 200) {
                delivery.onMessage(message, response);
            } else {
                ErrorResponse errorResponse = ResponseFactory.createErrorResponse();
                errorResponse.setErrorCode(CODE_ERROR);
                Response<String> textResponse = ResponseFactory.createTextResponse();
                textResponse.setResponseData(message);
                errorResponse.setResponseData(textResponse);
                errorResponse.setReserved(response);
                onSendDataError(errorResponse, delivery);
            }
        } catch (JSONException e) {
            ErrorResponse errorResponse = ResponseFactory.createErrorResponse();
            Response<String> textResponse = ResponseFactory.createTextResponse();
            textResponse.setResponseData(message);
            errorResponse.setResponseData(textResponse);
            errorResponse.setErrorCode(JSON_ERROR);
            errorResponse.setCause(e);
            onSendDataError(errorResponse, delivery);
        }
    }

    /**
     * 统一处理错误信息，
     * 界面上可使用 ErrorResponse#getDescription() 来当做提示语
     */
    @Override
    public void onSendDataError(ErrorResponse error, ResponseDelivery delivery) {
        switch (error.getErrorCode()) {
            case ErrorResponse.ERROR_NO_CONNECT:
                error.setDescription("网络错误");
                break;
            case ErrorResponse.ERROR_UN_INIT:
                error.setDescription("连接未初始化");
                break;
            case ErrorResponse.ERROR_UNKNOWN:
                error.setDescription("未知错误");
                break;
            case JSON_ERROR:
                error.setDescription("数据格式异常");
                break;
            case CODE_ERROR:
                error.setDescription("响应码错误");
                break;
        }
        delivery.onSendDataError(error);
    }
}
