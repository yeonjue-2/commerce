package hello.commerce.order;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.order.dto.OrderRequestV1;
import hello.commerce.order.dto.OrderResponseV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.product.ProductReader;
import hello.commerce.product.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductReader productReader;

    @Transactional
    @Override
    public OrderResponseV1 createOrder(OrderRequestV1 orderRequest) {
        // 1. 유효성 검사 - product는 영속상태
        Product product = validateOrderCondition(orderRequest);

        // 2. 주문 생성 및 저장
        int totalAmount = product.getAmount() * orderRequest.getQuantity();
        Order order = createOrderEntity(orderRequest, product, totalAmount);
        orderRepository.save(order);

        // 3. 재고 차감 - 영속상태인 product에 변경감지가 되어 커밋 시점에 JPA가 자동으로 update 쿼리 실행
        product.decreaseStock(orderRequest.getQuantity());

        // 4. 응답 반환
        return orderRepository.findWithProductById(order.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));
    }

    @Override
    public Page<OrderResponseV1> getOrders(Pageable pageable, OrderStatus orderStatus) {
        return orderRepository.findAllWithProductByOrderStatus(pageable, orderStatus);
    }

    @Override
    public Page<OrderResponseV1> getOrders(Pageable pageable) {
        return orderRepository.findAllWithProduct(pageable);
    }

    @Override
    public OrderResponseV1 getOrderById(Long orderId) {
        return orderRepository.findWithProductById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));
    }


    private static Order createOrderEntity(OrderRequestV1 orderRequest, Product product, int totalAmount) {
        return Order.builder()
                .product(product)
                .userId(orderRequest.getUserId())
                .quantity(orderRequest.getQuantity())
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.INITIAL)
                .build();
    }

    private Product validateOrderCondition(OrderRequestV1 orderRequest) {
        // 수량 유효성
        if (orderRequest.getQuantity() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_QUANTITY);
        }

        // product는 영속상태가 됨
        Product product = productReader.findByIdForUpdate(orderRequest.getProductId());

        // 재고 확인
        if (product.getStock() < orderRequest.getQuantity()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        return product;
    }
}
