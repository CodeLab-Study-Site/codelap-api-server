package com.codelap.common.study.service;

import com.codelap.common.study.domain.StudyDifficulty;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import org.springframework.scheduling.annotation.Scheduled;

public interface StudyService {

    void create(Long leaderId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer);

    void update(Long studyId, Long userId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer);

    void addMember(Long studyId, Long userId, Long leaderId);

    void proceed(Long studyId, Long userId);

    void removeMember(Long studyId, Long memberId, Long leaderId);

    void close(Long studyId, Long leaderId);

    void leave(Long studyId, Long memberId);
}
