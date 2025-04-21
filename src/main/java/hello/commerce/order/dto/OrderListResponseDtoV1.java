package hello.commerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListResponseDtoV1 {
    private List<OrderDto> orders;
    private int currentPage;
    private int totalPages;
    private long tatalElements;
}
