package angel_bridge.angel_bridge_server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    public ExceptionCode exceptionCode;
}