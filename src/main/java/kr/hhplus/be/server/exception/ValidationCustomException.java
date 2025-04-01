package kr.hhplus.be.server.exception;

import ai.univs.platform.compattendservice.enums.ProcessType;
import lombok.Getter;

@Getter
public class ValidationCustomException extends RuntimeException {
    private final String processCode;

    public ValidationCustomException(ProcessType processType) {
        super();
        this.processCode = processType.getProcessCode();
    }

    public ValidationCustomException(String processCode, String message) {
        super(message);
        this.processCode = processCode;
    }
}
