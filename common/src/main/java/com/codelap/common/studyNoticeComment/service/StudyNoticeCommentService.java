package com.codelap.common.studyNoticeComment.service;

import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;

public interface StudyNoticeCommentService {

    StudyNoticeComment create(Long studyNoticeId, Long userId, String content);

    void delete(Long StudyNoticeCommentId, Long userId);

    void update(Long StudyNoticeCommentId, Long userId, String content);
}
