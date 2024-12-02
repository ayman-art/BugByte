package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.Adapters.UserAdapter;
import com.example.BugByte_backend.controllers.GoogleAuthController;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.RegistrationService;
import com.example.BugByte_backend.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class AdministrativeFacade {
    private static final String CLIENT_ID = "40038768890-7h07ab156ebvn2aubqjdiup7ss8l7e05.apps.googleusercontent.com";
    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    /*
    Expected input format:
        userMap.put("userName", user.get_userName()); -> do not include in log in
        userMap.put("email", user.getEmail()); -> can be used as email for sign up and as email or username in log in
        userMap.put("password", user.getPassword());
        userMap.put("jwt", token); -> pass the jwt token
    Decisions:
        Used user adapter to avoid association with User model
    Flow:
        - uses the registration service to insert/validate the user to database
        - generates a jwt for the client side to store
     */
    public Map<String, Object> registerUser(Map<String, Object> userdata) throws Exception {
        UserAdapter adapter = new UserAdapter();
        Map<String, Object> userMap = adapter.toMap(registrationService.registerUser((String)userdata.get("email"),
                (String)userdata.get("userName"), (String)userdata.get("password")));
        String jwt = AuthenticationService.generateJWT((long)userMap.get("id"),
                (String)userMap.get("userName"), (boolean)userMap.get("isAdmin"));
        boolean isAdmin = (boolean)userMap.get("isAdmin");

        return Map.of(
            "jwt", jwt,
            "isAdmin", isAdmin
        );

    }

    public  String loginUser(Map<String, Object> userdata) throws Exception{
        UserAdapter adapter = new UserAdapter();
        Map<String, Object> userMap = adapter.toMap(registrationService.loginUser((String)userdata.get("email"),
                (String)userdata.get("password")));
        return AuthenticationService.generateJWT((long)userMap.get("id"),
                (String)userMap.get("userName"), (boolean)userMap.get("isAdmin"));
    }

    public  void deleteUser(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        registrationService.deleteUser(id);
    }

    public  String resetPassword(Map<String, Object> userdata) throws Exception {
        String email = (String) userdata.get("email");
        return registrationService.sendResetPasswordCode(email);

    }

    public String validateEmailedCode(Map<String, Object> userdata) throws Exception {
        UserAdapter adapter = new UserAdapter();

        String email = (String) userdata.get("email");
        String code = (String) userdata.get("code");
        Map<String, Object> userMap = adapter.toMap(registrationService.validateCode(email, code));
        Long id = (Long)userMap.get("id");
        return AuthenticationService.generateJWT((Long)userMap.get("id"),
                (String)userMap.get("userName"), (boolean)userMap.get("isAdmin"));
    }

    public void changePassword(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        registrationService.changePassword(id, (String)userdata.get("password"));
    }

    // User Profile Section
    public Map<String, Object> getProfile(Map<String, Object> userdata) throws Exception {
        return userService.getProfile((String)userdata.get("userName"));
    }
    public Map<String, Object> updateProfile(Map<String, Object> userdata) throws Exception {
        return null;
    }
    public boolean followUser(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        return userService.followUser(id, (String)userdata.get("userName"));
    }

    public boolean unfollowUser(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        return userService.unfollowUser(id, (String)userdata.get("userName"));
    }

    public List<Map<String, Object>> getFollowers(Map<String, Object> userdata) throws Exception {
        List<User> followers = userService.getFollowers((String)userdata.get("userName"));
        UserAdapter adapter = new UserAdapter();
        return followers.stream().map(adapter::toMap).toList();
    }

    public List<Map<String, Object>> getFollowings(Map<String, Object> userdata) throws Exception {
        List<User> followings = userService.getFollowings((String)userdata.get("userName"));
        UserAdapter adapter = new UserAdapter();
        return followings.stream().map(adapter::toMap).toList();
    }

    public boolean makeAdmin(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        return userService.makeAdmin(id, (String)userdata.get("userName"));
    }
    public Map<String, Object> googleOAuthSignUp(@RequestBody Map<String, String> requestBody) throws Exception {
        String tokenRequest = requestBody.get("token");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.jackson2.JacksonFactory()
        ).setAudience(Collections.singletonList(CLIENT_ID)).build();

        GoogleIdToken idToken = verifier.verify(tokenRequest);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            UserAdapter adapter = new UserAdapter();
            Map<String, Object> userMap = adapter.toMap(registrationService.registerUsingGoogle(name , email));
            String jwt = AuthenticationService.generateJWT((long)userMap.get("id"),
                    (String)userMap.get("userName"), (boolean)userMap.get("isAdmin"));
            boolean isAdmin = (boolean)userMap.get("isAdmin");
            return Map.of(
                    "jwt", jwt,
                    "isAdmin", isAdmin
            );
        } else {
            throw new RuntimeException("Invalid token");
        }
    }
}
