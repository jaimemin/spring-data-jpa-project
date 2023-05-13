package com.tistory.jaimemin.springdatajpaproject.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tistory.jaimemin.springdatajpaproject.domain.Order;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderStatus;
import com.tistory.jaimemin.springdatajpaproject.domain.QMember;
import com.tistory.jaimemin.springdatajpaproject.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    /**
     * JPA Criteria (사실 실무에서는 QueryDSL이 보편적으로 쓰임)
     * QueryDSL 적용
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    /**
     * Fetch Join
     *
     * @return
     */
    public List<Order> findAllWithMemberDelivery() {
        return entityManager.createQuery(
                "SELECT o FROM Order o " +
                        "JOIN FETCH o.member m " +
                        "JOIN FETCH o.delivery d", Order.class
        ).getResultList();
    }

    /**
     * XToOne 관계 FETCH JOIN Paging
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return entityManager.createQuery(
                        "SELECT o FROM Order o " +
                                "JOIN FETCH o.member m " +
                                "JOIN FETCH o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * FETCH JOIN
     * ORDER와 ORDER_ITEMS JOIN할 경우 데이터 뻥튀기 되는 문제
     * ORDER_ITEMS를 기준으로 ORDER 데이터가 늘어나는 문제
     * 우리의 목표는 ORDER를 기준으로 페이징
     *
     * @return
     */
    public List<Order> findAllWithItem() {
        return entityManager.createQuery(
                        "SELECT o FROM Order o " +
                                "JOIN FETCH o.member m " +
                                "JOIN FETCH o.delivery d " +
                                "JOIN FETCH o.orderItems oi " +
                                "JOIN FETCH oi.item i", Order.class)
                .getResultList();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }

        return QOrder.order.status.eq(statusCond);
    }

    private BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }

        return QMember.member.name.like(memberName);
    }
}
