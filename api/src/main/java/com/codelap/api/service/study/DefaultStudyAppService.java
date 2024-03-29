package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.StudyFile;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.integration.s3.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codelap.common.study.domain.StudyFile.create;
import static com.codelap.common.study.domain.StudyFile.dirName;


@Service
@Transactional
@RequiredArgsConstructor
public class DefaultStudyAppService implements StudyAppService {

    private final StudyQueryAppService studyQueryAppService;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FileUpload fileUpload;

    @Override
    public List<GetStudiesStudyDto> getStudies(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return studyQueryAppService.getStudies(user);
    }

    @Override
    public List<GetStudyInfo> findStudyCardsByCond(Long userId, String statusCond, List<TechStack> techStackList) {
        User user = userRepository.findById(userId).orElseThrow();

        List<GetStudyInfo> allStudies = studyQueryAppService.findStudyCardsByCond(user, statusCond, techStackList);

        Map<Long, List<GetTechStackInfo>> techStacksMap = studyQueryAppService.getTechStacks(toStudyIds(allStudies))
                .stream()
                .collect(Collectors
                        .groupingBy(GetTechStackInfo::getStudyId));

        allStudies.forEach(study -> study.setTechStackList(techStacksMap.get(study.getStudyId())));

        return allStudies;
    }

    @Override
    public List<GetStudyInfo> getBookmarkedStudiesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<Long> studyIds = bookmarkRepository.findByUser(user)
                .stream()
                .map(DefaultStudyAppService::getId)
                .collect(Collectors.toList());

        return getGetStudyInfo(studyQueryAppService.getBookmarkedStudiesByUser(studyIds));
    }

    private static List<Long> toStudyIds(List<GetStudyInfo> allStudies) {
        return allStudies
                .stream()
                .map(GetStudyInfo::getStudyId)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetOpenedStudiesDto> getOpenedStudies() {
        return studyQueryAppService.getOpenedStudies();
    }

    @Override
    public StudyFile imageUpload(MultipartFile multipartFile) {
        return (StudyFile) fileUpload.upload(multipartFile, dirName, create());
    }

    private static Long getId(Bookmark bookmark) {
        return bookmark.getStudy().getId();
    }

    private List<GetStudyInfo> getGetStudyInfo(List<GetStudyInfo> allStudies) {
        Map<Long, List<GetTechStackInfo>> techStacksMap = studyQueryAppService.getTechStacks(toStudyIds(allStudies))
                .stream()
                .collect(Collectors
                        .groupingBy(GetTechStackInfo::getStudyId));

        allStudies.forEach(study -> study.setTechStackList(techStacksMap.get(study.getStudyId())));

        return allStudies;
    }
}
