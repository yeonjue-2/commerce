package hello.commerce.product;

import hello.commerce.product.model.Product;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface ProductService {

    /**
     * 상품 목록 조회
     *
     * @param pageable
     * @return 상품 목록 응답
     */
    Page<Product> getProducts(Pageable pageable);

}