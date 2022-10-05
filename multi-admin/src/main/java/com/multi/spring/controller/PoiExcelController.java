package com.multi.spring.controller;

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


@Api(tags = "POI 엑셀-API")
@RestController
@RequestMapping("/v1/api/poi")
@Slf4j
public class PoiExcelController {

    @ApiOperation(value = "poi 엑셀 다운로드")
    @RequestMapping(value = "/excel", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public void getExcelDownload(){

    }
}
