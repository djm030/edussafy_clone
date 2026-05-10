package com.edussafy.clone.domain.board.domain.entity;

import com.edussafy.clone.domain.board.domain.enums.BoardType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false, length = 50)
    private BoardType boardType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public Board(Long id, String name, String code, BoardType boardType, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.boardType = boardType;
        this.description = description;
    }

    public boolean isAnonymous() {
        return boardType == BoardType.ANONYMOUS;
    }

    public boolean isAdminWritable() {
        return boardType == BoardType.NOTICE || boardType == BoardType.FAQ || boardType == BoardType.ACADEMIC_RULE;
    }

    public void update(String name, String code, BoardType boardType, String description) {
        this.name = name;
        this.code = code;
        this.boardType = boardType;
        this.description = description;
    }
}
