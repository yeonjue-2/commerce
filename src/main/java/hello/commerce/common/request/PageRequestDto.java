package hello.commerce.common.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageRequestDto {

    @Min(1)
    private int page = 1;

    @Min(1)
    @Max(20)
    private int size = 20;

    private String sortBy = "createdAt";
    private String sortOrder = "desc";

    public Pageable toPageable() {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.DESC);
        return PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
    }
}
