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
        User user = new User("user1", "user@gmail.com", "12345678", false);
        user.setReputation(10);

        Map<String, Object> userMap = userAdapter.toMap(user);

        assertEquals("user1", userMap.get("user_name"));
        assertEquals("user@gmail.com", userMap.get("email"));
        assertEquals("12345678", userMap.get("password"));
        assertEquals(false, userMap.get("is_admin"));
        assertEquals((long)10, userMap.get("reputation"));
    }

    //test fromMap function
    // test - 2
    @Test
    public void testFromMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_name", "user1");
        userMap.put("email", "user@gmail.com");
        userMap.put("password", "12345678");
        userMap.put("is_admin", false);
        userMap.put("reputation", (long)100);
        userMap.put("id", (long)1);

        User user = userAdapter.fromMap(userMap);

        assertEquals("user1", user.getUsername());
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("12345678", user.getPassword());
        assertFalse(user.is_adminn());
        assertEquals(100, user.getReputation());
        assertEquals(1, user.getId());
    }
    //test fromMap with null values
    // test - 3
    @Test
    public void testFromMapWithNullValues() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_name", "user1");
        userMap.put("email", null);
        userMap.put("password", null);
        userMap.put("is_admin", false);
        userMap.put("reputation", (long)50);
        userMap.put("id", (long)0);

        User user = userAdapter.fromMap(userMap);

        assertEquals("user1", user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertFalse(user.is_adminn());
        assertEquals(50, user.getReputation());
        assertEquals(0 , user.getId());
    }

    //test toJson
    // test - 4
    @Test
    public void testToJson(){
        User user = new User("user1", "user@gmail.com", "12345678", false);
        user.setReputation(10);
        user.setId((long) 5);

        String userString = userAdapter.toJson(user);
        String expected = "{\"id\":5,\"user_name\":\"" +
                "user1\",\"email\":\"user@gmail.com\"," +
                "\"password\":\"12345678\",\"reputation\":10,\"is_admin\":false}";


        assertEquals(userString , expected);

    }
    //test toJson with null values
    //test 5
    @Test
    public void testToJsonWithNullValues(){
        User user = new User("user1", "user@gmail.com", "12345678", false);

        String userString = userAdapter.toJson(user);

        String expected = "{\"id\":0,\"user_name\":\"" +
                "user1\",\"email\":\"user@gmail.com\"," +
                "\"password\":\"12345678\",\"reputation\":0,\"is_admin\":false}";


        assertEquals(userString , expected);

    }

}



