package com.tistory.jaimemin.springdatajpaproject.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager entityManager;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders();
        orders.forEach(orderQueryDto -> {
            orderQueryDto.setOrderItems(findOrderItems(orderQueryDto.getOrderId()));
        });

        return orders;
    }

    /**
     * 쿼리는 두 번
     * 메모리에서 값 세팅
     *
     * @return
     */
    public List<OrderQueryDto> findAllByDtoOptimization() {
        List<OrderQueryDto> orders = findOrders();
        Map<Long, List<OrderItemQueryDto>> orderItemMap = getOrderItemMap(getOrderIds(orders));
        orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return orders;
    }

    /**
     * 단 하나의 쿼리로 다 가져옴
     * 1:다 JOIN에 의해 중복 데이터 반환
     *
     * @return
     */
    public List<OrderFlatDto> findAllByDtoFlat() {
        return entityManager.createQuery(
                "SELECT new com.tistory.jaimemin.springdatajpaproject.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        "FROM Order o " +
                        "JOIN o.member m " +
                        "JOIN o.delivery d " +
                        "JOIN o.orderItems oi " +
                        "JOIN oi.item i", OrderFlatDto.class)
                .getResultList();
    }

    private Map<Long, List<OrderItemQueryDto>> getOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = entityManager.createQuery(
                        "SELECT new com.tistory.jaimemin.springdatajpaproject.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "FROM OrderItem oi " +
                                "JOIN oi.item i " +
                                "WHERE oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
    }

    private static List<Long> getOrderIds(List<OrderQueryDto> orders) {
        return orders.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return entityManager.createQuery(
                        "SELECT new com.tistory.jaimemin.springdatajpaproject.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "FROM OrderItem oi " +
                                "JOIN oi.item i " +
                                "WHERE oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return entityManager.createQuery(
                        "SELECT new com.tistory.jaimemin.springdatajpaproject.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                "FROM Order o " +
                                "JOIN o.member m " +
                                "JOIN o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

}
