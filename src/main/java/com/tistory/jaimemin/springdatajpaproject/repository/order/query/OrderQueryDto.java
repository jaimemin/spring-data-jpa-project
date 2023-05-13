package com.tistory.jaimemin.springdatajpaproject.repository.order.query;

import com.tistory.jaimemin.springdatajpaproject.domain.Address;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {

    private Long orderId;

    private String name;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    private Address address;

    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

}
