package hello.commerce.order;

import hello.commerce.order.dto.OrderResponseDtoV1;
import hello.commerce.order.dto.OrderListResponseDtoV1;
import hello.commerce.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Override
    public OrderListResponseDtoV1 getOrders(Pageable pageable, OrderStatus orderStatus) {
        return null;
    }

    @Override
    public OrderResponseDtoV1 getOrderById(Long orderId) {
        return null;
    }
}
