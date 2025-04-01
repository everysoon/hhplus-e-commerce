package kr.hhplus.be.server.service;

import kr.hhplus.be.server.entity.Coupon;
import kr.hhplus.be.server.entity.Order;
import kr.hhplus.be.server.entity.Product;
import kr.hhplus.be.server.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class MockService {

    private final List<Product> products = new ArrayList<>();
    private final List<Coupon> coupons = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    private List<Product>
}
