package com.codelap.common.studyConfirmation.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codelap.common.support.Preconditions.actorValidate;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyConfirmationDomainService implements StudyConfirmationService {

    private final StudyConfirmationRepository studyConfirmationRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    @Override
    public void create(Long studyId, Long userId, String title, String content, List<StudyConfirmationFile> files) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        actorValidate(study.containsMember(user));

        StudyConfirmation studyConfirmation = StudyConfirmation.create(study, user, title, content, files);

        studyConfirmationRepository.save(studyConfirmation);
    }

    @Override
    public void confirm(Long studyConfirmId, Long leaderId) {
        StudyConfirmation studyConfirmation = studyConfirmationRepository.findById(studyConfirmId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();

        actorValidate(studyConfirmation.isLeader(leader));

        studyConfirmation.confirm();
    }

    @Override
    public void reject(Long studyConfirmId, Long leaderId) {
        StudyConfirmation studyConfirmation = studyConfirmationRepository.findById(studyConfirmId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();

        actorValidate(studyConfirmation.isLeader(leader));

        studyConfirmation.reject("부적합");
    }

    @Override
    public void reConfirm(Long studyConfirmId, Long userId, String title, String content, List<StudyConfirmationFile> files) {
        StudyConfirmation studyConfirmation = studyConfirmationRepository.findById(studyConfirmId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        actorValidate(studyConfirmation.isUser(user));

        studyConfirmation.reConfirm(title, content, files);
    }

    @Override
    public void delete(Long studyConfirmId, Long userId) {
        StudyConfirmation studyConfirmation = studyConfirmationRepository.findById(studyConfirmId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        actorValidate(studyConfirmation.isUser(user));

        studyConfirmation.delete();
    }
}
