package hello.commerce.product;

import hello.commerce.product.dto.ProductListResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Override
    public ProductListResponseV1 getProducts(Pageable pageable) {
        return null;
    }
}
