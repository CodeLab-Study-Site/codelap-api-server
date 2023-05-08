package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserActivateDto.UserActivateRequest;
import com.codelap.api.controller.user.dto.UserActivateDto.UserActivateRequestUserCareerDto;
import com.codelap.api.support.ApiTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserTechStack;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequest;
import static com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequestUserCareerDto;
import static com.codelap.api.support.HttpMethod.*;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.user.domain.UserStatus.ACTIVATED;
import static com.codelap.common.user.domain.UserStatus.DELETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class UserControllerTest extends ApiTest {

    @Test
    @WithUserDetails
    void 유저_활성화_성공() throws Exception {
        User user = prepareLoggedInUser();

        UserTechStack techStack = new UserTechStack(Java);
        UserActivateRequestUserCareerDto dto = new UserActivateRequestUserCareerDto("직무", 10);

        UserActivateRequest req = new UserActivateRequest("name", dto, List.of(techStack));

        setMockMvcPerform(POST, req, "/user/activate");

        assertThat(user.getStatus()).isEqualTo(ACTIVATED);
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getCareer().getOccupation()).isEqualTo(dto.occupation());
        assertThat(user.getCareer().getYear()).isEqualTo(dto.year());
        assertThat(user.getTechStacks().stream().map(UserTechStack::getTechStack))
                .containsExactly(techStack.getTechStack());
    }

    @Test
    @WithUserDetails
    void 유저_수정_성공() throws Exception {
        User user = prepareLoggedInActiveUser();

        UserTechStack techStack = new UserTechStack(Java);
        UserUpdateRequestUserCareerDto dto = new UserUpdateRequestUserCareerDto("직무", 10);

        UserUpdateRequest req = new UserUpdateRequest("updatedName", dto, List.of(techStack));

        setMockMvcPerform(POST, req, "/user/update");

        assertThat(user.getName()).isEqualTo("updatedName");
        assertThat(user.getCareer().getOccupation()).isEqualTo(dto.occupation());
        assertThat(user.getCareer().getYear()).isEqualTo(dto.year());
        assertThat(user.getTechStacks().stream().map(UserTechStack::getTechStack))
                .containsExactly(techStack.getTechStack());
    }

    @Test
    @WithUserDetails
    void 유저_삭제_성공() throws Exception {
        User user = prepareLoggedInActiveUser();

        setMockMvcPerform(DELETE, "/user", "user/delete");

        assertThat(user.getStatus()).isEqualTo(DELETED);
    }

    @Test
    @WithUserDetails
    void 유저_활성화_상태_체크() throws Exception {
        prepareLoggedInActiveUser();

        setMockMvcPerform(GET, jsonPath("$").value(true), "/user/is-activated");
    }
}
