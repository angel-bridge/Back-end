package angel_bridge.angel_bridge_server.domain.assignment.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum AssignmentStatus {
    PAST("과거회차"),
    CURRENT("오늘회차"),
    UPCOMING("공개예정회차");

    private final String description;

    AssignmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
