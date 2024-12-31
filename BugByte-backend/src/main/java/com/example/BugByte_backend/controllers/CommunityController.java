package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.facades.AdministrativeFacade;
import com.example.BugByte_backend.models.Community;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/communities")
public class CommunityController {
    @Autowired
    AdministrativeFacade administrativeFacade;
    @GetMapping("/getCommunity")
    public ResponseEntity<?> getCommunity(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        token = token.replace("Bearer ", "");
        Long CommunityId = Long.parseLong(id);
        Map<String, Object> userdata = Map.of(
                "jwt", token,
                "communityId", CommunityId
        );
        try {
            Community response = administrativeFacade.getCommunityInfo(userdata);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/createCommunity")
    public ResponseEntity<?> createCommunity(@RequestBody Map<String, Object> communityData ,@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            communityData.put("jwt", token);
            if (administrativeFacade.createCommunity(communityData))
                return new ResponseEntity<>("Community Created Successfully" , HttpStatus.OK);
            else {
                return new ResponseEntity<>("error creating community", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/deleteCommunity")
    public ResponseEntity<?> deleteCommunity(@RequestBody Map<String, Object> communityData) {
        try {
            if (administrativeFacade.deleteCommunity(communityData)) {
                return new ResponseEntity<>("Community deleted Successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("error deleting community", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/editCommunity")
    public ResponseEntity<?> editCommunity(@RequestBody Map<String, Object> communityData) {
        try {
            if(administrativeFacade.editCommunity(communityData)) {
                return new ResponseEntity<>("Community edited Successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("error editing community", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/setModerator")
    public ResponseEntity<?> setModerator(@RequestBody Map<String, Object> moderatorData) {
        try {
            if(administrativeFacade.setModerator(moderatorData)) {
                return new ResponseEntity<>("User is Moderator now", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("error adding moderator", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/removeModerator")
    public ResponseEntity<?> removeModerator(@RequestBody Map<String, Object> moderatorData) {
        try {
            if(administrativeFacade.removeModerator(moderatorData)) {
                return new ResponseEntity<>("moderator removed Successfully", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("error removing moderator", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/removeMember")
    public ResponseEntity<?> removeMember(@RequestBody Map<String, Object> memberData) {
        try {
            if(administrativeFacade.removeMember(memberData)) {
                return new ResponseEntity<>("member removed Successfully", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("error removing member", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/getAdmins")
    public ResponseEntity<?> getAdmins(@RequestBody Map<String, Object> communityData) {
        try {
            List<Map<String, Object>> admins = administrativeFacade.getAdmins(communityData);
            Map<String , List> response = new HashMap<>();
            response.put("adminsList" , admins);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/joinCommunity")
    public ResponseEntity<?> joinCommunity(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> communityData) {
        token = token.replace("Bearer ", "");
        communityData.put("jwt", token);
        try {
            if(administrativeFacade.joinCommunity(communityData)) {
                return new ResponseEntity<>("user joined  Successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("error joining community", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/joinedCommunities")
    public ResponseEntity<?> joinedCommunities(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer ", "");
        try{
            return new ResponseEntity<>(administrativeFacade.getUserJoinedCommunities(token), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(Map.of("message", "unauthorized"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/allCommunities")
    public ResponseEntity<?> getAllCommunities(@RequestHeader("Authorization") String token , @RequestParam("page") Integer pageNumber ,
                                               @RequestParam("size") Integer pageSize){
        token = token.replace("Bearer ", "");
        try{
            return new ResponseEntity<>(administrativeFacade.getAllCommunities(token , pageSize , pageNumber), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(Map.of("message", "unauthorized"), HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("/leaveCommunity")
    public ResponseEntity<?> updateQuestion(@RequestHeader("Authorization") String token, @RequestParam("communityName") String communityName) {
        token = token.replace("Bearer ", "");
        try {
            return new ResponseEntity<>(administrativeFacade.leaveCommunity(token , communityName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/community-members")
    public ResponseEntity<?> getCommunityMembers(@RequestHeader("Authorization") String token,
                                                 @RequestParam long communityId) {
        try {
            String jwt = token.replace("Bearer ", "");
            List<Map<String, Object>> users = administrativeFacade.getCommunityMembers(jwt, communityId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
