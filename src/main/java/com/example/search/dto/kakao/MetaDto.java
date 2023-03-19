package com.example.search.dto.kakao;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MetaDto {
    Integer total_count;
    Integer pageable_count;
    Boolean is_end;
}
