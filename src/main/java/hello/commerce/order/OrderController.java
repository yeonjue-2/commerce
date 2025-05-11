package hello.commerce.order;

import hello.commerce.common.request.PageRequestDto;
import hello.commerce.order.dto.OrderResponseV1;
import hello.commerce.order.dto.OrderListResponseV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/v1/orders")
    public ResponseEntity<OrderListResponseV1> getOrders(
            @Valid @ModelAttribute PageRequestDto pageRequestDto,
            @RequestParam(value = "order_status", required = false) OrderStatus orderStatus
    ) {
        Pageable pageable = pageRequestDto.toPageable();
        Page<Order> page = (orderStatus != null)
                ? orderService.getOrders(pageable, orderStatus)
                : orderService.getOrders(pageable);
        OrderListResponseV1 orderListResponseV1 = OrderListResponseV1.fromEntities(page);

        return ResponseEntity.ok(orderListResponseV1);
    }

    @GetMapping("/v1/orders/{order_id}")
    public ResponseEntity<OrderResponseV1> getOrderById(@PathVariable("order_id") Long orderId) {
        // TO-DO 주문 상세 조회 서비스 호출
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
