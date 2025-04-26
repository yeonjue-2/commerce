package hello.commerce.order;

import hello.commerce.order.dto.OrderListResponseDtoV1;
import hello.commerce.order.model.OrderStatus;

import java.awt.print.Pageable;

public interface OrderService {

    /**
     * 주문 목록 조회
     * @param pageable
     * @param orderStatus
     * @return 주문 목록 응답
     */
    OrderListResponseDtoV1 getOrders(Pageable pageable, OrderStatus orderStatus);
}
