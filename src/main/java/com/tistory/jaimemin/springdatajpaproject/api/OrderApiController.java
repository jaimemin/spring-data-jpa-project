package com.tistory.jaimemin.springdatajpaproject.api;

import com.tistory.jaimemin.springdatajpaproject.domain.Address;
import com.tistory.jaimemin.springdatajpaproject.domain.Order;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderItem;
import com.tistory.jaimemin.springdatajpaproject.domain.OrderStatus;
import com.tistory.jaimemin.springdatajpaproject.repository.OrderRepository;
import com.tistory.jaimemin.springdatajpaproject.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * 객체를 그대로 반환하는 성능 안 좋은 api
     *
     * @return
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        /**
         * Lazy Loading 객체 그래프
         */
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            for (OrderItem orderItem : order.getOrderItems()) {
                orderItem.getItem().getName();
            }
        }

        return orders;
    }

    /**
     * DTO 반환할 경우 내부 Wrapping Entity도 없어야함
     * 다 DTO로 변환 필요
     * Entity 그대로 노출 시 API Spec 바뀔 위험성 큼
     * 여전히 쿼리 너무 많이 호출되는 문제 존재
     *
     * @return
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderRepository.findAll(new OrderSearch()).stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Collection Join에 의해 데이터 뻥튀기 되는 문제 존재
     *
     * @XToMany에 대한 FETCH JOIN문의 문제점
     * 1. 페이징 불가능!!
     * 2. 컬렉션 패치 조인은 1개만 사용 가능
     * -> 컬렉션 둘 이상에 FETCH JOIN을 사용하면 데이터 정합성 깨질 우려 있음
     * 
     * @return
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        return orderRepository.findAllWithItem().stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 성능 최적화
     * 1. XToOne 관계는 모두 Fetch Join으로 가져옴
     * 2. XToMany 관계는 application.yml 내 default_batch_fetch_size를 통해 미리 로딩하여 1 : N : N 관계를 1 : 1 : 1 관계로 변환
     *
     * -> 중복 데이터 배제
     *
     * @return
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3Page(@RequestParam(defaultValue = "0") int offset
            , @RequestParam(defaultValue = "100") int limit) {
        return orderRepository.findAllWithMemberDelivery(offset, limit).stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    static class OrderDto {

        private Long orderId;

        private String name;

        private LocalDateTime orderDate;

        private OrderStatus orderStatus;

        private Address address;

        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());

        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;

        private int orderPrice;

        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
