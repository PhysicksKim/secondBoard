package physicks.secondBoard.devinit;

import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import physicks.secondBoard.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostDevRepository extends JpaRepository<Post, Long> {

    List<Post> findAll(Sort sort);

    @Modifying
    @Query("update Post p set p.createdTime = :createdTime, p.lastUpdatedTime = :lastUpdatedTime where p.id = :id")
    void updateTimestamps(@Param("id") Long id, @Param("createdTime") LocalDateTime createdTime, @Param("lastUpdatedTime") LocalDateTime lastUpdatedTime);
}