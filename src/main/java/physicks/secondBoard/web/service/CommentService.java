package physicks.secondBoard.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.comment.Comment;
import physicks.secondBoard.domain.comment.CommentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * CRUD
     * 댓글 추가
     * 댓글 조회
     * 댓글 수정
     * 댓글 삭제
     */

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public void saveReplyComment(Comment replyComment, Comment parentComment) {
        saveComment(replyComment);
        replyComment.setParentComment(parentComment);
    }

    public List<Comment> findReplyComments(Comment parentComment) {
        return commentRepository.findRepliesByParentComment(parentComment);
    }
}
