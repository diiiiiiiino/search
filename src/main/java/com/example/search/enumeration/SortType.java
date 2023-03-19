package com.example.search.enumeration;

import lombok.Getter;

@Getter
public enum SortType {
    ACCURACY("accuracy", "sim"),
    RECENCY("recency", "date");

    private String kakao;
    private String naver;
    SortType(String kakao, String naver){
        this.kakao = kakao;
        this.naver = naver;
    }
}
