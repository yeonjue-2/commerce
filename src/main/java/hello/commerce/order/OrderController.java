package hello.commerce.order;

import hello.commerce.common.request.PageRequestDto;
import hello.commerce.order.dto.OrderResponseV1;
import hello.commerce.order.dto.OrderListResponseDtoV1;
import hello.commerce.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class OrderController {

    @GetMapping("/v1/orders")
    public ResponseEntity<OrderListResponseDtoV1> getOrders(
            @Validated @ModelAttribute PageRequestDto pageRequestDto,
            @RequestParam(value = "order_status", required = false)OrderStatus orderStatus
    ) {
        Pageable pageable = pageRequestDto.toPageable();
        // TO-D0 주문 목록 조회 서비스 호출
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/v1/orders/{order_id}")
    public ResponseEntity<OrderResponseV1> getOrderById(@PathVariable("order_id") Long orderId) {
        // TO-DO 주문 상세 조회 서비스 호출
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
