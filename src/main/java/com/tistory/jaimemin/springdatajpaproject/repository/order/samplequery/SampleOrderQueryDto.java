package com.tistory.jaimemin.springdatajpaproject.repository.order.samplequery;

import com.tistory.jaimemin.springdatajpaproject.domain.Address;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SampleOrderQueryDto {

    private Long orderId;

    private String name;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    private Address address;

}
