package com.multi.spring.service.user;


import com.multi.domain.user.model.User;
import com.multi.domain.user.model.UserExcelDetailCol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserBO {
    public User userInfo(int userIdx) {
        User user = new User();
        return user;
    }

    public List<UserExcelDetailCol> getUserExcelDetailColList() {
        //DB 연동하지 않았으니 임시로 처리하자.
        List<UserExcelDetailCol> userExcelDetailColList = new ArrayList<>();
        for (int i=0; i < 100; i++) {
            UserExcelDetailCol userExcelDetailCol = new UserExcelDetailCol();
            userExcelDetailCol.setUserIdx("100000" + i);
            userExcelDetailCol.setEmail("testExcel" +i+ "@co.kr");
            userExcelDetailCol.setDevice("AOS");
            userExcelDetailCol.setRank(i);
            userExcelDetailCol.setCountryName("한국");
            userExcelDetailCol.setNickname("USER_" +i);
            userExcelDetailColList.add(userExcelDetailCol);
        }
        return userExcelDetailColList;
    }

}
