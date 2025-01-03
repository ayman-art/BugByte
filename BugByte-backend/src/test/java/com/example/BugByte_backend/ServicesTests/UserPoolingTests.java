package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserPoolingTests extends UserService {
    private static Long id = 0L;
    private User CreateDummyUser() {
        id++;
        return User.builder()
                .id(id)
                .userName("name" + id)
                .email("example@provider.com")
                .password("password")
                .build();
    }

    @Test
    void cache1User() {
        User testUser = CreateDummyUser();
        cacheUser(testUser);
        User cached = getCachedUser(testUser.getUserName());
        assertEquals(testUser, cached);
    }

    @Test
    void cache1User2(){
        User testUser = CreateDummyUser();
        cacheUser(testUser);
        User cached = getCachedUser(testUser.getUserName());
        assertEquals(testUser, cached);
    }

    @Test
    void cache1000Users() {
        User[] testUser = new User[1000];
        for(int i = 0; i < 1000; i++) {
            testUser[i] = CreateDummyUser();
            cacheUser(testUser[i]);
        }

        User cached = getCachedUser(testUser[0].getUserName());
        assertEquals(testUser[0], cached);
    }

    @Test
    void cache1000Users2() {
        User[] testUser = new User[1000];
        for(int i = 0; i < 1000; i++) {
            testUser[i] = CreateDummyUser();
            cacheUser(testUser[i]);
        }
        User cached = getCachedUser(testUser[999].getUserName());
        assertEquals(testUser[999], cached);
    }

    @Test
    void cache1000Users3() {
        User[] testUser = new User[1000];
        for(int i = 0; i < 1000; i++) {
            testUser[i] = CreateDummyUser();
            cacheUser(testUser[i]);
        }
        User cached = getCachedUser(testUser[55].getUserName());
        assertEquals(testUser[55], cached);
    }

    @Test
    void cache1001Users() {
        User[] testUser = new User[1001];
        for(int i = 0; i < 1001; i++) {
            testUser[i] = CreateDummyUser();
            cacheUser(testUser[i]);
        }
        User cached = getCachedUser(testUser[1000].getUserName());
        assertEquals(testUser[1000], cached);
    }

    @Test
    void cache1001Users2(){
        User[] testUser = new User[1001];
        for(int i = 0; i < 1001; i++) {
            testUser[i] = CreateDummyUser();
            cacheUser(testUser[i]);
        }
        User cached = getCachedUser(testUser[1].getUserName());
        assertEquals(testUser[1], cached);
    }

    @Test
    void cache1001Users3() {
        User[] testUser = new User[1001];
        for(int i = 0; i < 1001; i++) {
            testUser[i] = CreateDummyUser();
            cacheUser(testUser[i]);
        }
        assertThrows(NullPointerException.class ,()->getCachedUser(testUser[0].getUserName()));
    }
}
