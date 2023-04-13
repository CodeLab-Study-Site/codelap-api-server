package com.codelap.common.bookmark.service;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkDomainService implements BookmarkService{

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    @Override
    public void create(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Bookmark bookmark = bookmarkRepository.save(Bookmark.create(study, user));

        study.addBookmark(bookmark);
    }
}