package angel_bridge.angel_bridge_server.domain.blog.repository;

import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BlogRepository extends JpaRepository<Long, Blog> {

    @Query("SELECT b FROM Blog b WHERE b.deletedAt IS NULL ORDER BY b.blogDate DESC")
    List<Blog> findLatestBlogs(Pageable pageable);
}
