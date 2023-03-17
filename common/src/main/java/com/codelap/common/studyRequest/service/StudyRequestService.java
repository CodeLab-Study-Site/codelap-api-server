package com.codelap.common.studyRequest.service;

public interface StudyRequestService {

    void create(Long userId, Long studyId, String message);

    void approve(Long studyRequestId, Long leaderId);

    void cancel(Long studyRequestId, Long userId);
}
