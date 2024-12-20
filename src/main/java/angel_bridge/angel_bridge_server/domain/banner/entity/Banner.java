package angel_bridge.angel_bridge_server.domain.banner.entity;

import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Banner SET deleted_at = NOW() where banner_id = ?")
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "is_post", nullable = false)
    private Boolean isPost;

    @Builder
    public Banner(String imageUrl, String name, Integer priority, Boolean isPost) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.priority = priority;
        this.isPost = isPost;
    }
}
