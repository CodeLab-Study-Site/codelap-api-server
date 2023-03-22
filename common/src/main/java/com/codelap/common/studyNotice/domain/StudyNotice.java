package com.codelap.common.studyNotice.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.CREATED;
import static com.codelap.common.support.Preconditions.require;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class StudyNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User leader;

    @ManyToMany
    private final List<User> readMembers = new ArrayList<>();

    private String title;

    private String contents;

    @ElementCollection
    private List<StudyNoticeFile> files;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    private final StudyNoticeStatus status = CREATED;

    private StudyNotice(String title, String contents, List<StudyNoticeFile> files) {
        this.title = title;
        this.contents = contents;
        this.files = files;
    }

    public static StudyNotice create(String title, String contents, List<StudyNoticeFile> files) {
        require(isNotBlank(title));
        require(isNotBlank(contents));

        return new StudyNotice(title, contents, files);
    }
}
