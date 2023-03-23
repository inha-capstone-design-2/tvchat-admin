package com.capstone.tvchat.api.board.domain.dto;

import com.capstone.tvchat.api.board.domain.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponse {
    private Long id;
    private String name;

    @Builder
    public BoardResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BoardResponse toResponse(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .build();
    }
}
