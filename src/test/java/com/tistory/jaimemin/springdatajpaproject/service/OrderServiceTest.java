package com.tistory.jaimemin.springdatajpaproject.service;

import com.tistory.jaimemin.springdatajpaproject.domain.Address;
import com.tistory.jaimemin.springdatajpaproject.domain.Member;
import com.tistory.jaimemin.springdatajpaproject.domain.Order;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderStatus;
import com.tistory.jaimemin.springdatajpaproject.domain.item.Book;
import com.tistory.jaimemin.springdatajpaproject.domain.item.Item;
import com.tistory.jaimemin.springdatajpaproject.exception.NotEnoughStockException;
import com.tistory.jaimemin.springdatajpaproject.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void order() throws Exception {
        // given
        Member member = createMember();

        int price = 10000;
        int stockQuantity = 10;

        Book book = createBook("JPA", price, stockQuantity);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", price * orderCount, getOrder.getTotalPrice());
        assertEquals("줌누 수량만큼 재고가 줄어야 한다.", stockQuantity - orderCount, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void orderStockQuantityExceedException() throws Exception {
        // given
        Member member = createMember();

        int price = 10000;
        int stockQuantity = 10;

        Book book = createBook("JPA", price, stockQuantity);

        int orderCount = stockQuantity + 1;

        // when
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void cancel() throws Exception {
        // given
        Member member = createMember();

        int price = 10000;
        int stockQuantity = 10;

        Book book = createBook("JPA", price, stockQuantity);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 시 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        entityManager.persist(book);

        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원");
        member.setAddress(new Address("서울", "강서구", "123-123"));
        entityManager.persist(member);

        return member;
    }
}