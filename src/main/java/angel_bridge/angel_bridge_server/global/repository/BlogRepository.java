package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findByIdAndDeletedAtIsNull(Long blogId);

    @Query("SELECT b FROM Blog b WHERE b.deletedAt IS NULL ORDER BY b.blogDate DESC")
    List<Blog> findLatestBlogs(Pageable pageable);
}
