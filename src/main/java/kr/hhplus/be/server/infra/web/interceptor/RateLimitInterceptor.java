package kr.hhplus.be.server.infra.web.interceptor;

//@Component
//public class RateLimitInterceptor implements HandlerInterceptor {
//	private final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);
//	private final Map<String, RequestCounter> ipRequestMap = new ConcurrentHashMap<>();
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		String ip = request.getRemoteAddr();
//		logger.info("### preHandle IP : {}", ip);
//		RequestCounter counter = ipRequestMap.computeIfAbsent(ip, k -> new RequestCounter());
//
//		if (counter.incrementAndCheckLimit()) {
//			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//			return false;
//		}
//		return true;
//	}
//
//	static class RequestCounter {
//		private static final int MAX_REQUESTS_PER_MINUTE = 100;
//		private int count;
//		private long lastResetTime;
//
//		RequestCounter() {
//			this.count = 0;
//			this.lastResetTime = System.currentTimeMillis();
//		}
//
//		synchronized boolean incrementAndCheckLimit() {
//			long now = System.currentTimeMillis();
//			if (now - lastResetTime > 60_000) { // 1분 지났나
//				count = 0;
//				lastResetTime = now;
//			}
//			return ++count > MAX_REQUESTS_PER_MINUTE;
//		}
//	}
//}
