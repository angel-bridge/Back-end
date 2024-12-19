package angel_bridge.angel_bridge_server.domain.education.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum RecruitmentStatus {

    UPCOMING("UPCOMING"),
    ONGOING("ONGOING"),
    CLOSED("CLOSED");

    private final String description;

    RecruitmentStatus(String description) {
        this.description = description;
    }
}
