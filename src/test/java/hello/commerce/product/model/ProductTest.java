package hello.commerce.product.model;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("정상적으로 재고가 감소해야 한다")
    void decreaseStock_success() {
        // given
        Product product = createProduct(101L);

        // when
        product.decreaseStock(3);

        // then
        assertThat(product.getStock()).isEqualTo(297);
    }

    @Test
    @DisplayName("재고가 부족하면 예외를 던져야 한다")
    void decreaseStock_insufficientStock() {
        // given
        Product product = createProduct(101L);

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(301))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.INSUFFICIENT_STOCK.getMessage());
    }

    private Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .productName("향균 베개 커버")
                .amount(25000)
                .stock(300)
                .build();
    }
}
