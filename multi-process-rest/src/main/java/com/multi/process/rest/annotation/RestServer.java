package com.multi.process.rest.annotation;

import lombok.Getter;

public enum RestServer {

    // 프로세스 유저
    PROCESS_USER("process-user");


    @Getter private final String key;

    RestServer(String key) {
        this.key = key;
    }

}
