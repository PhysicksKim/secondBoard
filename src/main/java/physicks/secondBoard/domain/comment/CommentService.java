package physicks.secondBoard.domain.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import physicks.secondBoard.domain.post.Post;

import java.util.List;

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
}
