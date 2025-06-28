package hello.commerce.order;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.order.model.Order;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderReaderTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderReader orderReader;

    @Test
    @DisplayName("주문이 정상적으로 조회되면 반환한다")
    void findByIdForUpdate_success() {
        // given
        Order order = mock(Order.class);
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));

        // when
        Order result = orderReader.findByIdForUpdate(1L);

        // then
        assertThat(result).isEqualTo(order);
    }

    @Test
    @DisplayName("주문이 존재하지 않으면 NOT_FOUND_ORDER 예외를 던진다")
    void findByIdForUpdate_notFound() {
        // given
        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderReader.findByIdForUpdate(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_ORDER);
    }

    @Test
    @DisplayName("PessimisticLockException 발생 시 LOCK_TIMEOUT_ORDER 예외를 던진다")
    void findByIdForUpdate_pessimisticLock() {
        // given
        when(orderRepository.findByIdForUpdate(1L))
                .thenThrow(new PessimisticLockException(null, null, null));

        // when & then
        assertThatThrownBy(() -> orderReader.findByIdForUpdate(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.LOCK_TIMEOUT_ORDER);
    }

    @Test
    @DisplayName("LockTimeoutException 발생 시 LOCK_TIMEOUT_ORDER 예외를 던진다")
    void findByIdForUpdate_lockTimeout() {
        // given
        when(orderRepository.findByIdForUpdate(1L))
                .thenThrow(new LockTimeoutException(null, null, null));

        // when & then
        assertThatThrownBy(() -> orderReader.findByIdForUpdate(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.LOCK_TIMEOUT_ORDER);
    }
}