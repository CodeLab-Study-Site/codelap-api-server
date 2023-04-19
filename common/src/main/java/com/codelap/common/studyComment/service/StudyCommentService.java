package com.codelap.common.studyComment.service;

import com.codelap.common.studyComment.domain.StudyComment;

public interface StudyCommentService {
    StudyComment create(Long studyId, Long userId, String message);

    void update(Long studyCommentId, Long userId, String comment);
}
