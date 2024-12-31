package com.example.BugByte_backend.facades;

import com.example.BugByte_backend.Adapters.CommunityAdapter;
import com.example.BugByte_backend.Adapters.UserAdapter;
import com.example.BugByte_backend.controllers.GoogleAuthController;
import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.services.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.jsonwebtoken.Claims;
import org.apache.kafka.common.protocol.types.Field;
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

    @Autowired
    CommunityService communityService;
    @Autowired
    ModeratorService moderatorService;
    @Autowired
    AuthenticationService authenticationService;

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
        String token = (String) userdata.get("jwt");
        Claims claim = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        Map<String, Object> profileData= userService.getProfile((String)userdata.get("userName"));
        long id2 = (long) profileData.get("id");
        profileData.put("is_following",userService.isFollowing(id, id2));
        profileData.remove("id");

        return profileData;
    }

    public void updateProfile(Map<String, Object> userdata) throws Exception {
        try {
            Claims claim = AuthenticationService.parseToken((String) userdata.get("jwt"));
            long id = Long.parseLong(claim.getId());
            userService.updateProfile(String.valueOf(userdata.get("bio")), id);
        } catch (Exception e){
            throw new Exception("Unauthorized operation: "+ e.getMessage());
        }
    }

    public boolean followUser(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        return userService.followUser(id, (String) userdata.get("userName"));
    }

    public boolean unfollowUser(Map<String, Object> userdata) throws Exception {
        String token = (String) userdata.get("jwt");
        Claims claim  = AuthenticationService.parseToken(token);
        long id = Long.parseLong(claim.getId());
        return userService.unfollowUser(id, (String) userdata.get("userName"));
    }

    public List<Map<String, Object>> getFollowers(Map<String, Object> userdata) throws Exception {
        List<User> followers = userService.getFollowers((String)userdata.get("userName"));
        UserAdapter adapter = new UserAdapter();
        List <Map<String, Object>> followersMap = followers.stream().map(adapter::toMap).toList();
        for (Map<String, Object> follower : followersMap) {
            follower.remove("password");
            follower.remove("email");
            follower.remove("id");
        }
        return followersMap;
    }

    public List<Map<String, Object>> getFollowings(Map<String, Object> userdata) throws Exception {
        List<User> followings = userService.getFollowings((String)userdata.get("userName"));
        UserAdapter adapter = new UserAdapter();
        List <Map<String, Object>> followingsMap = followings.stream().map(adapter::toMap).toList();
        for (Map<String, Object> following : followingsMap) {
            following.remove("password");
            following.remove("email");
            following.remove("id");
        }
        return followingsMap;
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
            Map<String, Object> userMap = adapter.toMap(registrationService.registerUsingGoogle(name, email));
            String jwt = AuthenticationService.generateJWT((long) userMap.get("id"),
                    (String) userMap.get("userName"), (boolean) userMap.get("isAdmin"));
            boolean isAdmin = (boolean) userMap.get("isAdmin");
            return Map.of(
                    "jwt", jwt,
                    "isAdmin", isAdmin
            );
        } else {
            throw new RuntimeException("Invalid token");
        }
    }
    public Community getCommunityInfo(Map<String,Object> map) throws Exception {
        String token = (String) map.get("jwt");
        String userName = authenticationService.getUserNameFromJwt(token);
        System.out.println("username ="+userName);
        if (userName == null) throw new Exception("userName is null");
        Community community = communityService.getCommunityById((Long)map.get("communityId"));
        System.out.println("community "+community.toString());
        System.out.println(map.get("communityId"));
        CommunityAdapter communityAdapter = new CommunityAdapter();
        return community;//communityAdapter.toMap(community);
    }
    public boolean createCommunity(Map<String,Object> map){
        try {
            String token = (String) map.get("jwt");
            boolean isAdmin = authenticationService.getIsAdminFromJwt(token);
            System.out.println(isAdmin);
            if (!isAdmin) throw new Exception("user is not an admin");
            CommunityAdapter communityAdapter = new CommunityAdapter();
            map.remove("jwt");
            map.put("admin_id" , authenticationService.getIdFromJwt(token));
            Long id = communityService.createCommunity(communityAdapter.fromMap(map));
            return  true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean deleteCommunity(Map<String, Object> map) {
        try {
            String token = (String) map.get("jwt");
            boolean isAdmin = authenticationService.getIsAdminFromJwt(token);
            if (!isAdmin) {
                throw new IllegalArgumentException("User is not an admin.");
            }
            Long communityId = Long.parseLong(map.get("communityId").toString());
            return communityService.deleteCommunity(communityId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid community ID or JWT.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while deleting the community.", e);
        }
    }

    public boolean editCommunity(Map<String,Object> map)
    {
        try {
            String token = (String) map.get("jwt");
            boolean isAdmin = authenticationService.getIsAdminFromJwt(token);
            if (!isAdmin) throw new Exception("user is not an admin");
            Community comm = new Community(Long.parseLong((String) map.get("communityId")),(String)map.get("name"),(String) map.get("desription"));
            return communityService.updateCommunity(comm);
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUserProfilePicture(Map<String, Object> map) throws Exception {
        String jwt = (String) map.get("jwt");
        Claims claim = AuthenticationService.parseToken(jwt);
        if(claim.getId()==null) throw new Exception("User Unauthorized ");
        Long id = Long.valueOf(claim.getId());
        String url = (String) map.get("url");
        userService.updatePicture(id, url);
    }

    public boolean setModerator(Long communityId,String moderatorName,String  token) {
        try {
            boolean isAdmin = authenticationService.getIsAdminFromJwt(token);
            if (!isAdmin) throw new Exception("user is not an admin");
        return moderatorService.setModerator(moderatorName, communityId);
        } catch (Exception e)
        {
            return false;
        }
    }

    public boolean removeModerator(Long communityId, String moderatorName,String token)
    {
        try {
            boolean isAdmin = authenticationService.getIsAdminFromJwt(token);
            if (!isAdmin) throw new Exception("user is not an admin");
            return moderatorService.removeModerator(moderatorName , communityId);
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public boolean isModerator(String jwt , Long communityId)
    {
        try {
            Long userId = authenticationService.getIdFromJwt(jwt);
            return moderatorService.isModerator(userId,communityId);
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public boolean isModeratorByName(String jwt , Long communityId , String userName)
    {
        try {
            boolean isAdmin = authenticationService.getIsAdminFromJwt(jwt);
            if (!isAdmin) throw new Exception("user is not an admin");

            return moderatorService.isModeratorByName(userName,communityId);
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public boolean removeMember(Long communityId, String memberName,String token)
    {
        try {
            boolean isAdmin = authenticationService.getIsAdminFromJwt(token);
            if (!isAdmin) throw new Exception("user is not an admin");
            return communityService.deleteMember(communityId ,memberName);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> getAdmins(Map<String , Object>req) throws Exception {
        String token = (String) req.get("jwt");
        boolean isAdmin = authenticationService.getIsAdminFromJwt(token);
        if (!isAdmin) throw new Exception("user is not an admin");
        List<User> admins = communityService.getCommunityAdmins(( (Long)req.get("communityId")));
        UserAdapter adapter = new UserAdapter();
        List <Map<String, Object>> adminsMap = admins.stream().map(adapter::toMap).toList();
        for (Map<String, Object> admin : adminsMap) {
            admin.remove("password");
            admin.remove("email");
            admin.remove("id");
        }
        return adminsMap;
    }

    public boolean joinCommunity(Map<String,Object>req)
    {
        try {
            String token = (String) req.get("jwt");
            long userId = authenticationService.getIdFromJwt(token);
            return communityService.joinCommunity( (Long)req.get("communityId")
                    ,userId);
        } catch (Exception e) {
            return false;
        }
    }
    public List<Community> getUserJoinedCommunities(String jwt) throws Exception {
        long userId = authenticationService.getIdFromJwt(jwt);
        System.out.println(userId);
        //if (id == null )throw new Exception("UnAuthorized");
        List<Community> comms = communityService.getUserCommunities(userId);
        System.out.println("here");

        return comms;
    }
    public List<Community> getAllCommunities(String jwt , int pageSize , int pageNumber) throws Exception {
        long userId = authenticationService.getIdFromJwt(jwt);
        System.out.println(userId);
        //if (id == null )throw new Exception("UnAuthorized");
        List<Community> comms = communityService.getAllCommunities(pageSize,pageNumber);
        System.out.println("get All communities");
        return comms;
    }
    public boolean leaveCommunity(String jwt , String communityName)
    {
        Claims claim = AuthenticationService.parseToken(jwt);
        Long id = Long.parseLong(claim.getId());
        return communityService.leaveCommunity(communityName,id);
    }
    public boolean isAdmin (String jwt ,String username)
    {
        try {
            boolean isAdmin = authenticationService.getIsAdminFromJwt(jwt);
            if (!isAdmin) throw new Exception("user is not an admin");
            return userService.getUser(username).getIsAdmin();
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
