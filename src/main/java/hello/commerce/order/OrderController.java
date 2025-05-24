package hello.commerce.order;

import hello.commerce.common.request.PageRequestDto;
import hello.commerce.order.dto.OrderRequestV1;
import hello.commerce.order.dto.OrderResponseV1;
import hello.commerce.order.dto.OrderListResponseV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequiredArgsConstructor
@RestController
@RequestMapping
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping("/v1/orders")
    public ResponseEntity<OrderResponseV1> createOrder(@Valid @RequestBody OrderRequestV1 orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        OrderResponseV1 orderResponseV1 = OrderResponseV1.fromEntity(order);

        // 주문 생성 후 redirect URI 생성
        URI location = URI.create("/v1/orders/" + order.getId());
        return ResponseEntity.created(location).body(orderResponseV1);
    }

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

        Order orderById = orderService.getOrderById(orderId);
        OrderResponseV1 response = OrderResponseV1.fromEntity(orderById);

        return ResponseEntity.ok(response);
    }
}
