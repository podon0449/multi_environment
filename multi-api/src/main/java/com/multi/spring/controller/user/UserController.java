package com.multi.spring.controller.user;

import com.multi.process.rest.service.RestApiService;
import com.multi.spring.service.user.UserBO;
import com.multi.domain.user.model.User;
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
    @Autowired
    RestApiService<Response> restApiService;
    @Autowired
    UserBO userBO;

    @ApiOperation(value = "유저 정보 API")
    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = "application/json; charset=utf8")
    public User getUserInfo(){
        //유저 idx 가져와야함
        int userIdx = 1;
        //ResponseEntity<Response>  a = restApiService.get("/v1/api/user/info", 1);
        return userBO.userInfo(userIdx);
        //return null;
    }
}
