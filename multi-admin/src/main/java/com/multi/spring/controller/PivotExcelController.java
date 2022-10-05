package com.multi.spring.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "피벗차트 엑셀-API")
@RestController
@RequestMapping("/v1/api/pivot")
@Slf4j
public class PivotExcelController {

    @ApiOperation(value = "피벗테이블 엑셀 다운로드")
    @RequestMapping(value = "/excel", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public void getExcelDownload(){

    }
}
