package com.multi.spring.controller;

import com.multi.domain.user.model.UserExcelField;
import com.multi.spring.service.user.UserBO;
import com.podong.ExcelFile;
import com.podong.gc.GcSheetExcelFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "피벗차트 엑셀-API")
@RestController
@RequestMapping("/v1/api/pivot")
@Slf4j
public class PivotExcelController {

    @Autowired
    UserBO userBO;

    @ApiOperation(value = "피벗 엑셀 다운로드")
    @RequestMapping(value = "/excel", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public void getExcelDownload(HttpServletResponse response) throws IOException {
        List<List<Object>> sourceList = new ArrayList<>();

        for (int i=0; i < 100; i++) {
            List<Object> list = new ArrayList<>();
            list.add(i);
            list.add("100000" + i);
            list.add("한국");
            list.add("AOS");
            list.add("nickname_"+ i);
            list.add("erqrk@.naver.com");
            list.add(i + 1000);
            sourceList.add(list);
        }
        ExcelFile excelFile = new GcSheetExcelFile(sourceList, UserExcelField.class);


        excelFile.write(response.getOutputStream());

    }
}
