package com.tistory.jaimemin.springdatajpaproject.service;

import com.tistory.jaimemin.springdatajpaproject.domain.Delivery;
import com.tistory.jaimemin.springdatajpaproject.domain.Member;
import com.tistory.jaimemin.springdatajpaproject.domain.Order;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderItem;
import com.tistory.jaimemin.springdatajpaproject.domain.item.Item;
import com.tistory.jaimemin.springdatajpaproject.repository.ItemRepository;
import com.tistory.jaimemin.springdatajpaproject.repository.MemberRepository;
import com.tistory.jaimemin.springdatajpaproject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final ItemRepository itemRepository;

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    /**
     * 검색
     */
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
