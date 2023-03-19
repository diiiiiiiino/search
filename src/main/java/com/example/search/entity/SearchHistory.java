package com.example.search.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String keyword;
    Long searchHit;

    public void plusSearchHit(){
        searchHit++;
    }
}