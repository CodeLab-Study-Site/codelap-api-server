package com.codelap.api.controller.studyComment.dto;

public class StudyCommentCreateDto {
    public record StudyCommentCreateRequest(
            Long studyId,
            String message
    ) {
    }
}
