package hello.commerce.order;

import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderStatus(Pageable pageable, OrderStatus orderStatus);
}
