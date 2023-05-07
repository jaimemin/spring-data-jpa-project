package com.tistory.jaimemin.springdatajpaproject.repository;

import com.tistory.jaimemin.springdatajpaproject.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager entityManager;

    public void save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
        } else {
            /**
             * 기존 파라미터로 전달되는 item은 영속성 컨텍스트로 관리 X
             * merge로 반환하는 Item 객체가 영속성 컨텍스트로 관리
             *
             * merge는 모든 필드를 업데이트 (의도치 않게 null로 업데이트 가능)
             */
            entityManager.merge(item); // 준영속 상태의 엔티티를 영속 상태로 변경
        }
    }

    public Item findOne(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager.createQuery("SELECT i FROM Item i", Item.class)
                .getResultList();
    }
}
