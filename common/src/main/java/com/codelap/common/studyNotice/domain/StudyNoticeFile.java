package com.codelap.common.studyNotice.domain;

import com.codelap.common.support.FileStandard;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codelap.common.support.Preconditions.require;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class StudyNoticeFile extends FileStandard {
    private String s3ImageURL;
    private String originalName;
    public static final String dirName = "studyNotice";

    @Override
    public StudyNoticeFile update(String s3ImageURL, String originalName) {
        require(isNotBlank(s3ImageURL));
        require(isNotBlank(originalName));

        this.s3ImageURL = s3ImageURL;
        this.originalName = originalName;

        return this;
    }

    public static FileStandard create() {
        return new StudyNoticeFile();
    }
}
