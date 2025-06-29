package hello.commerce.product;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.product.model.Product;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductReaderTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductReader productReader;

    @Test
    @DisplayName("상품이 존재하면 반환한다")
    void findByIdForUpdate_success() {
        // given
        Product product = Product.builder()
                .id(1L)
                .productName("상품")
                .amount(10000)
                .stock(5)
                .build();

        when(productRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(product));

        // when
        Product result = productReader.findByIdForUpdate(1L);

        // then
        assertThat(result).isEqualTo(product);
    }

    @Test
    @DisplayName("상품이 존재하지 않으면 NOT_FOUND_PRODUCT 예외를 던진다")
    void findByIdForUpdate_notFound() {
        when(productRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productReader.findByIdForUpdate(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("PessimisticLockException 발생하면 LOCK_TIMEOUT_PRODUCT 예외를 던진다")
    void findByIdForUpdate_pessimisticLock() {
        when(productRepository.findByIdForUpdate(1L))
                .thenThrow(new PessimisticLockException(null, null, null));

        assertThatThrownBy(() -> productReader.findByIdForUpdate(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.LOCK_TIMEOUT_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("락 타임아웃이 발생하면 LOCK_TIMEOUT_PRODUCT 예외를 던진다")
    void findByIdForUpdate_lockTimeout() {
        when(productRepository.findByIdForUpdate(1L))
                .thenThrow(new LockTimeoutException(null, null, null));

        assertThatThrownBy(() -> productReader.findByIdForUpdate(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.LOCK_TIMEOUT_PRODUCT.getMessage());
    }
}
