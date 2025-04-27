package hello.commerce.product;

import hello.commerce.product.dto.ProductListResponseV1;

import java.awt.print.Pageable;

public interface ProductService {

    /**
     * 상품 목록 조회
     *
     * @param pageable
     * @return 상품 목록 응답
     */
    ProductListResponseV1 getProducts(Pageable pageable);

}