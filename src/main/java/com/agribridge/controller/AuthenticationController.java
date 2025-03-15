package com.agribridge.controller;

import com.agribridge.dto.JsonResponse;
import com.agribridge.dto.LoginPayload;
import com.agribridge.dto.RegisterPayload;
import com.agribridge.dto.ResendTokenPayload;
import com.agribridge.response.UserResponse;
import com.agribridge.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller:::", description = "Endpoints for authentication")
@CrossOrigin(origins = {
        "http://localhost:4203",
        "http://127.0.0.1:4203"
})
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            description = "Create an Account, Exposed endpoint",
            summary = "Endpoint to create an account",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "A new user has been created"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Something went wrong"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody RegisterPayload registerPayload, HttpServletRequest request){
        JsonResponse response = authenticationService.register(registerPayload, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            description = "Login to your account, Exposed Endpoint",
            summary = "Endpoint to login to your account",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User Logged in successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Wrong username or password"
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<? > login(@RequestBody LoginPayload loginPayload){
        UserResponse response = authenticationService.login(loginPayload);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-account")
   public ResponseEntity<?> verifyAccount(@RequestParam("token") String token){
        JsonResponse response = authenticationService.verifyAccount(token);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/resend-verification-token")
    public ResponseEntity<?> resendVerificationToken(@RequestParam String email, HttpServletRequest request){
        String response = authenticationService.resendVerificationToken(email, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUserAvailable(@RequestParam(value = "username") String username){
        Boolean response = authenticationService.checkIfUsernameExist(username);
        return ResponseEntity.ok(response);
    }


}
