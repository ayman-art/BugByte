package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AuthenticationServiceTest {
    @Test
    void TestJWTForNonAdmin(){
        String jwt = AuthenticationService.generateJWT(1000, "testUser", false);
        Claims claim = AuthenticationService.parseToken(jwt);
        Boolean isAdmin = (Boolean) claim.get("is_admin");
        assertEquals(false, isAdmin);
    }
    @Test
    void TestJWTForAdmin(){
        String jwt = AuthenticationService.generateJWT(1000, "testUser", true);
        Claims claim = AuthenticationService.parseToken(jwt);
        Boolean isAdmin = (Boolean) claim.get("is_admin");
        assertEquals(true, isAdmin);
    }
    @Test
    void TestJWTId(){
        String jwt = AuthenticationService.generateJWT(1000, "testUser", false);
        Claims claim = AuthenticationService.parseToken(jwt);
        Long Id = Long.parseLong(claim.getId());
        assertEquals(1000, Id);
    }
    @Test
    void TestJWTId2(){
        String jwt = AuthenticationService.generateJWT(100, "testUser", false);
        Claims claim = AuthenticationService.parseToken(jwt);
        Long Id = Long.parseLong(claim.getId());
        assertEquals(100, Id);
    }
    @Test
    void TestJWTId3(){
        String jwt = AuthenticationService.generateJWT(100, "testUser", false);
        Claims claim = AuthenticationService.parseToken(jwt);
        Long Id = Long.parseLong(claim.getId());
        assertNotEquals(1000, Id);
    }
    @Test
    void TestJWTName(){
        String jwt = AuthenticationService.generateJWT(100, "testUser", false);
        Claims claim = AuthenticationService.parseToken(jwt);
        String name = claim.getSubject();
        assertEquals("testUser", name);
    }
    @Test
    void TestJWTName2(){
        String jwt = AuthenticationService.generateJWT(100, "porvah", false);
        Claims claim = AuthenticationService.parseToken(jwt);
        String name = claim.getSubject();
        assertEquals("porvah", name);
    }

}
