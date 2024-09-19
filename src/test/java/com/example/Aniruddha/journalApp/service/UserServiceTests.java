package com.example.Aniruddha.journalApp.service;

import com.example.Aniruddha.journalApp.entity.User;
import com.example.Aniruddha.journalApp.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepo userRepo;

    @ParameterizedTest
    @CsvSource({
            "Ani",
            "Atanu"
    })
    public void testFindByUserName(String name){

        User user = userRepo.findByUserName(name);
        assertNotNull(user);
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "3,20,23",
            "0,2,1"
    })
    public void test(int a,int b,int expected)
    {
        assertEquals(expected,a+b);
    }

}
