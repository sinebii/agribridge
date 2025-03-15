package com.agribridge.service.impl;

import com.agribridge.dto.FarmRequest;
import com.agribridge.dto.JsonResponse;
import com.agribridge.exception.CustomUserNotFoundException;
import com.agribridge.model.Farm;
import com.agribridge.model.User;
import com.agribridge.repository.FarmRepository;
import com.agribridge.repository.UserRepository;
import com.agribridge.service.FarmService;
import com.agribridge.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {
    private final UserRepository userRepository;
    private final FarmRepository farmRepository;
    @Override
    public JsonResponse createFarm(FarmRequest request) {
        User user = userRepository.findByUsername(AppUtils.findAccountUsername()).orElseThrow(()->new CustomUserNotFoundException(HttpStatus.NOT_FOUND,"User not found"));
        Farm farm = Farm.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(user)
                .state(request.getState())
                .streetName(request.getStreetName())
                .lga(request.getLga())
                .townOrVillage(request.getTownOrVillage())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .isApproved(false)
                .build();
        farmRepository.save(farm);
        return JsonResponse.builder()
                .message("Farm created, now waiting for approval from admin")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }

    @Override
    public List<Farm> getApprovedFarms() {
        return null;
    }
}
