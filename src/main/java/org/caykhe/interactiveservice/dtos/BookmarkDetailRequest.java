package org.caykhe.interactiveservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkDetailRequest {
    private Integer targetId;
    private Boolean type;
}
