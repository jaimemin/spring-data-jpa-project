package com.tistory.jaimemin.springdatajpaproject.repository.order.queryrepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    final EntityManager entityManager;

    /**
     * 장점: 성능 최적화 측면에서 좀 더 나음
     * 단점: Fetch Join보다 재사용성은 떨어짐
     *
     * @return
     */
    public List<SampleOrderQueryDto> findOrderDtos() {
        return entityManager.createQuery(
                "SELECT new com.tistory.jaimemin.springdatajpaproject.repository.SampleOrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "FROM Order o " +
                        "JOIN o.member m " +
                        "JOIN o.delivery d", SampleOrderQueryDto.class
        ).getResultList();
    }
}
