package hello.commerce.product;

import hello.commerce.common.request.PageRequestDto;
import hello.commerce.product.dto.ProductListResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    @GetMapping
    public ResponseEntity<ProductListResponseV1> getProducts(
            @ModelAttribute PageRequestDto pageRequestDto
    ) {
        // TO-DO 상품 목록 조회 서비스 호출
        return ResponseEntity.ok().body(null);
    }
}
