package com.agribridge.service;
import com.agribridge.dto.JsonResponse;
import com.agribridge.dto.LoginPayload;
import com.agribridge.dto.RegisterPayload;
import com.agribridge.dto.ResendTokenPayload;
import com.agribridge.model.User;
import com.agribridge.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    JsonResponse register(RegisterPayload registerPayload, HttpServletRequest request);
    String resendVerificationToken(String email, HttpServletRequest request);
    UserResponse login(LoginPayload loginPayload);
    JsonResponse verifyAccount(String token);
    void createVerificationToken(User user, String token);
    Boolean checkIfUsernameExist(String username);
}
