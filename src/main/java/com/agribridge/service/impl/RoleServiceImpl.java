package com.agribridge.service.impl;
import com.agribridge.dto.JsonResponse;
import com.agribridge.model.Role;
import com.agribridge.model.User;
import com.agribridge.repository.RoleRepository;
import com.agribridge.repository.UserRepository;
import com.agribridge.service.RoleService;
import com.agribridge.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public JsonResponse createRole(String name) {
        User user = userRepository.findByUsername(AppUtils.findAccountUsername()).orElseThrow(()->new RuntimeException("User not found"));
        Set<Role> roles = user.getRoles();
        if(checkIfUserIsAdmin(roles)){
            Role role = Role.builder()
                    .name(name)
                    .build();
          roleRepository.save(role);
          return JsonResponse.builder()
                    .message("Role created successfully")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .data(role)
                    .build();
        }else{
            return JsonResponse.builder()
                    .message("You are not authorized to create role")
                    .status(HttpStatus.UNAUTHORIZED)
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }
    }

    @Override
    public JsonResponse deleteRole(String name) {
        User user = userRepository.findByUsername(AppUtils.findAccountUsername()).orElseThrow(()->new RuntimeException("User not found"));
        Set<Role> roles = user.getRoles();
        if(checkIfUserIsAdmin(roles)){
            Role role = roleRepository.findByName(name).orElseThrow(()->new RuntimeException("Role not found"));
            roleRepository.delete(role);
            return JsonResponse.builder()
                    .message("Role deleted successfully")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }else{
            return JsonResponse.builder()
                    .message("You are not authorized to delete role")
                    .status(HttpStatus.UNAUTHORIZED)
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }

    }

    @Override
    public JsonResponse deleteRole(Long roleId) {
        User user = userRepository.findByUsername(AppUtils.findAccountUsername()).orElseThrow(()->new RuntimeException("User not found"));
        Set<Role> roles = user.getRoles();
        if(checkIfUserIsAdmin(roles)){
            Role role = roleRepository.findById(roleId).orElseThrow(()->new RuntimeException("Role not found"));
            roleRepository.delete(role);
            return JsonResponse.builder()
                    .message("Role deleted successfully")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build();
    }else{
            return JsonResponse.builder()
                    .message("You are not authorized to delete role")
                    .status(HttpStatus.UNAUTHORIZED)
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .build();}
    }

    @Override
    public JsonResponse updateRole(Long roleId, String name) {
        User user = userRepository.findByUsername(AppUtils.findAccountUsername()).orElseThrow(()->new RuntimeException("User not found"));
        Set<Role> roles = user.getRoles();
        if(checkIfUserIsAdmin(roles)){
            Role role = roleRepository.findById(roleId).orElseThrow(()->new RuntimeException("Role not found"));
            role.setName(name);
            roleRepository.save(role);
            return JsonResponse.builder()
                    .message("Role updated successfully")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .data(role)
                    .build();

    }else{
            return JsonResponse.builder()
                    .message("You are not authorized to update role")
                    .status(HttpStatus.UNAUTHORIZED)
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .build();}
    }

    @Override
    public JsonResponse getRoles() {
        List<Role> roles = roleRepository.findAll();
        return JsonResponse.builder()
                .message("Roles fetched successfully")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(roles)
                .build();
    }

    @Override
    public JsonResponse getRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(()->new RuntimeException("Role not found"));
        return JsonResponse.builder()
                .message("Role fetched successfully")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(role)
                .build();
    }

    private Boolean checkIfUserIsAdmin(Set<Role> roles){
       final Boolean[] isAdmin = {false};
        roles.forEach(role -> {
            if(role.getName().equals("ROLE_ADMIN")){
                isAdmin[0] = true;
            }
        });
        return isAdmin[0];
    }
}
