package com.agribridge.service;

import com.agribridge.dto.FarmRequest;
import com.agribridge.dto.JsonResponse;
import com.agribridge.model.Farm;

import java.util.List;

public interface FarmService {
    JsonResponse createFarm(FarmRequest request);
    List<Farm> getApprovedFarms();
}
