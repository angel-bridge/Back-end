package angel_bridge.angel_bridge_server.domain.banner.entity;

import angel_bridge.angel_bridge_server.domain.banner.dto.request.AdminBannerRequestDto;
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

    @Column(name = "banner_image", nullable = false)
    private String bannerImage;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "is_post", nullable = false)
    private Boolean isPost;

    public void changeIsPost() {
        this.isPost = !this.isPost;
    }

    public void update(AdminBannerRequestDto request, String file) {
        this.bannerImage = file;
        this.name = request.name();
        this.priority = request.priority();
        this.isPost = request.isPost();
    }

    @Builder
    public Banner(String bannerImage, String name, Integer priority, Boolean isPost) {
        this.bannerImage = bannerImage;
        this.name = name;
        this.priority = priority;
        this.isPost = isPost;
    }
}
