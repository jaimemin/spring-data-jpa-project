package com.tistory.jaimemin.springdatajpaproject.repository;

import com.tistory.jaimemin.springdatajpaproject.domain.Member;
import com.tistory.jaimemin.springdatajpaproject.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
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
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> o = criteriaQuery.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = criteriaBuilder.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    criteriaBuilder.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery).setMaxResults(1000); //최대 1000건

        return query.getResultList();
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

}
