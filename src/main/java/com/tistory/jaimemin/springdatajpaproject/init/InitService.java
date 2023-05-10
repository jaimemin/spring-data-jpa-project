package com.tistory.jaimemin.springdatajpaproject.init;

import com.tistory.jaimemin.springdatajpaproject.domain.*;
import com.tistory.jaimemin.springdatajpaproject.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final EntityManager entityManager;

    public void dbInit() {
        Member member = createMember("userA");
        entityManager.persist(member);

        Book book = createBook("JPA1 BOOK", 10000, 100);
        Book book2 = createBook("JPA2 BOOK", 20000, 100);
        entityManager.persist(book);
        entityManager.persist(book2);

        OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
        OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

        Delivery delivery = createDelivery(member);

        Order order = Order.createOrder(member, delivery, orderItem, orderItem2);
        entityManager.persist(order);
    }

    public void dbInit2() {
        Member member = createMember("userB");
        entityManager.persist(member);

        Book book = createBook("SPRING1 BOOK", 20000, 200);
        Book book2 = createBook("SPRING2 BOOK", 40000, 300);
        entityManager.persist(book);
        entityManager.persist(book2);

        OrderItem orderItem = OrderItem.createOrderItem(book, 20000, 3);
        OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

        Delivery delivery = createDelivery(member);

        Order order = Order.createOrder(member, delivery, orderItem, orderItem2);
        entityManager.persist(order);
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "강동구", "12345"));

        return member;
    }

    private Book createBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);

        return book;
    }

    private Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        return delivery;
    }
}