package com.codelap.common.study.domain;

import com.codelap.common.support.TechStack;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class StudyTechStack {

    @Enumerated(STRING)
    private TechStack techStack;

}
