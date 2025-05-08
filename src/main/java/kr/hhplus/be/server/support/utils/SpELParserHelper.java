package kr.hhplus.be.server.support.utils;

import java.lang.reflect.Method;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpELParserHelper {
	private static final ExpressionParser parser = new SpelExpressionParser();
	private static final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

	public static String[] getParameterNames(Method method) {
		return discoverer.getParameterNames(method);
	}
	public static String parseExpression(String expression, Method method, Object[] args) {
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 파라미터 이름을 기반으로 변수 등록
		String[] paramNames = getParameterNames(method);

		if (paramNames != null) {
			for (int i = 0; i < paramNames.length; i++) {
				context.setVariable(paramNames[i], args[i]);
			}
		}

		return parser.parseExpression(expression).getValue(context, String.class);
	}
}
