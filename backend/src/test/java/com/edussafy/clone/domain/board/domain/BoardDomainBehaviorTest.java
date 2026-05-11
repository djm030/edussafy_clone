package com.edussafy.clone.domain.board.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.board.domain.entity.Board;
import com.edussafy.clone.domain.board.domain.entity.BoardCategory;
import com.edussafy.clone.domain.board.domain.entity.BoardComment;
import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.board.domain.enums.BoardType;
import com.edussafy.clone.domain.board.domain.enums.ContentType;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import org.junit.jupiter.api.Test;

class BoardDomainBehaviorTest {

    @Test
    void boardPost_defaults_counters_and_never_decreases_counts_below_zero() {
        BoardPost post = BoardPost.builder()
                .category(category(BoardType.NORMAL))
                .user(user(1L))
                .title("notice")
                .contentType(ContentType.HTML)
                .contentHtml("<p>body</p>")
                .build();

        assertThat(post.getViewCount()).isZero();
        assertThat(post.getLikeCount()).isZero();
        assertThat(post.getCommentCount()).isZero();
        assertThat(post.getHasAttachment()).isFalse();
        assertThat(post.getIsDeleted()).isFalse();

        post.decreaseLikeCount();
        post.decreaseCommentCount();

        assertThat(post.getLikeCount()).isZero();
        assertThat(post.getCommentCount()).isZero();

        post.increaseLikeCount();
        post.increaseCommentCount();
        post.increaseViewCount();

        assertThat(post.getLikeCount()).isEqualTo(1);
        assertThat(post.getCommentCount()).isEqualTo(1);
        assertThat(post.getViewCount()).isEqualTo(1);
    }

    @Test
    void boardComment_tracks_owner_updates_and_soft_delete_state() {
        BoardPost post = BoardPost.builder()
                .category(category(BoardType.ANONYMOUS))
                .user(user(1L))
                .title("question")
                .build();
        BoardComment comment = BoardComment.builder()
                .post(post)
                .user(user(2L))
                .content("first")
                .build();

        assertThat(comment.isWrittenBy(2L)).isTrue();
        assertThat(comment.isWrittenBy(3L)).isFalse();

        comment.update("edited");
        comment.softDelete();

        assertThat(comment.getContent()).isEqualTo("edited");
        assertThat(comment.getIsDeleted()).isTrue();
    }

    private BoardCategory category(BoardType type) {
        Board board = Board.builder().id(1L).name("board").code("board").boardType(type).build();
        return BoardCategory.builder().id(1L).board(board).name("general").code("general").build();
    }

    private User user(Long id) {
        return User.builder()
                .id(id)
                .email("user" + id + "@example.com")
                .password("encoded")
                .name("User " + id)
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
