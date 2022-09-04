package com.multi.common.core.exception;

public enum ServiceStatusCode {

    // 레거시 코드에 사용되었던 기본적인 상태 코드들
    SUCCESS(100, "[SUCCESS]"),
    FAILED(101, "[FAILED]"),

    // 쿼리 에러를 구분하기 위해 사용되는 상태 코드들
    ERROR_QUERY(200, "[ERROR_QUERY]"),
    ERROR_INSERT(201, "[ERROR_INSERT]"),
    ERROR_READ(202, "[ERROR_READ]"),
    ERROR_UPDATE(203, "[ERROR_UPDATE]"),
    ERROR_DELETE(204, "[ERROR_DELETE]"),


    // Open API에 주로 사용되었던 상태 코드들
    /* Parameter가 Null 일때 */
    ERROR_NULL(601, "[ERROR_NULL]"),
    ERROR_NO_PARAM(602, "[ERROR_NO_PARAM]"),
    ERROR_NOT_SUPPORT_TYPE(603, "[ERROR_NOT_SUPPORT_TYPE]"),
    ERROR_PARAM_VALIDITY(604, "[ERROR_PARAM_VALIDITY]"),
    ERROR_PROCESS_FAILED(605, "[ERROR_PROCESS_FAILED]"),

    ERROR_NETWORK(701, "[ERROR_NETWORK]"),
    ERROR_INTERNAL_SERVER(702, "[ERROR_INTERNAL_SERVER]"),
    ERROR_INTERNAL_SERVER_DEAD(703, "[ERROR_INTERNAL_SERVER_DEAD]"),

    /* 유저 혹은 인증에 관련된 상태 코드들 */
    // 회원가입 (81x)
    ERROR_JOIN_FAILED(810, "[ERROR_JOIN_FAILED]"),                  // 중복 가입
    ERROR_EMAIL_DUPLICATE(811, "[ERROR_EMAIL_DUPLICATE]"),          // 이메일 중복
    ERROR_NICKNAME_DUPLICATE(812, "[ERROR_NICKNAME_DUPLICATE]"),    // 닉네임 중복
    // 인증 및 로그인 (82x)
    ERROR_NOT_EXISTS(820, "[ERROR_NOT_EXISTS]"),                    // 존재하지 않음
    ERROR_ACCESS_DENIED(821, "[ERROR_ACCESS_DENIED]"),              // 접근 거부(블랙리스트, 휴면, 탈퇴대기, 잘못된 토큰값 등)
    ERROR_ACCESS_INFO_EXPIRED(822, "[ERROR_ACCESS_INFO_EXPIRED]"),  // 만료된 접근 정보(토큰 만료 등)
    ERROR_ACCOUNT_DORMANT(823, "[ERROR_ACCOUNT_DORMANT]"),          // 휴면 계정
    ERROR_ACCOUNT_WITHDRAW(824, "[ERROR_ACCOUNT_WITHDRAW]"),        // 탈퇴 계정
    ERROR_ACCOUNT_BLACK(825, "[ERROR_ACCOUNT_BLACK]"),              // 차단 계정

    ERROR_WRONG_CODE(901, "[ERROR_WRONG_CODE]"),
    ERROR_ALREADY_CERTIFICATE(902, "[ERROR_ALREADY_CERTIFICATE]"),



    ERROR_SYSTEM_EXCEPTION(1000, "[ERROR_SYSTEM_EXCEPTION]")
    ;



    private int error;
    private String reason;

    ServiceStatusCode(int error, String reason) {
        this.error = error;
        this.reason = reason;
    }

    public int getError() {
        return error;
    }

    public String getReason() {
        return reason;
    }

}
