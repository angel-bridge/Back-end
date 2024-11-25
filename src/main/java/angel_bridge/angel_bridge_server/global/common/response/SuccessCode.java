package angel_bridge.angel_bridge_server.global.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    SUCCESS(HttpStatus.OK,1000, "요청에 성공하였습니다.");

    private HttpStatus httpStatus;
    private int code;
    private String message;

    SuccessCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
