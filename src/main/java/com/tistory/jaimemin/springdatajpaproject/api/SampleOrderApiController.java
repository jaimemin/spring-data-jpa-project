package com.tistory.jaimemin.springdatajpaproject.api;

import com.tistory.jaimemin.springdatajpaproject.domain.Address;
import com.tistory.jaimemin.springdatajpaproject.domain.Order;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderStatus;
import com.tistory.jaimemin.springdatajpaproject.repository.OrderRepository;
import com.tistory.jaimemin.springdatajpaproject.repository.OrderSearch;
import com.tistory.jaimemin.springdatajpaproject.repository.order.queryrepository.OrderQueryRepository;
import com.tistory.jaimemin.springdatajpaproject.repository.order.queryrepository.SampleOrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class SampleOrderApiController {

    private final OrderRepository orderRepository;

    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        return orderRepository.findAll(new OrderSearch());
    }

    /**
     * 순수 Entity를 반환하지 않게 되었지만
     * N + 1 문제에 취약한 API
     *
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SampleOrderDto> ordersV2() {
        return orderRepository.findAll(new OrderSearch()).stream()
                .map(SampleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * N + 1 문제를 해결하기 위해 FETCH JOIN
     *
     * @return
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SampleOrderDto> ordersV3() {
        return orderRepository.findAllWithMemberDelivery().stream()
                .map(SampleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * v3보다 성능 측면에서는 유리
     * 재사용성은 떨어짐
     *
     * @return
     */
    @GetMapping("/api/v4/simple-orders")
    public List<SampleOrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderDtos();
    }

    @Data
    static class SampleOrderDto {

        private Long orderId;

        private String name;

        private LocalDateTime orderDate;

        private OrderStatus orderStatus;

        private Address address;

        public SampleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
