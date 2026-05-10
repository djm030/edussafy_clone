package com.edussafy.clone.domain.board.dto.response;

import com.edussafy.clone.domain.board.domain.enums.BoardType;

public record BoardResponse(Long id, String name, String code, BoardType boardType, String description) {
}
