package com.tistory.jaimemin.springdatajpaproject.repository;

import com.tistory.jaimemin.springdatajpaproject.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String memberName;

    private OrderStatus orderStatus;
}
