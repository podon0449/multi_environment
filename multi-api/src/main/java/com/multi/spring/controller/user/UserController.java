package com.multi.spring.controller.user;

import com.multi.process.rest.service.RestApiService;
import com.podong.domain.user.model.User;
import com.podong.spring.process.service.user.UserBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "유저-API")
@RestController
@RequestMapping("/v1/api/user")
@Slf4j
public class UserController {
    @Autowired RestApiService<Response> restApiService;
    @Autowired UserBO userBO;

    @ApiOperation(value = "유저 정보 API")
    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json; charset=utf8")
    public User getUserInfo(){
        //유저 idx 가져와야함
        int userIdx = 1;
        //ResponseEntity<Response>  a = restApiService.get("/v1/api/user/info", 1);
        //throw new PodongCommonException(ERROR, ERROR_NULL, "error is null");
        return userBO.userInfo(userIdx);
        //return null;
    }
}
