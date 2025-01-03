package com.example.BugByte_backend.AdapterTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.Adapters.UserAdapter;

import java.util.HashMap;
import java.util.Map;
public class UserAdapterTest {
    private final UserAdapter userAdapter = new UserAdapter();

    //test toMap function
    // test - 1
    @Test
    public void testToMap() {
        User user = User.builder()
                .userName("user1")
                .email("user@gmail.com")
                .password("12345678")
                .reputation(10L)
                .bio("simple bio")
                .isAdmin(false)
                .build();

        Map<String, Object> userMap = userAdapter.toMap(user);

        assertEquals("user1", userMap.get("userName"));
        assertEquals("user@gmail.com", userMap.get("email"));
        assertEquals("12345678", userMap.get("password"));
        assertEquals("simple bio", userMap.get("bio"));
        assertEquals(false, userMap.get("isAdmin"));
        assertEquals((long)10, userMap.get("reputation"));
    }

    //test fromMap function
    // test - 2
    @Test
    public void testFromMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", "user1");
        userMap.put("email", "user@gmail.com");
        userMap.put("password", "12345678");
        userMap.put("bio", "simple bio");
        userMap.put("isAdmin", false);
        userMap.put("reputation", (long)100);
        userMap.put("id", (long)1);

        User user = userAdapter.fromMap(userMap);

        assertEquals("user1", user.getUserName());
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("12345678", user.getPassword());
        assertEquals("simple bio", user.getBio());
        assertFalse(user.getIsAdmin());
        assertEquals(100, user.getReputation());
        assertEquals(1, user.getId());
    }
    
    //test fromMap with null values
    // test - 3
    @Test
    public void testFromMapWithNullValues() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", "user1");
        userMap.put("email", null);
        userMap.put("password", null);
        userMap.put("bio", null);
        userMap.put("isAdmin", false);
        userMap.put("reputation", (long)50);
        userMap.put("id", (long)0);

        User user = userAdapter.fromMap(userMap);

        assertEquals("user1", user.getUserName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getBio());
        assertFalse(user.getIsAdmin());
        assertEquals(50, user.getReputation());
        assertEquals(0, user.getId());
    }

    //test toJson
    // test - 4
    @Test
    public void testToJson() {
        User user = User.builder()
                .id(5L)
                .userName("user1")
                .email("user@gmail.com")
                .password("12345678")
                .reputation(10L)
                .bio("simple bio")
                .isAdmin(false)
                .build();

        String userString = userAdapter.toJson(user);
        String expected = "{\"id\":5,\"userName\":\"" +
                "user1\",\"email\":\"user@gmail.com\"," +
                "\"password\":\"12345678\",\"bio\":\"simple bio\",\"reputation\":10,\"isAdmin\":false}";

        assertEquals(userString, expected);
    }

    //test toJson with null values
    //test 5
    @Test
    public void testToJsonWithNullValues() {
        User user = User.builder()
                .id(0L)
                .userName("user1")
                .email("user@gmail.com")
                .password("12345678")
                .bio("simple bio")
                .reputation(0L)
                .isAdmin(false)
                .build();

        String userString = userAdapter.toJson(user);

        String expected = "{\"id\":0,\"userName\":\"" +
                "user1\",\"email\":\"user@gmail.com\"," +
                "\"password\":\"12345678\",\"bio\":\"simple bio\",\"reputation\":0,\"isAdmin\":false}";

        assertEquals(userString, expected);
    }
}



