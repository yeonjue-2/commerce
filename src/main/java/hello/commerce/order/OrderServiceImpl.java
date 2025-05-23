package hello.commerce.order;

import hello.commerce.common.model.BusinessException;
import hello.commerce.common.model.ErrorCode;
import hello.commerce.order.dto.OrderRequestV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(OrderRequestV1 request) {
        return null;
    }

    @Override
    public Page<Order> getOrders(Pageable pageable, OrderStatus orderStatus) {
        return orderRepository.findAllByOrderStatus(pageable, orderStatus);
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));
    }
}
