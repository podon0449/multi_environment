package com.hanteo.spring.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hanteo.common.core.exception.CommonException;
import com.hanteo.common.core.exception.HanteoServiceStatusCode;
import com.hanteo.common.core.model.common.ResultInfo;
import com.hanteo.common.core.util.JsonUtils;
import com.hanteo.spring.helper.annotation.ConditionalOnHanteoTest;
import com.hanteo.util.ExceptionUtils;
import org.hamcrest.BaseMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureCache
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@ConditionalOnHanteoTest
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, JtaAutoConfiguration.class})
abstract public class HanteoFrontTestHelper implements ApplicationContextAware, InitializingBean {
    // 하위 클래스에서 사용할 수 있는 프로필을 가져옴
    private String profile = "local";
    public String profile() { return this.profile; }


    // 빈을 주입하기 위한 컨텍스트를 받아옴
    private ApplicationContext context;
    @Override public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;

        this.profile = context.getEnvironment().getProperty("spring.profiles.active", "local");
    }


    // 각 유틸리티 메서들에 사용될 빈들을 받아옴
    private MockMvc mockMvc;
    public MockMvc mockMvc() {
        return mockMvc;
    }
    private RestTemplate restTemplate;
    public RestTemplate restTemplate() {
        return restTemplate;
    }
    @Override public void afterPropertiesSet() throws Exception {
        initializeBeans(context);
        initialize();
    }

    // 빈 초기화에 사용되는 메서드
    public void initializeBeans(ApplicationContext context) {
        this.mockMvc = context.getBean(MockMvc.class);
        this.restTemplate = context.getBean(RestTemplate.class);
    }

    // 빈 초기화 이후 실행할 초기화 로직
    // @BeforeAll 애노테이션을 사용할 경우 빈과는 관련없이 초기화하므로 해당 메서드를 제작함
    public void initialize() {}


    // Hanteo V4 REST API 호출을 위한 유틸리티 메서드
    // 간단한 구조의 Response JSON에 대응하기 위한 목적, 따로 VO 타입을 명시하지 않고 Map으로 리턴함
    public Map<?, ?> hanteoRestRequest(HttpMethod method, String url, String token) {
        // REST 통신에 대한 정보를 생성함 (method, url, authorization header)
        RequestEntity<Void> requestEntity = RequestEntity.method(method, url)
                                                         .header(AUTHORIZATION, token).build();

        // 해당 URL로 REST 통신을 거친 뒤 ResultInfo 객체의 resultData 프로퍼티에 해당하는 내용만 가져옴
        ResponseEntity<String> responseEntity = restTemplate().exchange(requestEntity, String.class);
        Map<?, ?> resultData = JsonUtils.toDataObject(responseEntity.getBody(), "resultData", Map.class);

        // API 호출 도중 에러가 발생할 경우 resultData 프로퍼티의 값은 null, ResultInfo 객체의 message가 필요하므로 ResultInfo 객체 자체를 들고옴
        if (resultData == null) {
            ResultInfo resultInfo = JsonUtils.toObject(responseEntity.getBody(), ResultInfo.class);
            if (resultInfo.getCode() != 100) { // GET 이외의 API는 resultData가 null이지만 100번 응답을 보이므로 해당 조건 체크
                throw new AssertionError(resultInfo.getMessage());
            }
        }

        return resultData;
    }


    // Mock MVC 호출을 위한 유틸리티 메서드
    // Content(Body) 입력 시 JSON 형태로 입력되고 DB 트랜잭션의 롤백 기능이 자동 탑재되며, 대부분의 한터 API가 인증을 요구하므로 인증 관련 헤더도 덧붙임
    public MockHttpServletRequestBuilder request(HttpMethod method, String urlTemplate, String token) {
        return MockMvcRequestBuilders.request(method, urlTemplate)
                                     .contentType(APPLICATION_JSON)
                                     .characterEncoding("UTF-8")
                                     .header(AUTHORIZATION, token)
                                     .header("DryRun", "true");
    }

    public ResultActions perform(MockHttpServletRequestBuilder request) throws Exception {
        ResultActions actions = mockMvc().perform(request);

        // 리스폰스를 UTF-8 형태로 받기 위해 MockHttpServletResponse의 참조값으로 setter 호출
        MockHttpServletResponse response = actions.andReturn().getResponse();
        response.setCharacterEncoding("UTF-8");

        return actions.andDo(log());
    }
    public ResultActions perform(MockHttpServletRequestBuilder request, HanteoServiceStatusCode code) throws Exception {
        try {
            return perform(request).andExpect(status().isOk())
                                   .andExpect(jsonEquals("$.code", code.getError()));
        } catch (Exception e) {
            if (ExceptionUtils.wrapThrowable(e).getReason() != code) {
                throw new AssertionError(e.getMessage());
            } else {
                return null;
            }
        }
    }
    public ResultActions perform(MockHttpServletRequestBuilder request, HttpStatus httpStatus) throws Exception {
        return perform(request).andExpect(status().is(httpStatus.value()));
    }

    public ResultInfo fetchResultInfo(MockHttpServletRequestBuilder request) throws Exception {
        return fetchResultInfo(perform(request));
    }
    public ResultInfo fetchResultInfo(ResultActions actions) throws Exception {
        String body = actions.andReturn().getResponse().getContentAsString();

        // 한터 리스폰스는 모두 ResultInfo로 감싸져서 나옴, 해당 리스폰스 체크
        ResultInfo resultInfo = JsonUtils.toType(body, ResultInfo.class);
        if (resultInfo != null) {
            return resultInfo;
        } else {
            throw new AssertionError("Can't parse response! Parsable type is only ResultInfo! Response body: "+body);
        }
    }

    public <T> T fetchResultData(MockHttpServletRequestBuilder request, TypeReference<T> returnType) throws Exception {
        return fetchResultData(perform(request), returnType);
    }
    public <T> T fetchResultData(ResultActions actions, TypeReference<T> returnType) throws Exception {
        ResultInfo resultInfo = fetchResultInfo(actions);
        if (resultInfo.getResultData() == null) return null; // 받아온 데이터가 없다면 null을 뿌려줌

        return JsonUtils.toObject(JsonUtils.toJson(resultInfo.getResultData()), returnType);
    }

    public CommonException fetchException(MockHttpServletRequestBuilder request) throws Exception {
        return fetchException(perform(request));
    }
    public CommonException fetchException(ResultActions actions) throws Exception {
        ResultInfo resultInfo = fetchResultInfo(actions);
        if (resultInfo.getCode() == 100) return null; // 성공이라면 Exception은 떨어지지 않으므로 null을 뿌려줌

        return new CommonException(resultInfo);
    }


    // 클라이언트 Basic 토큰을 받아오는 유틸리티 메서드
    public String getClientBasicToken(String clientIdx, String clientSecret) {
        String credentials = MessageFormat.format("{0}:{1}", clientIdx, clientSecret);

        return "Basic "+new String(Base64.getEncoder().encode(credentials.getBytes()));
    }

    // Assertion을 좀 더 쉽게 할 수 있는 유틸리티 메서드
    public ResultMatcher jsonEquals(String expression, Object value) {
        return jsonPath(expression).value(value);
    }
    public ResultMatcher jsonIsEmpty(String expression) {
        return jsonPath(expression).isEmpty();
    }
    public ResultMatcher jsonIsNotEmpty(String expression) {
        return jsonPath(expression).isNotEmpty();
    }

    /**
     * 해당 JSON expression에 해당되는 키값이 존재하는지 여부를 검사함
     * 값이 null이든 아니든 키값이 존재하면 통과, 아닐 경우 에러
     *
     * @param expression $.code, $.resultData.userIdx 등 JSON의 키값을 나타내는 표현식
     */
    public ResultMatcher jsonExists(String expression) {
        return jsonPath(expression).hasJsonPath();
    }
    public ResultMatcher jsonNotExists(String expression) {
        return jsonPath(expression).doesNotHaveJsonPath();
    }

    public ResultMatcher jsonVal(String expression, BaseMatcher<?> matcher) {
        return jsonPath(expression).value(matcher);
    }


    // JSON 바디 값 테스트를 위해 JSON Object를 심플하게 만들 수 있는 빌더
    public static class JsonObjectBuilder {
        private Map<String, Object> map = new HashMap<>();

        private JsonObjectBuilder() {}

        public JsonObjectBuilder put(String key, Object value) {
            map.put(key, value);

            return this;
        }

        public String build() {
            return JsonUtils.toJson(map);
        }
    }
    public JsonObjectBuilder jsonObjectBuilder() {
        return new JsonObjectBuilder();
    }

    // JSON 바디 값 테스트를 위해 JSON Array를 심플하게 만들 수 있는 빌더
    public static class JsonArrayBuilder {
        private final List<Object> array = new ArrayList<>();

        private JsonArrayBuilder() {}

        public JsonArrayBuilder add(Object value) {
            array.add(value);

            return this;
        }

        // Array 내에 맵을 추가하고 싶을 경우 심플하게 만들 수 있는 Map 빌더
        public static class MapBuilder {
            private final JsonArrayBuilder root;
            private final Map<String, Object> map = new HashMap<>();

            private MapBuilder(JsonArrayBuilder root) {
                this.root = root;
            }

            public MapBuilder put(String key, Object value) {
                map.put(key, value);

                return this;
            }

            public JsonArrayBuilder and() {
                root.add(map);
                return root;
            }
        }
        public MapBuilder map() {
            return new MapBuilder(this);
        }

        public String build() {
            return JsonUtils.toJson(array);
        }
    }
    public JsonArrayBuilder jsonArrayBuilder() {
        return new JsonArrayBuilder();
    }

    // 결과에 대한 JSON 리스폰스를 문자열 형태로 받아오기 위한 유틸리티 메서드
    // JSON 문자열을 가공하는건 테스트에서 할 수 있도록 함
    public String jsonResponse(ResultActions actions) throws Exception {
        return actions.andReturn().getResponse().getContentAsString();
    }
}
