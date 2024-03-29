package com.project.zipkok.common.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남자"), FEMALE("여자"), UNDISCLOSED("비공개");

    private String description;

    Gender(String description) {
        this.description = description;
    }

}
