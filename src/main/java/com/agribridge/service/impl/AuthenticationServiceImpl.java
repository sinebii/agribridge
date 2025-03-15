package com.agribridge.service.impl;
import com.agribridge.dto.JsonResponse;
import com.agribridge.dto.LoginPayload;
import com.agribridge.dto.RegisterPayload;
import com.agribridge.dto.ResendTokenPayload;
import com.agribridge.events.RegistrationCompleteEvent;
import com.agribridge.exception.CustomUserNotFoundException;
import com.agribridge.model.RegistrationVerificationToken;
import com.agribridge.model.Role;
import com.agribridge.model.User;
import com.agribridge.repository.RegistrationTokenRepository;
import com.agribridge.repository.RoleRepository;
import com.agribridge.repository.UserRepository;
import com.agribridge.response.UserResponse;
import com.agribridge.security.JwtTokenProvider;
import com.agribridge.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegistrationTokenRepository registrationTokenRepository;

    private final ApplicationEventPublisher publisher;
    @Override
    public JsonResponse register(RegisterPayload registerPayload, HttpServletRequest request) {
        validateUserUniqueness(registerPayload);
        User user = mapToUser(registerPayload);
        Role role = roleRepository.findRoleById(registerPayload.getRoleId())
                .orElseThrow(() -> new CustomUserNotFoundException(HttpStatus.BAD_REQUEST, "Role not found"));

        user.setRoles(Set.of(role));
        userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user, buildMailUrl(request)));
        user.setPassword(null);
        return JsonResponse.builder()
                .data(user)
                .message("Account created, please check your email to verify your account")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public String resendVerificationToken(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"User with associated email not found"));
        publisher.publishEvent(new RegistrationCompleteEvent(user, buildMailUrl(request)));
        return "Verification token sent successfully";
    }

    @Override
    public UserResponse login(LoginPayload loginPayload) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginPayload.getUsername(),
                    loginPayload.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Optional<User> user = userRepository.findByUsername(authentication.getName());

            UserResponse userResponse = UserResponse.builder()
                    .message("logged in successfully")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .token(jwtTokenProvider.generateToken(authentication))
                    .user(user.get())
                    .build();
            return userResponse;
        } catch (BadCredentialsException ex) {
            // Handle incorrect password scenario
            return UserResponse.builder()
                    .message("Invalid username or password.")
                    .status(HttpStatus.UNAUTHORIZED)
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .build();
        } catch (AuthenticationException ex) {
            // Handle other authentication failures
            return UserResponse.builder()
                    .message("Authentication failed: " + ex.getMessage())
                    .status(HttpStatus.UNAUTHORIZED)
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }
    }

    @Override
    public JsonResponse verifyAccount(String token) {
        RegistrationVerificationToken verificationToken = registrationTokenRepository.findByToken(token)
                .orElseThrow(()->new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"Token is invalid or has already been used"));

        if(verificationToken.getUser().getEnabled()){
            throw new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"Account Already Verified");
        }

        String message = validateAccountCreationToken(token);
        return JsonResponse.builder()
                .data(message)
                .message(message)
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @Override
    public void createVerificationToken(User user, String token) {
        if(registrationTokenRepository.existsByUser(user)){
            RegistrationVerificationToken verificationToken = registrationTokenRepository.findByUser(user).orElseThrow(()->new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"Invalid Token"));
            registrationTokenRepository.delete(verificationToken);
        }
        RegistrationVerificationToken verificationToken = new RegistrationVerificationToken(token, user);
        registrationTokenRepository.save(verificationToken);
    }

    @Override
    public Boolean checkIfUsernameExist(String username) {
        System.err.println(username);
        return !userRepository.existsByUsername(username);

    }

    public String validateAccountCreationToken(String token){
        RegistrationVerificationToken verificationToken = registrationTokenRepository.findByToken(token)
                .orElseThrow(()->new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"Invalid Token"));

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            registrationTokenRepository.delete(verificationToken);
            throw new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"Token Expired");
        }

        user.setEnabled(true);
        userRepository.save(user);
//        registrationTokenRepository.delete(verificationToken);
        return "Account Verified Successfully";
    }

    public String buildMailUrl(HttpServletRequest request){
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    private void validateUserUniqueness(RegisterPayload registerPayload) {
        if (userRepository.existsByUsername(registerPayload.getUsername())) {
            throw new CustomUserNotFoundException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByEmail(registerPayload.getEmail())) {
            throw new CustomUserNotFoundException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        if (userRepository.existsByPhoneNumber(registerPayload.getPhoneNumber())) {
            throw new CustomUserNotFoundException(HttpStatus.BAD_REQUEST, "Phone Number already exists");
        }
    }

    private User mapToUser(RegisterPayload registerPayload) {
        User user = new User();
        user.setUsername(registerPayload.getUsername());
        user.setFirstName(registerPayload.getFirstName());
        user.setLastName(registerPayload.getLastName());
        user.setAddress(registerPayload.getAddress());
        user.setPhoneNumber(registerPayload.getPhoneNumber());
        user.setGender(registerPayload.getGender());
        user.setDateOfBirth(registerPayload.getDateOfBirth());
        user.setEmail(registerPayload.getEmail());
        user.setPassword(passwordEncoder.encode(registerPayload.getPassword()));
        return user;
    }

}
