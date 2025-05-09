package hello.commerce.order;

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
    public Page<Order> getOrders(Pageable pageable, OrderStatus orderStatus) {
        return (orderStatus != null)
                ? orderRepository.findAllByOrderStatus(pageable, orderStatus)
                : orderRepository.findAll(pageable);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return null;
    }
}
