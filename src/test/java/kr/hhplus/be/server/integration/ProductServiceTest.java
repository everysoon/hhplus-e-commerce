package kr.hhplus.be.server.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infra.cache.PopularProductRedisService;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.integration.common.TestBatchDataFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProductServiceTest extends BaseIntegrationTest {

	@Autowired
	private ProductRepository productRepository;
	private ProductService productService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private TestBatchDataFactory dataFactory;
	@Autowired
	private PopularProductRedisService popularProductRedisService;
	@BeforeAll
	public void init() {
		dataFactory = new TestBatchDataFactory(jdbcTemplate);
		productService = new ProductService(productRepository, popularProductRedisService);

		dataFactory.insertBulkProducts(1000);
	}

	@Test
	void bulkInitTest() {
		List<Product> products = productRepository.findAll();
		assertThat(products.size()).isEqualTo(1000);
	}

//	@Test
//	void 상품_조회_정상() {
//		/**
//		 *  productId : 1,
//		 *  category : PET_SUPPLIES,
//		 *  description : Eos recusandae eligendi odit.,
//		 *  price : 2185.00,
//		 *  productName : Heavy Duty Iron Lamp
//		 *  status : AVAILABLE,
//		 *  stock : 15
//		 */
//		//given
//		Long productId = 1L;
//		//when
//		Product product = productService.findById(productId);
//		//then
//		assertThat(product).isNotNull();
//		assertAll("쿠폰 정보 확인",
//			() -> assertThat(product.getCategory()).isEqualTo(Category.PET_SUPPLIES),
//			() -> assertThat(product.getDescription()).isEqualTo("Eos recusandae eligendi odit."),
//			() -> assertThat(product.getProductName()).isEqualTo("Heavy Duty Iron Lamp"),
//			() -> assertThat(product.getStatus()).isEqualTo(ProductStatus.AVAILABLE),
//			() -> assertThat(product.getStock()).isEqualTo(15)
//		);
//	}
//
//	@Test
//	void 상품_조회시_유효하지않은_상품아이디를_넣으면_Throws_NOT_EXIST_PRODUCT() {
//		Long productId = 10000L;
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			productService.findById(productId);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.NOT_EXIST_PRODUCT);
//	}
//
//	@Test
//	void 필터링_상품_조회시_카테고리_필터시_해당_카테고리만_조회된다() {
//		ProductCommand.FilterSearch command = ProductCommand.FilterSearch.of(
//			null,
//			Category.BABY.name(),
//			null,
//			null,
//			false
//		);
//		List<Product> products = productService.searchFilter(command);
//		assertThat(products.size()).isGreaterThan(0);
//		assertThat(products.stream().filter(p -> p.getCategory() == Category.BABY).toList()
//			.size()).isEqualTo(products.size());
//	}
//
//	@Test
//	void 필터링_상품_조회시_카테고리_필터시_카테고리가_enum에_존재하지않는다면_Throws_InvalidDataAccessApiUsageException() {
//		ProductCommand.FilterSearch command = ProductCommand.FilterSearch.of(
//			null,
//			"minsun",
//			null,
//			null,
//			false
//		);
//		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
//			productService.searchFilter(command);
//		});
//	}
//
//	@Test
//	void 필터링_상품_조회시_해당_이름으로_검색이_가능하다() {
//		ProductCommand.FilterSearch command = ProductCommand.FilterSearch.of(
//			"Small",
//			null,
//			null,
//			null,
//			false
//		);
//		List<Product> products = productService.searchFilter(command);
//		assertThat(products.size()).isGreaterThan(0);
//		assertThat(products.stream().map(Product::getProductName)
//			.filter(name -> name.contains("Small")).toList().size()).isEqualTo(products.size());
//	}
//
//	@Test
//	void 필터링_상품_조회시_정렬값으로_정렬이_가능하다() {
//		ProductCommand.FilterSearch command = ProductCommand.FilterSearch.of(
//			null,
//			null,
//			"CATEGORY",
//			null,
//			false
//		);
//		List<Product> products = productService.searchFilter(command);
//		assertNotNull(products);
//	}
//	@Test
//	void 필터링_상품_조회시_유효하지않은_정렬값이면_Throws_INVALID_SORTED_BY(){
//		ProductCommand.FilterSearch command = ProductCommand.FilterSearch.of(
//			null,
//			null,
//			"MINSOON",
//			null,
//			false
//		);
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			productService.searchFilter(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_SORTED_BY);
//	}
//	@Test
//	void 필터링_상품_조회시_유효하지않은_정렬순이면_Throws_INVALID_SORTED(){
//		ProductCommand.FilterSearch command = ProductCommand.FilterSearch.of(
//			null,
//			null,
//			null,
//			"null",
//			false
//		);
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			productService.searchFilter(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_SORTED);
//	}
//	@Test
//	void 최근3일간의_인기상품_조회시_판매량_내림차순으로_조회된다(){
//		ProductCommand.TopSelling command = new ProductCommand.TopSelling(null,null,null);
//		List<Product> products = productService.findPopularAll(command);
//		assertThat(products.size()).isGreaterThan(0);
//		assertThat(products.get(0).getCategory()).isEqualTo(Category.PET_SUPPLIES);
//		assertThat(products.get(1).getCategory()).isEqualTo(Category.SPORTS);
//		assertThat(products.get(2).getCategory()).isEqualTo(Category.BOOKS);
//
//	}

//	@Test
//	void 주문시_상품_재고가_주문량만큼_감소된다(){
//		// before - 15개
//		Product product = ProductTestFixture.create(1L);
//		List<OrderItem> orderItems = List.of(new OrderItem(1L,product,null,10, BigDecimal.valueOf(2185)));
//
//
//		List<Product> products = productService.decreaseStock(orderItems);
//
//		assertThat(products.size()).isGreaterThan(0);
//		assertThat(products.get(0).getStock()).isEqualTo(5);
//	}
}
