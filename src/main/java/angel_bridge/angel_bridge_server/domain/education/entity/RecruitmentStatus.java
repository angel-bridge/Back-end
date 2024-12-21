package angel_bridge.angel_bridge_server.domain.education.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum RecruitmentStatus {

    UPCOMING("모집예정"),
    ONGOING("모집중"),
    CLOSED("모집종료");

    private final String description;

    RecruitmentStatus(String description) {
        this.description = description;
    }
}

