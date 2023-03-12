package com.codelap.common.study.service;

import com.codelap.common.study.domain.StudyDifficulty;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.user.domain.User;

public interface StudyService {

    void create(Long leaderId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer);

    void update(Long studyId, Long userId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer);
}
