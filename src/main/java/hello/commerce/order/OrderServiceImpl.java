package hello.commerce.order;

import hello.commerce.order.dto.OrderRequestV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Override
    public Order createOrder(OrderRequestV1 request) {
        return null;
    }

    @Override
    public Page<Order> getOrders(Pageable pageable, OrderStatus orderStatus) {
        return null;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return null;
    }
}
