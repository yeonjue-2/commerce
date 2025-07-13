package hello.commerce.order;

import hello.commerce.order.dto.OrderResponseV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT new hello.commerce.order.dto.OrderResponseV1(
                o.id, o.userId, p.id, p.productName, o.orderStatus,
                o.totalAmount, o.quantity, o.kakaoPayReadyUrl,
                o.createdAt, o.updatedAt
        )
        FROM Order o
        JOIN o.product p
        WHERE o.id = :orderId
    """)
    Optional<OrderResponseV1> findWithProductById(@Param("orderId") Long orderId);

    @Query(
        value = """
            SELECT new hello.commerce.order.dto.OrderResponseV1(
                o.id, o.userId, p.id, p.productName, o.orderStatus,
                o.totalAmount, o.quantity, o.kakaoPayReadyUrl,
                o.createdAt, o.updatedAt
            )
            FROM Order o
            JOIN o.product p
        """,
            countQuery = "SELECT COUNT(o) FROM Order o"
    )
    Page<OrderResponseV1> findAllWithProduct(Pageable pageable);

    @Query(
        value = """
            SELECT new hello.commerce.order.dto.OrderResponseV1(
                o.id, o.userId, p.id, p.productName, o.orderStatus,
                o.totalAmount, o.quantity, o.kakaoPayReadyUrl,
                o.createdAt, o.updatedAt
            )
            FROM Order o
            JOIN o.product p
            WHERE o.orderStatus = :orderStatus
        """,
            countQuery = "SELECT COUNT(o) FROM Order o"
    )
    Page<OrderResponseV1> findAllWithProductByOrderStatus(Pageable pageable, @Param("orderStatus") OrderStatus orderStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000") // 밀리초 단위
    })
    @Query("select o from Order o where o.id = :order_id")
    Optional<Order> findByIdForUpdate(@Param("order_id") Long orderId);
}
