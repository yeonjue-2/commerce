package hello.commerce.order.dto;

import hello.commerce.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListResponseDtoV1 {
    private List<OrderResponseDtoV1> orders;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public static OrderListResponseDtoV1 fromEntities(Page<Order> orderPage) {

        List<OrderResponseDtoV1> orderDtos = orderPage.getContent().stream()
                .map(OrderResponseDtoV1::fromEntity)
                .collect(Collectors.toList());

        return OrderListResponseDtoV1.builder()
                .orders(orderDtos)
                .currentPage(orderPage.getNumber() + 1)
                .totalPages(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .build();
    }
}
