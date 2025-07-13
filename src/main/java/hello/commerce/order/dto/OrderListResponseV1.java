package hello.commerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListResponseV1 {
    private List<OrderResponseV1> orders;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public static OrderListResponseV1 from(Page<OrderResponseV1> orderPage) {
        return OrderListResponseV1.builder()
                .orders(orderPage.getContent())
                .currentPage(orderPage.getNumber() + 1)
                .totalPages(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .build();
    }
}
