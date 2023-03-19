package com.example.search.dto;

import com.example.search.dto.kakao.DocumentDto;
import com.example.search.dto.kakao.MetaDto;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDto {
    MetaDto meta;
    List<DocumentDto> documents;
}
