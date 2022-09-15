package com.cognizant.rps.util;

import com.cognizant.rps.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UtilityTest {

    @Autowired
    private Utility ut;

    @Test
    void SuperTest(){

        User mockUser = new User(1, "justin", "pass", 1);
        String ticket = ut.generateToken(mockUser);
        String userFromToken = ut.getUsernameFromToken(ticket);
        Boolean validToken = ut.validateToken(ticket, mockUser);

        assertEquals("justin", userFromToken);
        assertEquals(true, validToken);

    }
}
