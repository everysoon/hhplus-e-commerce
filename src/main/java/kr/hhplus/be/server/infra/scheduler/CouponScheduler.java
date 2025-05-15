package kr.hhplus.be.server.infra.scheduler;

import kr.hhplus.be.server.application.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponScheduler {
	private final CouponService couponService;

	@Scheduled(cron = "0 0 0 * * *")
	public void expireCoupons(){
		couponService.scheduleExpireCoupons();
	}
	@Scheduled(cron = "0 0 0 * * *")
	public void notifyUserBeforeExpireDay(){
		couponService.scheduleNotifyUserBeforeExpireDay();
	}
}
