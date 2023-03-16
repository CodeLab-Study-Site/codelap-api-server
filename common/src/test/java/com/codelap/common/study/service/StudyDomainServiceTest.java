package com.codelap.common.study.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
class StudyDomainServiceTest {

    @Autowired
    private StudyService studyService;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    private User leader;
    private Study study;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader);
        study = studyRepository.save(study);
    }

    @Test
    void 스터디_생성_성공() {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        studyService.create(leader.getId(), "팀", "설명", 4, NORMAL, period, needCareer);

        Study foundStudy = studyRepository.findAll().get(0);

        assertThat(foundStudy.getId()).isNotNull();
    }

    @Test
    void 스터디_수정_성공() {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        studyService.update(study.getId(), leader.getId(), "updateName", "updateInfo", 5, HARD, updatePeriod, updateNeedCareer);

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getName()).isEqualTo("updateName");
        assertThat(foundStudy.getMaxMembersSize()).isEqualTo(5);
        assertThat(foundStudy.getInfo()).isEqualTo("updateInfo");
        assertThat(foundStudy.getDifficulty()).isEqualTo(HARD);
        assertThat(foundStudy.getPeriod()).isSameAs(updatePeriod);
        assertThat(foundStudy.getNeedCareer()).isSameAs(updateNeedCareer);
    }

    @Test
    void 스터디_수정_실패__스터디의_리더가_아님() {
        StudyPeriod updatePeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer updateNeedCareer = StudyNeedCareer.create("직무", 1);

        UserCareer career = UserCareer.create("직무", 1);
        User fakeLeader = userRepository.save(User.create("name", 10, career, "abcd", "fakeLeader"));

        assertThatIllegalArgumentException().isThrownBy(() ->
                studyService.update(study.getId(), fakeLeader.getId(), "updateName", "updateInfo", 5, HARD, updatePeriod, updateNeedCareer)
        );
    }

    @Test
    void 스터디_멤버_추가_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = userRepository.save(User.create("name", 10, career, "abcd", "user"));

        studyService.addMember(study.getId(), user.getId(), leader.getId());

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getMembers()).contains(user);
    }

    @Test
    void 스터디_멤버_추가_실패__사용자가_스터디의_주인이_아님() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = userRepository.save(User.create("name", 10, career, "abcd", "user"));
        User fakeLeader = userRepository.save(User.create("name", 10, career, "abcd", "fakeLeader"));

        assertThatIllegalArgumentException().isThrownBy(() ->
                studyService.addMember(study.getId(), user.getId(), fakeLeader.getId())
        );
    }

    @Test
    void 스터디_진행_성공() {
        study.proceed(study, leader);

        assertThat(study.getStatus()).isEqualTo(IN_PROGRESS);
    }
}
