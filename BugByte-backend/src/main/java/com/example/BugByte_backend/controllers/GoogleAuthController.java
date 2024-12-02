package com.example.BugByte_backend.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class GoogleAuthController {

    private static final String CLIENT_ID = "40038768890-7h07ab156ebvn2aubqjdiup7ss8l7e05.apps.googleusercontent.com";
    @PostMapping("/google")
    public Object verifyGoogleToken(@RequestBody Map<String, String> requestBody) {
        try {
            // Create a verifier
            String tokenRequest = requestBody.get("token");
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(),
                    new com.google.api.client.json.jackson2.JacksonFactory()
            ).setAudience(Collections.singletonList(CLIENT_ID)).build();

            // Verify the token
            GoogleIdToken idToken = verifier.verify(tokenRequest);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract user information
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                return new UserResponse(email, name);
            } else {
                throw new RuntimeException("Invalid token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error verifying token: " + e.getMessage());
        }
    }

    // Response DTO
    public static class UserResponse {
        private String email;
        private String name;

        public UserResponse(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

    }
}

