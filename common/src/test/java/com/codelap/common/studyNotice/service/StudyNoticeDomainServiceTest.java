package com.codelap.common.studyNotice.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.DELETED;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StudyNoticeDomainServiceTest {

    @Autowired
    private StudyNoticeService studyNoticeService;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyNoticeRepository studyNoticeRepository;

    private User leader;
    private Study study;
    private StudyNoticeFile file;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        List<TechStack> techStackList = Arrays.asList(Java, Spring);

        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList));

        file = StudyNoticeFile.create("saved", "original", 10L);
    }

    @Test
    void 스터디_공지_생성_성공() {
        studyNoticeService.create(study.getId(), leader.getId(), "title", "contents", List.of(file));

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(studyNotice.getId()).isNotNull();
        assertThat(studyNotice.getTitle()).isEqualTo("title");
        assertThat(studyNotice.getContents()).isEqualTo("contents");
        assertThat(studyNotice.getFiles()).isNotNull();
    }

    @Test
    void 스터디_공지_생성_실패__리더가_아님() {
        UserCareer career = UserCareer.create("직무", 1);
        User fakeLeader = userRepository.save(User.create("fakeLeader", 10, career, "abcd", "fakeLeader"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeService.create(study.getId(), fakeLeader.getId(), "title", "contents", List.of(file)));
    }

    @Test
    void 스터디_공지_수정_성공() {
        StudyNotice studyNotice = studyNoticeService.create(study.getId(), leader.getId(), "title", "contents", List.of(file));

        studyNoticeService.update(studyNotice.getId(), leader.getId(), "updateTitle", "updatedContents", List.of(file));

        StudyNotice foundStudyNotice = studyNoticeRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudyNotice.getTitle()).isEqualTo("updateTitle");
        assertThat(foundStudyNotice.getContents()).isEqualTo("updatedContents");
        assertThat(foundStudyNotice.getFiles()).isNotNull();
    }

    @Test
    void 스터디_공지_수정_실패__리더가_아님() {
        StudyNotice studyNotice = studyNoticeService.create(study.getId(), leader.getId(), "title", "contents", List.of(file));

        UserCareer career = UserCareer.create("직무", 1);
        User fakeLeader = userRepository.save(User.create("fakeLeader", 10, career, "abcd", "fakeLeader"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeService.update(studyNotice.getId(), fakeLeader.getId(), "updateTitle", "updatedContents", List.of(file)));
    }

    @Test
    void 스터디_공지_삭제_성공() {
        StudyNotice studyNotice = studyNoticeService.create(study.getId(), leader.getId(), "title", "contents", List.of(file));

        studyNoticeService.delete(studyNotice.getId(), leader.getId());

        StudyNotice foundStudyNotice = studyNoticeRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudyNotice.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_공지_삭제_실패__리더가_아님() {
        StudyNotice studyNotice = studyNoticeService.create(study.getId(), leader.getId(), "title", "contents", List.of(file));

        UserCareer career = UserCareer.create("직무", 1);
        User fakeLeader = userRepository.save(User.create("fakeLeader", 10, career, "abcd", "fakeLeader"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeService.delete(studyNotice.getId(), fakeLeader.getId()));
    }
}