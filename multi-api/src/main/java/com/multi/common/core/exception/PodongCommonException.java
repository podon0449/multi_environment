package com.multi.common.core.exception;


import com.multi.common.core.model.ResultInfo;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.multi.common.core.exception.PodongErrorType.ERROR_SQL;
import static com.multi.common.core.exception.PodongErrorType.ERROR_SYSTEM;
import static com.multi.common.core.exception.PodongServiceStatusCode.*;

public class PodongCommonException extends NestedRuntimeException {
/**
     *  UndeclaredThrowableException 에러를 방지 하기 위하여 Exception을 상속 받지 않고
     *  NestedRuntimeException을 상속 받았다.  자바 직렬화(serialize)와 관계 있는 것으로 보이며, 기존에 작성된 클래스를 활용하였지만
     *  추후 보다 근본적인 원인을 파악하여 명확한 클래스를 선택하여야 하겠다.
     *
     *  NestedRuntimeException는 기존 Mybatis SqlSessionTemplate에 작성되어 있는 예외처리 PersistenceExceptionTranslator를 참조하여 선택한 클래스이다.
     *
     *  TODO errorType에 대한 분류 기준을 세워 적용하여야 한다. /repository 는 errorType에 명시된 타입들과 맵핑이 가능한 디렉토리 구조로 만들어서 디렉토리 명으로 분류한다.
     */


    @Getter
    private PodongErrorType type = ERROR_SYSTEM;
    @Getter
    private PodongServiceStatusCode reason = ERROR_SYSTEM_EXCEPTION;

    private String message;

    // 정보를 노출하기 위한 목적이 아닌 "예외를 던진다"라는 목적에 맞춘 생성자
    public PodongCommonException() {
        super("");
        this.message = "";
    }

    // 간단한 정보만을 노출시키기 위한 생성자 (메세지를 그대로 출력)
    public PodongCommonException(String message) {
        super(message);

        setMessage(message);
    }

    // 자세한 정보를 실어나르기 위한 생성자
    public PodongCommonException(PodongErrorType type, PodongServiceStatusCode reason, String message) {
        super(message);

        setErrorCode(type, reason);
        setMessage(message);
    }

    // Rest 통신으로 받은 Exception을 해석하기 위한 생성자
    public PodongCommonException(ResultInfo resultInfo) {
        super(resultInfo.getMessage());

        setErrorCode(resultInfo);
        setMessage(resultInfo);
    }

    // 간단한 정보만을 노출시키기 위한 생성자 (Throwable의 메세지 출력)
    public PodongCommonException(Throwable throwable) {
        super(throwable.getMessage(), throwable);

        setErrorCode(throwable);
        setMessage(throwable);
    }

