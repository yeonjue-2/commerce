package hello.commerce.product;

import hello.commerce.common.request.PageRequestDto;
import hello.commerce.product.dto.ProductListResponseDtoV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    @GetMapping("/v1/products")
    public ResponseEntity<ProductListResponseDtoV1> getProducts(
            @ModelAttribute PageRequestDto pageRequestDto
    ) {
        Pageable pageable = pageRequestDto.toPageable();
        // TO-D0 상품 목록 조회 서비스 호출
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
