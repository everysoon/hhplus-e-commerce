package kr.hhplus.be.server.application.product;

import java.util.List;
import kr.hhplus.be.server.application.product.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
	public ProductResponseDTO findById(Long productId){
		return null;
	}
	public List<ProductResponseDTO> findAll(){
		return null;
	}
	public List<ProductResponseDTO> findAllPopularProducts(){
		return null;
	}
}
