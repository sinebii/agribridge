package com.agribridge.service;

import com.agribridge.dto.AccountUpdatePayload;
import com.agribridge.dto.JsonResponse;
import com.agribridge.dto.ResetPasswordPayload;

public interface AppUserService {
    JsonResponse resetPassword(ResetPasswordPayload resetPassword);
    JsonResponse updateUserRole(AccountUpdatePayload accountUpdatePayload);

}
