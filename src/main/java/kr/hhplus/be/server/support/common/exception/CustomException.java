package kr.hhplus.be.server.support.common.exception;

import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private ErrorCode errorCode;
    public CustomException(ErrorCode code) {
		super(code.getMessage());
		this.errorCode = code;
    }
}
