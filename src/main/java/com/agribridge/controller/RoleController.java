package com.agribridge.controller;

import com.agribridge.dto.JsonResponse;
import com.agribridge.dto.RolePayload;
import com.agribridge.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<JsonResponse> createRole(RolePayload rolePayload){
        JsonResponse response = roleService.createRole(rolePayload.getName());
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<JsonResponse> deleteRole(@RequestBody RolePayload rolePayload){
        JsonResponse response = roleService.deleteRole(rolePayload.getName());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update/{roleId}/")
    public ResponseEntity<JsonResponse> updateRole(@RequestBody RolePayload rolePayload, @PathVariable Long roleId){
        JsonResponse response = roleService.updateRole(roleId, rolePayload.getName());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<JsonResponse> deleteRole(@PathVariable Long roleId){
        JsonResponse response = roleService.deleteRole(roleId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/{roleId}")
    public ResponseEntity<JsonResponse> getRole(@PathVariable Long roleId){
        JsonResponse response = roleService.getRole(roleId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<JsonResponse> getRoles(){
        JsonResponse response = roleService.getRoles();
        return ResponseEntity.ok(response);
    }
}
