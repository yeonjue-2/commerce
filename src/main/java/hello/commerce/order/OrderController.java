package hello.commerce.order;

import hello.commerce.common.request.PageRequestDto;
import hello.commerce.order.dto.OrderResponseDtoV1;
import hello.commerce.order.dto.OrderListResponseDtoV1;
import hello.commerce.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @GetMapping
    public ResponseEntity<OrderListResponseDtoV1> getOrders(
        @ModelAttribute PageRequestDto pageRequestDto,
        @RequestParam(value = "order_status", required = false)OrderStatus orderStatus
    ) {
        Pageable pageable = pageRequestDto.toPageable();
        // TO-D0 주문 목록 조회 서비스 호출
        return ResponseEntity.ok().body(null); // 임시로 null 반환
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderResponseDtoV1> getOrderById(@PathVariable("order_id") Long orderId) {
        // TO-DO 주문 상세 조회 서비스 호출
        return ResponseEntity.ok().body(null); // 임시로 null 반환
    }
}
