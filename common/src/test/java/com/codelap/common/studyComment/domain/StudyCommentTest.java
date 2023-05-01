package com.codelap.common.studyComment.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyTechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static com.codelap.common.studyComment.domain.StudyCommentStatus.CREATED;
import static com.codelap.common.studyComment.domain.StudyCommentStatus.DELETED;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

class StudyCommentTest {

    private User leader;
    private Study study;

    private StudyPeriod period;
    private StudyNeedCareer needCareer;
    private List<StudyTechStack> techStackList;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = User.create("name", 10, career, "abcd", "setUp");

        period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));

        study = create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
    }

    @Test
    void 스터디_댓글_생성_성공() {
        StudyComment studyComment = StudyComment.create(study, leader, "댓글");

        assertThat(studyComment.getComment()).isEqualTo("댓글");
        assertThat(studyComment.getUser()).isSameAs(leader);
        assertThat(studyComment.getStatus()).isEqualTo(CREATED);
        assertThat(studyComment.getCreateAt()).isNotNull();
    }

    @Test
    void 스터디_댓글_생성_실패__스터디가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyComment.create(null, leader, "댓글"));
    }

    @Test
    void 스터디_댓글_생성_실패__유저가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyComment.create(study, null, "댓글"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_댓글_생성_실패__댓글이_널이거나_공백(String comment) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyComment.create(study, leader, comment));
    }

    @Test
    void 스터디_댓글_수정_성공() {
        StudyComment studyComment = StudyComment.create(study, leader, "댓글");

        studyComment.update("updatedComment");

        assertThat(studyComment.getComment()).isEqualTo("updatedComment");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_댓글_수정_실패__댓글이_널이거나_공백(String comment){
        StudyComment studyComment = StudyComment.create(study, leader, "댓글");

        assertThatIllegalArgumentException().isThrownBy(() -> studyComment.update(comment));
    }

    @Test
    void 스터디_댓글_삭제_성공() {
        StudyComment studyComment = StudyComment.create(study, leader, "댓글");

        studyComment.delete();

        assertThat(studyComment.getStatus()).isEqualTo(DELETED);
    }

    @ParameterizedTest
    @EnumSource(value = StudyCommentStatus.class, names = {"DELETED"}, mode = INCLUDE)
    void 스터디_댓글_삭제_실패__이미_삭제된_상태(StudyCommentStatus status) {
        StudyComment studyComment = StudyComment.create(study, leader, "댓글");

        studyComment.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyComment.delete());
    }
}