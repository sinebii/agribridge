package com.agribridge.service.impl;
import com.agribridge.dto.AccountUpdatePayload;
import com.agribridge.dto.JsonResponse;
import com.agribridge.dto.ResetPasswordPayload;
import com.agribridge.exception.CustomUserNotFoundException;
import com.agribridge.exception.RoleNotFoundException;
import com.agribridge.model.Role;
import com.agribridge.model.User;
import com.agribridge.repository.RoleRepository;
import com.agribridge.repository.UserRepository;
import com.agribridge.service.AppUserService;
import com.agribridge.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Override
    public JsonResponse resetPassword(ResetPasswordPayload resetPassword) {
        User user = userRepository.findByUsername(AppUtils.findAccountUsername()).orElseThrow(()->new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"App user not found"));
        if(!passwordEncoder.matches(resetPassword.getPassword(), user.getPassword())){
            throw new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"Password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
        userRepository.save(user);
        // TODO: Send email to user informing them of password change
        return JsonResponse.builder()
                .data(user)
                .message("Password reset successful")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .build();
    }



    @Override
    public JsonResponse updateUserRole(AccountUpdatePayload accountUpdatePayload) {
        Role role = roleRepository.findRoleById(accountUpdatePayload.getRoleId()).orElseThrow(()->new RoleNotFoundException(HttpStatus.BAD_REQUEST,"Role not found"));
        User user = userRepository.findById(accountUpdatePayload.getUserId()).orElseThrow(()->new CustomUserNotFoundException(HttpStatus.BAD_REQUEST,"User not found"));
        Set<Role> userRoles = user.getRoles();
        userRoles.add(role);
        userRepository.save(user);
        //TODO add Email notification to the user
        return JsonResponse.builder()
                .message("Role Added Successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(user)
                .build();
    }

}
