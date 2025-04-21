package hello.commerce.common.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PageRequestDto {

    private int page = 1;
    private int size = 20;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
