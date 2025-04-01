package kr.hhplus.be.server.exception;


import kr.hhplus.be.server.config.swagger.ErrorCode;
import lombok.Getter;

@Getter
public class ValidationCustomException extends RuntimeException {
    private final String processCode;

    public ValidationCustomException(ErrorCode erroCode) {
        super();
        this.processCode = erroCode.getProcessCode();
    }

    public ValidationCustomException(String processCode, String message) {
        super(message);
        this.processCode = processCode;
    }
}
