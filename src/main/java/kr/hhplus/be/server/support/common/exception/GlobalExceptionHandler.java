package kr.hhplus.be.server.support.common.exception;

import kr.hhplus.be.server.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CustomException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseApi<?> handleCompAttendCustomException(CustomException ex) {
		String message = ex.getMessage();
		logger.error("Client Error, processCode: {}, message: {}", message, ex);
		return new ResponseApi<>(false, message, null);
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseApi<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
		throws Exception {
		StringBuilder messageBuilder = new StringBuilder();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			messageBuilder.append(error.getDefaultMessage()).append(" ");
		});

		logger.error("The fields have an issue : {}", messageBuilder);
		logger.error("Error by : {}", ex.getMessage(), ex);
		return new ResponseApi<>(false, messageBuilder.toString(), null);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseApi<?> handleIllegalArgumentException(IllegalArgumentException ex) {
		logger.error("Client Error: {}", ex.getMessage(), ex);
		return new ResponseApi<>(false, ex.getMessage(), null);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseApi<?> handleNoResourceFoundException(NoResourceFoundException ex) {
		logger.error("hhplus Service Client Error: {}", ex.getMessage(), ex);

		return new ResponseApi<>(false, ex.getMessage(), null);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ResponseApi<?> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException ex) {
		logger.error("hhplus Service  Client Error: {}", ex.getMessage(), ex);
		return new ResponseApi<>(false, ex.getMessage(), null);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseApi<?> handleGlobalException(Exception ex) {
		logger.error("hhplus Service  Server Error: {}", ex.getMessage(), ex);
		return new ResponseApi<>(false, ex.getMessage(), null);
	}
}
