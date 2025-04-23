package kr.hhplus.be.server.support;

import kr.hhplus.be.server.infra.web.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	@Bean
	public FilterRegistrationBean<LoggingFilter> loggingFilter() {
		FilterRegistrationBean<LoggingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new LoggingFilter());
		registration.addUrlPatterns("/**");
		return registration;
	}
}
