package com.agribridge.controller;

import com.agribridge.dto.FarmRequest;
import com.agribridge.dto.JsonResponse;
import com.agribridge.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/farm")
@RequiredArgsConstructor
public class FarmController {
    private final FarmService farmService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','FARMER')")
    public ResponseEntity<?> createNewFarm(@RequestBody FarmRequest farmRequest){
        JsonResponse response = farmService.createFarm(farmRequest);
        return ResponseEntity.ok(response);
    }

}
