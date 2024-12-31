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
    public ResponseEntity<String> deleteCommunity(@RequestBody Map<String, Object> communityData) {
        try {
            boolean result = administrativeFacade.deleteCommunity(communityData);
            if (result) {
                return ResponseEntity.ok("Community deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting community.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
    @PostMapping("/setModerator/{communityId}/{moderatorName}")
    public ResponseEntity<?> setModerator(
            @PathVariable Long communityId,
            @PathVariable String moderatorName,
            @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            boolean isModeratorSet = administrativeFacade.setModerator(communityId, moderatorName, token);

            if (isModeratorSet) {
                return new ResponseEntity<>("User is now a Moderator", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error adding moderator", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method =RequestMethod.GET , value = "/isModerator/{communityId}")
    public boolean isModerator(@RequestHeader("Authorization") String token ,@PathVariable Long communityId ) {
        token = token.replace("Bearer ", "");
        try {
            if(administrativeFacade.isModerator(token , communityId)) {
               return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    @RequestMapping(method =RequestMethod.GET , value = "/isModeratorByName/{communityId}/{userName}")
    public boolean isModeratorByName(@RequestHeader("Authorization") String token ,@PathVariable Long communityId ,
                                     @PathVariable String userName) {

        token = token.replace("Bearer ", "");
        try {
            if(administrativeFacade.isModeratorByName(token , communityId , userName)) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    @PostMapping("/removeModerator/{communityId}/{moderatorName}")
    public ResponseEntity<?> removeModerator(
            @PathVariable Long communityId,
            @PathVariable String moderatorName,
            @RequestHeader("Authorization") String token) {

        try {
            // Remove the "Bearer " prefix from the token
            token = token.replace("Bearer ", "");

            // Call the facade method with individual arguments
            boolean isModeratorRemoved = administrativeFacade.removeModerator(communityId, moderatorName, token);

            if (isModeratorRemoved) {
                return new ResponseEntity<>("Moderator removed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error removing moderator", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/removeMember/{communityId}/{memberName}")
    public ResponseEntity<?> removeMember(
            @PathVariable Long communityId,
            @PathVariable String memberName,
            @RequestHeader("Authorization") String token) {

        try {
            token = token.replace("Bearer ", "");

            boolean isMemberRemoved = administrativeFacade.removeMember(communityId, memberName, token);

            if (isMemberRemoved) {
                return new ResponseEntity<>("Member removed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error removing member", HttpStatus.BAD_REQUEST);
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

    @GetMapping("/community-members/{communityId}")
    public ResponseEntity<?> getCommunityMembers(@RequestHeader("Authorization") String token,
                                                 @PathVariable long communityId) {
        try {
            String jwt = token.replace("Bearer ", "");
            List<Map<String, Object>> users = administrativeFacade.getCommunityMembers(jwt, communityId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-community")
    public ResponseEntity<?> updateCommunity(@RequestHeader("Authorization") String token,
                                             @RequestBody Community community) {
        try {
            String jwt = token.replace("Bearer ", "");
            return new ResponseEntity<>(administrativeFacade.updateCommunity(jwt, community), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
