package hello.commerce.order;

import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    /**
     * 주문 목록 조회
     * @param pageable
     * @param orderStatus
     * @return 주문 목록 응답
     */
    Page<Order> getOrders(Pageable pageable, OrderStatus orderStatus);

    /**
     * 주문 상세 조회
     * @param orderId
     * @return 주문 단건 응답
     */
    Order getOrderById(Long orderId);

}
