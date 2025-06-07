package kr.hhplus.be.server.infra.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoggingFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 요청과 응답 래핑
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

		long startTime = System.currentTimeMillis();

		try {
			chain.doFilter(requestWrapper, responseWrapper); // 실제 요청 처리
		} finally {
			long duration = System.currentTimeMillis() - startTime;
			logRequest(requestWrapper);
			logResponse(responseWrapper, duration);
			responseWrapper.copyBodyToResponse();
		}
	}

	private void logRequest(ContentCachingRequestWrapper request) throws IOException {
		String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
		logger.info("▶️ [Request] {} {} | Params: {} | Body: {}",
			request.getMethod(),
			request.getRequestURI(),
			request.getQueryString(),
			body
		);
	}

	private void logResponse(ContentCachingResponseWrapper response, long duration) throws IOException {
		String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
		logger.info("✅ [Response] Status: {} | Duration: {}ms | Body: {}",
			response.getStatus(),
			duration,
			responseBody
		);
	}
}
