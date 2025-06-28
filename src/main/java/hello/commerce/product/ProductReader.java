package hello.commerce.product;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReader {

    private final ProductRepository productRepository;

    public Product findByIdForUpdate(Long productId) {
        try {
            return productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
        } catch (PessimisticLockException | LockTimeoutException e) {
            throw new BusinessException(ErrorCode.LOCK_TIMEOUT_PRODUCT);
        }
    }
}
