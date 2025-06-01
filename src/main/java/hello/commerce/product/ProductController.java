package hello.commerce.product;

import hello.commerce.common.request.PageRequestDto;
import hello.commerce.product.dto.ProductListResponseV1;
import hello.commerce.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/v1/products")
    public ResponseEntity<ProductListResponseV1> getProducts(
            @Validated @ModelAttribute PageRequestDto pageRequestDto
    ) {
        Pageable pageable = pageRequestDto.toPageable();
        Page<Product> products = productService.getProducts(pageable);

        ProductListResponseV1 productListResponseV1 = ProductListResponseV1.fromEntities(products);

        return ResponseEntity.ok(productListResponseV1);
    }
}
