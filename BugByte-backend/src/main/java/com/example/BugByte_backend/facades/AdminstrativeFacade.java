package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.Adapters.UserAdapter;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.RegistrationService;
import com.example.BugByte_backend.services.UserService;
import io.jsonwebtoken.Claims;

import java.util.Map;

public class AdminstrativeFacade {
    // Applying singleton design pattern
    private static AdminstrativeFacade instance = null;

    public static AdminstrativeFacade getInstance(){
        if(instance==null) instance = new AdminstrativeFacade();
        return instance;
    }
    // protected constructor to avoid using it outside the class
    protected AdminstrativeFacade(){}


    private static UserService userService = UserService.getInstance();
    private static RegistrationService registrationService = new RegistrationService();

    /*
    Expected input format:
        userMap.put("user_name", user.get_user_name()); -> do not include in log in
        userMap.put("email", user.getEmail()); -> can be used as email for sign up and as email or username in log in
        userMap.put("password", user.getPassword());
        userMap.put("jwt", token); -> pass the jwt token
    Decisions:
        Used user adapter to avoid association with User model
    Flow:
        - uses the registration service to insert/validate the user to database
        - generates a jwt for the client side to store
     */
    public static String registerUser(Map<String, Object> userdata) throws Exception {
        UserAdapter adapter = new UserAdapter();
        Map<String, Object> userMap = adapter.toMap(registrationService.registerUser((String)userdata.get("email"),
                (String)userdata.get("user_name"), (String)userdata.get("password")));
        return AuthenticationService.generateJWT((long)userMap.get("id"),
                (String)userMap.get("user_name"), (boolean)userMap.get("is_admin"));
    }
    public static String loginUser(Map<String, Object> userdata) throws Exception{
        UserAdapter adapter = new UserAdapter();
        Map<String, Object> userMap = adapter.toMap(registrationService.loginUser((String)userdata.get("email"),
                (String)userdata.get("password")));
        return AuthenticationService.generateJWT((long)userMap.get("id"),
                (String)userMap.get("user_name"), (boolean)userMap.get("is_admin"));
    }
    public static void deleteUser(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        registrationService.deleteUser(id);
    }
    public static String resetPassword(Map<String, Object> userdata) throws Exception {
        String email = (String) userdata.get("email");
        return registrationService.sendResetPasswordCode(email);

    }
    public static String validateEmailedCode(Map<String, Object> userdata) throws Exception {
        UserAdapter adapter = new UserAdapter();

        String email = (String) userdata.get("email");
        String code = (String) userdata.get("code");
        Map<String, Object> userMap = adapter.toMap(registrationService.validateCode(email, code));
        Long id = (Long)userMap.get("id");
        return AuthenticationService.generateJWT((Long)userMap.get("id"),
                (String)userMap.get("user_name"), (boolean)userMap.get("is_admin"));
    }
    public static void changePassword(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        registrationService.changePassword(id, (String)userdata.get("password"));
    }

}
