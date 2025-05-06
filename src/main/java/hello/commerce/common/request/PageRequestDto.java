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

    public static final int DEFAULT_PAGE = 1;
    public static final int MIN_SIZE = 1;
    public static final int DEFAULT_SIZE = 20;

    @Min(DEFAULT_PAGE)
    private int page = DEFAULT_PAGE;

    @Min(MIN_SIZE)
    @Max(DEFAULT_SIZE)
    private int size = DEFAULT_SIZE;

    private String sortBy = "createdAt";
    private String sortOrder = "desc";

    public Pageable toPageable() {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortOrder).orElse(Sort.Direction.DESC);
        return PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
    }
}
