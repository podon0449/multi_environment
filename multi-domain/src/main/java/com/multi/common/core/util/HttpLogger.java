package com.multi.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * HTTP 정보를 로깅하는 유틸리티
 * by donghans, at 2021.08.13
 *
 * 프론트와 협업하면서, 앱단에서 자신이 호출하는 헤더조차 모르고 호출하면서 협업에 어려움을 많이 겪어서 만드는 유틸리티
 * 기본적으로 필터 위에서 동작하는 것을 기준으로 잡고 설계하였으며, com.hanteo.process.rest.configuration.StubConfiguration에 해당 필터를 등록해둠
 *
 * 기본적인 INFO 레벨의 Slf4j 구현체(Log4j, Logback 등) 활성화 시 아래와 같은 정보를 표시함
 *  - 호출자의 IP 주소
 *  - 리퀘스트 메서드 (GET, POST, ...)
 *  - 리퀘스트 URI (http 프로토콜과 도메인 주소 뒤의 "실제 주소")
 *  - 리퀘스트 바디 (JSON 혹은 HTML FORM 바디 등)
 *  - Basic / Bearer 인증 정보 (토큰을 해석해서 보여줌)
 *  - @RestController 등을 사용할 경우 ResponseProcess AOP로 받아온 리스폰스 객체 JSON 값
 *  - RestController 등에서 Exception이 던져져서 AOP에서 처리되었을 경우 Exception에 대한 요약 정보
 *  - 필터단에서 리스폰스 OutputStream이 직접적으로 쓰였을 경우 리스폰스 바디 문자열
 *  - 요청에 대한 응답 시간 (time elapsed), 요청에 대한 응답 전문의 크기 (byte(s) sent)
 *
 * DEBUG 레벨의 Slf4j 퍼사드 활성화 시 아래와 같은 정보를 추가적으로 표시함
 *  - 스택 트레이스 전문 (INFO 레벨에선 com.hanteo 하위의 스택트레이스만 표기하여 디버깅을 용이하게 함)
 *
 * TRACE 레벨의 Slf4j 퍼사드 활성화 시 아래와 같은 정보를 추가적으로 표시함
 *  - 모든 리퀘스트 헤더
 *  - 모든 리스폰스 헤더
 *
 * 리퀘스트에 대한 로직 수행 중 Exception이 떨어져서 해당 필터에 캐치될 경우, ERROR 레벨로 에러 정보와 함께 리퀘스트를 로깅함
 *
 * 또한, 대용량, 느린 요청/응답의 경우 호출자 IP, 호출 URL, 해당 경고에 대한 수치와 함께 별도의 WARN 레벨 로그 생성
 *  - 대용량 리퀘스트 바디의 경우 바이트 수 표시
 *  - 대용량 리스폰스 바디의 경우 바이트 수 표시
 *  - 느린 리스폰스의 경우 리스폰스에 걸린 시간 표시
 * 이때, 요청에 대한 분석을 용이하게 하기 위해 리퀘스트 로그도 WARN 레벨로 표시됨
 * (수동으로 HttpLogUtils.setWarningFlag() 를 사용해 리퀘스트 로그를 WARN 레벨로 만들 수도 있음)
 *
 * 해당 HTTP 로깅 유틸리티로 디버깅을 편하게 할 수 있길 바라며...
 */
@Slf4j
public class HttpLogger {
    public static final String RESPONSE_OBJECT = "podongResponseObject";


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
