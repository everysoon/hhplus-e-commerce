package kr.hhplus.be.server.exception;

import kr.hhplus.be.server.config.swagger.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    public CustomException(ErrorCode code) {
        super(code.getMessage());
    }
}
