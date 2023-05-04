package com.codelap.common.studyView.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.studyView.domain.StudyView;
import com.codelap.common.studyView.domain.StudyViewRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StudyViewDomainServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyViewRepository studyViewRepository;

    @Autowired
    private StudyViewService studyViewService;


    private User leader;
    private Study study;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        List<StudyTechStack> techStackList = Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));

        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList));
    }

    @Test
    void 스터디_조회수_생성_성공() {
        studyViewService.create(study.getId(), "1.1.1.1");

        StudyView studyView = studyViewRepository.findAll().get(0);

        assertThat(studyView.getId()).isNotNull();
    }

    @Test
    void 스터디_조회수_생성_실패__동일한_IP() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            for (int i = 0; i < 2; i++) {
                studyViewService.create(study.getId(), "1.1.1.1");
            }
        });
    }
}