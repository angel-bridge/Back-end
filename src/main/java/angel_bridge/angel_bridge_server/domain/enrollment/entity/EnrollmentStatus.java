package angel_bridge.angel_bridge_server.domain.enrollment.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public enum EnrollmentStatus {

    SCHEDULED("수강예정"),
    IN_PROGRESS("수강중"),
    COMPLETED("수강완료");

    private final String description;

    EnrollmentStatus(String description) {
        this.description = description;
    }

    public static EnrollmentStatus getStatusFromEducationStartDate(
            LocalDate educationStartDate,
            LocalDate educationEndDate) {

        LocalDate now = LocalDate.now();
        if (now.isBefore(educationStartDate))
            return SCHEDULED;
        if (now.isAfter(educationEndDate))
            return COMPLETED;
        return IN_PROGRESS;
    }
}
