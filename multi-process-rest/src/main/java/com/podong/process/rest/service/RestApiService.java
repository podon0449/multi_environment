package com.podong.process.rest.service;

import com.podong.properties.ProjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.podong.process.rest.annotation.RestServer.PROCESS_USER;

@Service
public class RestApiService<T> {
    private RestTemplate restTemplate;
    @Autowired private ProjectData projectData;
    @Autowired
    public RestApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<T> get(String url) {
        return callApiEndpoint(url, HttpMethod.GET,null, (Class<T>)Object.class);
    }
    public ResponseEntity<T> get(String url, Object params) {
        //Object params 벗겨내서 url 셋팅진행.
        return callApiEndpoint(url, HttpMethod.GET,null, (Class<T>)Object.class);
    }
    public ResponseEntity<T> get(String url, Class<T> clazz) {
        return callApiEndpoint(url, HttpMethod.GET, null, clazz);
    }
    public ResponseEntity<T> post(String url, HttpHeaders httpHeaders, Object body) {
        return callApiEndpoint(url, HttpMethod.POST, body,(Class<T>)Object.class);
    }
    public ResponseEntity<T> post(String url , Object body, Class<T> clazz) {
        return callApiEndpoint(url, HttpMethod.POST, body, clazz);
    }
    private ResponseEntity<T> callApiEndpoint(String url, HttpMethod httpMethod, Object body, Class<T> clazz) {
        String pullUrl =  projectData.getServers().getUrl().get(PROCESS_USER.getKey()) + url;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return restTemplate.exchange(pullUrl, httpMethod, new HttpEntity<>( body, headers), clazz);
    }
}
