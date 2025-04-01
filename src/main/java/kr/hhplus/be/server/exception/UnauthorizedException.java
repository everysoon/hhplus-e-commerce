package kr.hhplus.be.server.exception;

import ai.univs.platform.compattendservice.enums.ProcessType;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String processCode;

    public UnauthorizedException(ProcessType processType) {
        super();
        this.processCode = processType.getProcessCode();
    }

    public UnauthorizedException(String processCode, String message) {
        super(message);
        this.processCode = processCode;
    }
}
