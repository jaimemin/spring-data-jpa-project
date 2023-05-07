package com.tistory.jaimemin.springdatajpaproject.service;

import com.tistory.jaimemin.springdatajpaproject.domain.item.Book;
import com.tistory.jaimemin.springdatajpaproject.domain.item.Item;
import com.tistory.jaimemin.springdatajpaproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 아이템 저장
     *
     * @param item
     */
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 아이템 업데이트 (변경 감지 기능)
     *
     * @param itemId
     * @param name
     * @param price
     * @param stockQuantity
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);

        // 영속 상태이기 때문에 굳이 save 메서드 호출할 필요 없음 (트랜잭션이 끝난 시점에 flush)
    }

    /**
     * 모든 아이템 조회
     *
     * @return
     */
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    /**
     * 하나의 아이템 조회
     *
     * @param itemId
     * @return
     */
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
