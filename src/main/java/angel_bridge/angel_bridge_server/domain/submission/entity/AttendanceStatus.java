package angel_bridge.angel_bridge_server.domain.submission.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum AttendanceStatus {

    ON_TIME("ONTIME"),
    LATE("LATE"),
    ABSENT("ABSENT"),
    PENDING("PENDING");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }
}
