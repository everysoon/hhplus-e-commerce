package kr.hhplus.be.server.service;

import kr.hhplus.be.server.entity.Product;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class MockService {
    private final Map<Long,Integer> userPoint = new HashMap<>();
    private final List<Product> products = new ArrayList<>();
    private final List<Product> coupons = new ArrayList<>();
    private final List<Product> orders = new ArrayList<>();
}
