package com.codelap.api.service.study;

import com.codelap.common.study.domain.*;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class DefaultStudyAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyAppService studyAppService;

    private User leader;
    private List<TechStack> techStackList;

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<Study> getStudies = studyRepository.findByLeader(leader)
                .stream()
                .filter(it -> it.getStatus() != DELETED)
                .collect(Collectors.toList());

        List<GetStudiesStudyDto> studies = studyAppService.getStudies(leader.getId());

        assertThat(studies.size()).isEqualTo(getStudies.size());
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(Java, Spring);

        for (int i = 0; i < 5; i++) {
            studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList));
        }

        Study study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
        study.setStatus(DELETED);

        studyRepository.save(study);
    }
}