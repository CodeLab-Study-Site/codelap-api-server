package com.codelap.api.controller.studyConfirmation;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;

import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationConfirmDto.StudyConfirmationConfirmRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequestFileDto;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.CONFIRMED;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyConfirmationControllerTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyConfirmationRepository studyConfirmationRepository;

    private User leader;
    private User member;
    private Study study;


    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader));

        member = userRepository.save(User.create("candidate", 10, career, "abcd", "email"));

        study.addMember(member);
    }

    @Test
    void 스터디_인증_생성_성공() throws Exception {
        StudyConfirmationCreateRequestFileDto file = new StudyConfirmationCreateRequestFileDto("savedName", "originalName", 100L);
        StudyConfirmationCreateRequest req = new StudyConfirmationCreateRequest(study.getId(), member.getId(), "title", "content", List.of(file));

        mockMvc.perform(post("/study-confirmation")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-confirmation/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        assertThat(studyConfirmation.getId()).isNotNull();
        assertThat(studyConfirmation.getStudy()).isSameAs(study);
        assertThat(studyConfirmation.getUser()).isSameAs(member);
        assertThat(studyConfirmation.getTitle()).isEqualTo("title");
        assertThat(studyConfirmation.getContent()).isEqualTo("content");
        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
        assertThat(studyConfirmation.getCreatedAt()).isNotNull();
    }

    @Test
    void 스터디_인증_확인_성공() throws Exception {
        StudyConfirmationFile file = StudyConfirmationFile.create("savedName", "originalName", 100L);

        studyConfirmationRepository.save(StudyConfirmation.create(study, member, "title", "content", List.of(file)));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        StudyConfirmationConfirmRequest req = new StudyConfirmationConfirmRequest(studyConfirmation.getId(), leader.getId());

        mockMvc.perform(post("/study-confirmation/confirm")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-confirmation/confirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(studyConfirmation.getStatus()).isEqualTo(CONFIRMED);
    }
}