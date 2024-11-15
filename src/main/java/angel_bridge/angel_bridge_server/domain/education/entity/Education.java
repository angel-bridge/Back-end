package angel_bridge.angel_bridge_server.domain.education.entity;

import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Education extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id", nullable = false)
    private Long id;

    @Column(name = "program_preview_image")
    private String programPreImage;

    @Column(name = "education_preview_image")
    private String educationPreImage;

    @Column(name = "program_description")
    private String programDescription;

    @Column(name = "creator_biography")
    private String creatorBiography;

}
