package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {


}
