package kr.hhplus.be.server.support.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import kr.hhplus.be.server.ResponseApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.CUSTOM_INTERNAL_SERVER_ERROR;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.CUSTOM_METHOD_NOT_ALLOWED;

@Configuration
@OpenAPIDefinition(
	info = @io.swagger.v3.oas.annotations.info.Info(
		title = "HHPlus E-Commerce Service API",
		version = "1.0.0"
	),
	servers = @io.swagger.v3.oas.annotations.servers.Server(
		url = "http://localhost:8223"
	)
)
public class SwaggerConfig {

	private static final List<ErrorCode> DEFAULT_ERROR_CODES = List.of(
		CUSTOM_INTERNAL_SERVER_ERROR,
		CUSTOM_METHOD_NOT_ALLOWED
	);

	@Bean
	public OperationCustomizer customizer() {
		return (operation, handlerMethod) -> {
			Optional.ofNullable(handlerMethod.getMethodAnnotation(SwaggerErrorExample.class))
				.ifPresent(
					swaggerErrorExample -> settingExamples(operation, swaggerErrorExample.value()));
			return operation;
		};
	}

	private void settingExamples(Operation operation, ErrorCode[] swaggerErrorCodes) {
		List<ErrorCode> errorCodes = new ArrayList<>(DEFAULT_ERROR_CODES);
		errorCodes.addAll(Arrays.asList(swaggerErrorCodes));

		Map<Integer, List<ExampleHolder>> groupedExamples = errorCodes.stream()
			.map(this::createExampleHolder)
			.collect(Collectors.groupingBy(ExampleHolder::getCode));

		addExamplesToResponses(operation.getResponses(), groupedExamples);
	}

	private ExampleHolder createExampleHolder(ErrorCode code) {
		return ExampleHolder.builder()
			.holder(createExample(code))
			.code(code.getStatusCode())
			.name(code.name())
			.build();
	}

	private Example createExample(ErrorCode type) {
		return new Example().value(
			new ResponseApi<>(false, type.getMessage(), new Object()));
	}

	private void addExamplesToResponses(ApiResponses responses,
		Map<Integer, List<ExampleHolder>> exampleHolders) {
		exampleHolders.forEach((status, holders) -> {
			MediaType mediaType = new MediaType();
			holders.forEach(holder -> mediaType.addExamples(holder.getName(), holder.getHolder()));

			responses.addApiResponse(status.toString(), new ApiResponse().content(
				new Content().addMediaType("application/json", mediaType)));
		});
	}
}