    // 자세한 정보를 실어나르기 위한 생성자
    public PodongCommonException(PodongErrorType type, PodongServiceStatusCode reason, Throwable throwable) {
        super(throwable.getMessage(), throwable);

        setErrorCode(type, reason);
        setMessage(throwable);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    // 에러코드 set
    private void setErrorCode(PodongErrorType type, PodongServiceStatusCode reason) {
        if (type != null) this.type = type;
        if (reason != null) this.reason = reason;
    }
    // 에러코드 set, ResultInfo를 기반으로 생성함
    private void setErrorCode(ResultInfo resultInfo) {
        if (type != null) this.type = getTypeByMessage(resultInfo.getMessage());
        if (reason != null) this.reason = getReasonByMessage(resultInfo.getMessage());
    }
    // 에러코드 set, Throwable의 정보를 받아들여 에러타입 및 스테이터스의 set을 위한 용도
    private void setErrorCode(Throwable throwable) {
        // Spring DAO Exception 같은 경우, getCause() 가 SQLException 이므로 해당 조건문 사용
        if (throwable.getCause() instanceof SQLException) {
            setErrorCode(ERROR_SQL, ERROR_QUERY);

            if (!StringUtils.isBlank(throwable.getMessage())) {
                if (throwable.getMessage().contains("insert")) {
                    setErrorCode(ERROR_SQL, ERROR_INSERT);
                } else if (throwable.getMessage().contains("select")) {
                    setErrorCode(ERROR_SQL, ERROR_READ);
                } else if (throwable.getMessage().contains("update")) {
                    setErrorCode(ERROR_SQL, ERROR_UPDATE);
                } else if (throwable.getMessage().contains("delete")) {
                    setErrorCode(ERROR_SQL, ERROR_DELETE);
                }
            }
        } else if (throwable instanceof MissingServletRequestParameterException) { // 필수로 입력해야 하는 파라미터를 비운 경우
            setErrorCode(ERROR_SYSTEM, ERROR_NO_PARAM);
        } else if (throwable instanceof HttpMessageNotReadableException || throwable instanceof MethodArgumentTypeMismatchException) { // ENUM 등의 정해진 규격의 값에 규격 외의 파라미터를 입력했을 경우
            String msg = throwable.getMessage();
            if (msg.contains("Failed to convert ") // 형변환 실패
             || msg.contains("Cannot deserialize ") // JSON 역질렬화 실패
             || msg.contains("Unrecognized field ") // JSON 바디 잘못 입력
             || msg.contains("Wrong HtDateTime ")) { // HtDateTime 잘못 입력
                setErrorCode(ERROR_SYSTEM, ERROR_PARAM_VALIDITY);
            } else if (msg.contains("Required ")) { // 필수 파라미터 미기입 에러, 파라미터를 입력하지 않은 경우 발생
                setErrorCode(ERROR_SYSTEM, ERROR_NULL);
            }
        } else if (throwable instanceof PodongCommonException) {
            PodongErrorType type = getTypeByMessage(throwable.getMessage());
            PodongServiceStatusCode reason = getReasonByMessage(throwable.getMessage());

            setErrorCode(type, reason);
        }
    }

    // 메세지 set
    private void setMessage(String message) {
        this.message = "[" + this.type.getName() + this.reason.getError() + " " + this.reason.getReason() + ": ";

        Throwable cause = getCause();
        if (cause != null) {
            // NullPointerException일 경우 스택트레이스 중 com.hanteo 패키지로 시작되는 스택트레이스 중 첫번째 건을 출력함
            // com.hanteo로 시작하는 스택트레이스 중 첫번째 건이 보편적으로 에러가 나는 이유이기 때문이며 디버깅을 용이하게 하기 위함
            if (cause instanceof NullPointerException) {
                for (StackTraceElement element : cause.getStackTrace()) {
                    if (message == null && element.getClassName().startsWith("com.podong")) {
                        message = "java.lang.NullPointerException occured in " + element;
                    }
                }
            } else {
                // 파라미터로 받은 추가하고자 하는 메세지에 Exception의 Cause들을 기입함
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append(message).append(" (Caused by: ").append(cause.getClass().getCanonicalName());
                do {
                    messageBuilder.append(", ").append(cause.getClass().getCanonicalName());
                } while ((cause = cause.getCause()) != null);
                message = messageBuilder.toString();
                message += ")";
            }
        }

        this.message +=  message;
    }
    // 메세지 set, Rest 통신으로 받아온 ResultInfo에 대해 메세지를 설정함
    private void setMessage(ResultInfo resultInfo) {
        this.message =  this.type.getName() + this.reason.getError() + " " + this.reason.getReason() + ": ";

        // ResultInfo에서 이미 만들어진 HanteoCommonException 메세지를 받아와 실제 메세지 부분만 추출함
        Matcher matcher = Pattern.compile("^\\[.+?: (.+)").matcher(resultInfo.getMessage());
        if (matcher.find()) this.message += matcher.group(1);
    }
    // 다른 예외를 통해 메세지 set
    private void setMessage(Throwable throwable) {
        // Spring DAO Exception 처럼, Cause 존재 시 그 메세지를 출력함
        if (throwable.getCause() != null) {
            setMessage(throwable.getCause().getMessage());
        } else {
            setMessage(throwable.getMessage());
        }
    }

    // 에러 메세지에서 HanteoCommonException의 에러 코드를 찾아주는 메서드
    private static PodongErrorType getTypeByMessage(String message) {
        Matcher matcher = Pattern.compile("(HT_[A-Z_]+)[0-9]+").matcher(message);
        if (matcher.find()) {
            String typeName = matcher.group(1);
            for (PodongErrorType type : PodongErrorType.values())
                if (ObjectUtils.nullSafeEquals(type.getName(), typeName))
                    return type;
        }
        return ERROR_SYSTEM;
    }
    private static PodongServiceStatusCode getReasonByMessage(String message) {
        Matcher matcher = Pattern.compile("HT_[A-Z_]+([0-9]+)").matcher(message);
        if (matcher.find()) {
            int reasonError = Integer.parseInt(matcher.group(1));
            for (PodongServiceStatusCode reason : PodongServiceStatusCode.values())
                if (ObjectUtils.nullSafeEquals(reason.getError(), reasonError))
                    return reason;
        }
        return ERROR_SYSTEM_EXCEPTION;
    }

}
