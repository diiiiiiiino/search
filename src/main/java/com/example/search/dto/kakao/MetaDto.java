package com.example.search.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaDto {
    private Integer total_count;
    private Integer pageable_count;
    private Boolean is_end;
}
