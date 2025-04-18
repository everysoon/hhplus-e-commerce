package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.infra.product.entity.Category;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Product {

	private final Long id;
	private String productName;
	private Integer stock;
	private final Category category;
	private String description;
	private BigDecimal price;
	private ProductStatus status;
	private LocalDateTime createdAt;

	public void decreaseStock(Integer amount) {
		if(this.stock <= 0){
			throw new CustomException(ErrorCode.OUT_OF_STOCK);
		}
		this.stock -= amount;
		if(this.stock == 0){
			this.status = ProductStatus.OUT_OF_STOCK;
		}
	}
	public void increaseStock(Integer amount) {
		this.stock += amount;
	}
	public void validateOrderable(){
		if(this.status ==ProductStatus.OUT_OF_STOCK || this.stock <=0){
			throw new CustomException(ErrorCode.OUT_OF_STOCK);
		}
	}
}
