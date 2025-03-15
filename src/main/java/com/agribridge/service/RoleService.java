package com.agribridge.service;


import com.agribridge.dto.JsonResponse;

public interface RoleService {
    JsonResponse createRole(String name);
    JsonResponse deleteRole(String name);

    JsonResponse deleteRole(Long roleId);

    JsonResponse updateRole(Long roleId, String name);

    JsonResponse getRoles();

    JsonResponse getRole(Long roleId);
}
