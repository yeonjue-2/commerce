package hello.commerce.product;

import hello.commerce.product.model.Product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    @DisplayName("상품 리스트를 페이징하여 반환한다")
    void getProducts_pagingSuccess() {
        // given
        Product p1 = Product.builder().productName("상품1").amount(10000).stock(10).build();
        Product p2 = Product.builder().productName("상품2").amount(20000).stock(20).build();
        List<Product> products = List.of(p1, p2);

        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> page = new PageImpl<>(products, pageable, products.size());

        given(productRepository.findAll(pageable)).willReturn(page);

        // when
        Page<Product> result = productService.getProducts(pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(p1, p2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }
}
