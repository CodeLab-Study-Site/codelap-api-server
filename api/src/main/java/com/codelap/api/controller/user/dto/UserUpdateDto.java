package com.codelap.api.controller.user.dto;

import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserTechStack;

import java.util.List;

public class UserUpdateDto {

    public record UserUpdateRequest(
            String name,
            UserUpdateRequestUserCareerDto career,
            List<UserTechStack> techStacks
    ) {
    }

    public record UserUpdateRequestUserCareerDto(
            String occupation,
            int year
    ) {
        public UserCareer toCareer() {
            return UserCareer.create(occupation, year);
        }
    }
}
