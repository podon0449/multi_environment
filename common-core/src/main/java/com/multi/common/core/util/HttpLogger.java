package com.multi.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Slf4j
public class HttpLogger {
    public static final String RESPONSE_OBJECT = "responseObject";
    /**
     * AOP로 받아온 Response 객체를 등록하는 메서드
     * 리스폰스 객체가 등록되면 해당 객체의 정보를 상세하게 뿌려줌
     *
     * 만약 등록되지 않더라도 로그엔 리스폰스 스트링값을 넘겨주게 됨
     *
     * @param responseObject AOP를 통한 리스폰스 직전에 넘겨주고자 했던 객체 (REST 통신에 한함)
     */
    public static void writeObjectLog(Object responseObject) {
        if (responseObject == null) return;

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) return;

        HttpServletRequest request = requestAttributes.getRequest();
        request.setAttribute(RESPONSE_OBJECT, responseObject);
    }
}
