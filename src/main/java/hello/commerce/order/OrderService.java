package hello.commerce.order;

import hello.commerce.order.dto.OrderRequestV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import java.awt.print.Pageable;

public interface OrderService {

    /**
     * 주문 생성
     * @param request
     * @return
     */
    Order createOrder(OrderRequestV1 request);

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
