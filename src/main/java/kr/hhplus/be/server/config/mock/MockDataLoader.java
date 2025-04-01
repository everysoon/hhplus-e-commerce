package kr.hhplus.be.server.config.mock;

import kr.hhplus.be.server.repository.CouponRepository;
import kr.hhplus.be.server.repository.ProductRepository;
import kr.hhplus.be.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MockDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

    }
}
