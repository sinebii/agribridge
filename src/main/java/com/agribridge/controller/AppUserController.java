package com.agribridge.controller;

import com.agribridge.dto.AccountUpdatePayload;
import com.agribridge.dto.JsonResponse;
import com.agribridge.dto.ResetPasswordPayload;
import com.agribridge.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app-user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordPayload resetPassword){
        JsonResponse response = appUserService.resetPassword(resetPassword);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-user-role")
    public ResponseEntity<JsonResponse> updateUserRole(@RequestBody AccountUpdatePayload accountUpdatePayload){
        JsonResponse response = appUserService.updateUserRole(accountUpdatePayload);
        return ResponseEntity.ok(response);
    }

}
