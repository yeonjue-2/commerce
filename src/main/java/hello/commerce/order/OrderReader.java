package hello.commerce.order;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReader {

    private final OrderRepository orderRepository;

    public Order findByIdForUpdate(Long orderId) {
        try {
            return orderRepository.findByIdForUpdate(orderId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));
        } catch (PessimisticLockException | LockTimeoutException e) {
            throw new BusinessException(ErrorCode.LOCK_TIMEOUT_ORDER);
        }
    }
}
