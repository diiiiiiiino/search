package com.example.search.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoSearchResponseDto {
    private MetaDto meta;
    private List<DocumentDto> documents;
}
