package com.multi.spring.service.user;


import com.multi.domain.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserBO {
    public User userInfo(int userIdx) {
        User user = new User();
        return user;
    }
}
