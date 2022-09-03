package com.multi.common.core.util;

import com.multi.common.core.exception.PodongCommonException;
import com.multi.common.core.exception.PodongServiceStatusCode;
import com.multi.common.core.model.ResultInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

import static com.multi.common.core.exception.PodongServiceStatusCode.SUCCESS;

@Component
public class ResultInfoUtil {
    public static final String ADDITIONAL_RESULT_DATA = "AdditionalResultData";

    public static void setAdditionalResultData(Object resultData) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        attributes.setAttribute(ADDITIONAL_RESULT_DATA, resultData, RequestAttributes.SCOPE_REQUEST);
    }

    public static Object getAdditionalResultData() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        return attributes.getAttribute(ADDITIONAL_RESULT_DATA, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     *  RestApi Return시 사용
     *  Result
     *  Code : 반드시 성공/실패 포함
     *  Message : 검색한 키워드(idx) or null or input messageVal
     *  ResultMap : result 그대로 전달.
     */


    public static ResultInfo setResultInfo(Object result) {
        return setResultInfoForMap(SUCCESS, result, null);
    }


    public static ResultInfo setResultInfo(PodongServiceStatusCode code, Object result) {
        return setResultInfoForMap(code, result, null);
    }

    public static ResultInfo setResultInfo(PodongServiceStatusCode code, Object result, int keyword) {
        return setResultInfoForMap(code, result, String.valueOf(keyword));
    }

    public static ResultInfo setResultInfo(PodongServiceStatusCode code, Object result, String msg) {
        return setResultInfoForMap(code, result, msg);
    }

    public static ResultInfo setResultInfo(PodongServiceStatusCode code, Map<String, Object> resultData) {
        return setResultInfoForMap(code, resultData, null);
    }

    public static ResultInfo setResultInfo(PodongServiceStatusCode code, Map<String, Object> resultData, String msg) {
        return setResultInfoForMap(code, resultData, msg);
    }
    public static ResultInfo setResultInfo(PodongCommonException e) {
        return setResultInfoForMap(e);
    }
    public static ResultInfo setResultInfo(PodongCommonException e, Object result) {
        return setResultInfoForMap(e, result);
    }

    private static ResultInfo setResultInfoForMap(PodongServiceStatusCode code, Object resultData, String msg) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(code.getError());
        resultInfo.setResultData(resultData);
        resultInfo.setMessage(msg);
        return resultInfo;
    }

    private static ResultInfo setResultInfoForMap(PodongCommonException e) {
        return setResultInfoForMap(e, null);
    }
    private static ResultInfo setResultInfoForMap(PodongCommonException e, Object result) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(e.getReason().getError());
        resultInfo.setResultData(result);
        resultInfo.setMessage(e.getMessage());
        return resultInfo;
    }



    /**
     *  Controller Return시 사용
     *  API 서버와의 통신을 통한 결과는 Json String으로 전달된다.
     *  전달값을 ResultInfo 형태로 형변환 시켜서 리턴한다.
     *
     *  통신 실패는 파라미터가 정상적이지 않거나 서버의 상태가 비정상일 경우이다.
     *  실패시에는 실패코드와 메시지를 담아 리턴하도록 하여 프론트에서 확인 가능한 형태로 예외처리를 해두었다.
     *
     * @return ResultInfo
     */
    /*public static ResultInfo getResultFromJson(String json) {
        ResultInfo resultInfo = null;
        if(!StringUtils.isBlank(json))
            resultInfo = JsonUtils.toType(json, ResultInfo.class);
        if(resultInfo == null) {
            return setResultInfo(new PodongCommonException(ERROR_SYSTEM, ERROR_SYSTEM_EXCEPTION, "API Server communication FAIL !"));
        }
        return resultInfo;
    }
    public static ResultInfo getResultFromJson(String json, String errorMsg) {
        ResultInfo resultInfo = JsonUtils.toType(json, ResultInfo.class);
        if(resultInfo == null) {
            return setResultInfo(new PodongCommonException(ERROR_SYSTEM, ERROR_SYSTEM_EXCEPTION, errorMsg));
        }
        return resultInfo;
    }*/

}
