package com.hanteo.spring.helper;

import com.hanteo.common.core.model.type.HtJoinType;

import java.text.MessageFormat;
import java.util.Map;

import static com.hanteo.common.core.model.type.HtJoinType.*;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.springframework.http.HttpMethod.POST;

/**
 * properties, 설정 파일에 분리해서 넣으면 주소가 바뀔때마다 모든 프로젝트를 수정해줘야 함
 * 해당 문제를 임시로 해결하기 위해 코드에 직접적으로 url, 인증 정보 등을 넣음
 *
 * TODO 문제가 될 경우 프로퍼티 파일로 옮기거나, 더 좋은 방법이 있다면 변경바람
 */
abstract public class HanteoFrontUserTestHelper extends HanteoFrontTestHelper {
    // 한터 API를 호출하기 위한 URL을 리턴
    public String getApiUrl(String uri) {
        // 프로필이 real과 아닌 녀석들의 구분으로 url 구분자를 지음, 추후 정책이 변경된다면 변경바람
        if (startsWith(profile(), "real")) {
            return "http://api.whosfan.io" + uri;
        } else {
            return "http://apitest.whosfan.io" + uri;
        }
    }


    // 토큰 요청을 위한 디폴트 값 목록
    // 모든 프로젝트들이 참조할 수 있는 공간에 두어서 값이 변경될 때마다 대처하기 용이하게 하기 위함
    public static final String DEFAULT_CLIENT_IDX = "1000";
    public static final String DEFAULT_CLIENT_SECRET = "hanteo1004";

    // Bearer 토큰을 발급받기 위한 유틸리티 메서드
    private String requestBearerToken(String url, String clientBasicToken) {
        Map<?, ?> resultData = hanteoRestRequest(POST, url, clientBasicToken);

        // Spring OAuth2 통신 결과에는 access_token 키가 포함되어 있으므로, 해당 키를 가져옴
        return "Bearer " + resultData.get("access_token");
    }


    // 클라이언트 Bearer 토큰을 요청하는 유틸리티 메서드
    // 2021-06-09 donghans: 디폴트로 잡힌 클라이언트로 Bearer 토큰을 제작함
    public String getClientBearerToken(String clientBasicToken)  {
        String url = getApiUrl("/oauth/token/whosfan_client");

        return requestBearerToken(url, clientBasicToken);
    }
    public String getClientBearerToken(String clientId, String clientSecret) {
        return getClientBearerToken(getClientBasicToken(clientId, clientSecret));
    }
    public String getClientBearerToken() {
        return getClientBearerToken(DEFAULT_CLIENT_IDX, DEFAULT_CLIENT_SECRET);
    }

    // Whosfan SNS Bearer 토큰을 요청하는 유틸리티 메서드
    // 2021-06-09 donghans: ACTIVE 유저만 토큰을 정상적으로 발급받을 수 있고, 갖고 있는 정상 상태인 부계정이 현재 하나 뿐이므로 이런 식으로 코드를 작성함
    public String getUserBearerToken(HtJoinType joinType, String snsIdx) {
        String url = getApiUrl("/oauth/token/whosfan_sns"); // 프로필이 real과 아닌 녀석들의 구분으로 url 구분자를 지음, 추후 정책이 변경된다면 변경바람
        String queryString = MessageFormat.format("?join_type={0}&sns_idx={1}", joinType.getName(), snsIdx);

        // 후즈팬 SNS Bearer 토큰은 1000번 클라이언트(후즈팬)로만 가능하므로, 직접 기입함
        return requestBearerToken(url+queryString, getClientBasicToken("1000", "hanteo1004"));
    }
    public String getActiveUserBearerToken() {
        // donghans 부계정
        return getUserBearerToken(N_FACEBOOK, "134981142128555");
    }
}
