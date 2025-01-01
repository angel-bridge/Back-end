package angel_bridge.angel_bridge_server.domain.submission.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum AttendanceStatus {

    ON_TIME("출석"),
    LATE("지각"),
    ABSENT("결석"),
    PENDING("미제출");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }
}
