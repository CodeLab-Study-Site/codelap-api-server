package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.support.TechStack;

import java.util.List;

public interface StudyAppService {

    List<GetStudiesStudyDto> getStudies(Long userId);

    List<GetStudiesCardDto.GetStudyInfo> getAttendedStudiesByUser(Long userId, String statusCond, List<TechStack> techStackList);

    public List<GetOpenedStudiesDto> getOpenedStudies();
}
