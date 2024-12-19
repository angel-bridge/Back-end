package angel_bridge.angel_bridge_server.domain.blog.entity;

import angel_bridge.angel_bridge_server.domain.blog.dto.request.AdminBlogRequestDto;
import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "blog_link", nullable = false)
    private String blogLink;

    @Column(name = "start_text", nullable = false)
    private String startText;

    @Column(name = "blog_date", nullable = false)
    private LocalDate blogDate;

    public void update(AdminBlogRequestDto request) {
        this.title = request.title();
        this.blogLink = request.blogLink();
        this.startText = request.text();
        this.blogDate = request.blogDate();
    }

    @Builder
    public Blog(String title, String blogLink, String startText, LocalDate blogDate) {
        this.title = title;
        this.blogLink = blogLink;
        this.startText = startText;
        this.blogDate = blogDate;
    }
}
