package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * x to One
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1의 문제점
     * Entity를 그대로 반환 시 무한 루프에 빠진다
     * @JsonIgnore 어노테이션을 사용해도 지연로딩일 시 프록시객체를 반환되므로
     * Hibernate5Module의 jackson-datatype-hibernaete5를 사용하여 설정하여야 정상적으로 출력된다.
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        return orderRepository.findAllByString(new OrderSearch());
    }

    /**
     * V2의 문제점
     * DB 쿼리가 너무 많이 나간다
     * N+1 문제
     */
    @GetMapping("/api/v2/simple-orders")
    public OrdersRes ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleDto> result = orders.stream()
                .map(OrderSimpleDto::new)
                .collect(Collectors.toList());

        return new OrdersRes(result);
    }

    /**
     * V3의 문제점
     * 페치조인을 사용하여 페이징을 사용하지 못한다
     * 엔티티 -> DTO로 변환 로직이 필요하다
     */
    @GetMapping("/api/v3/simple-orders")
    public OrdersRes ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderSimpleDto> result = orders.stream()
                .map(OrderSimpleDto::new)
                .collect(Collectors.toList());

        return new OrdersRes(result);
    }

    /**
     * V4의 문제점
     * 성능은 개선되지만 리포지토리 재사용성이 떨어지고 쿼리 코드가 지저분해진다
     * 화면과 종속성이 생긴다
     */
    @GetMapping("/api/v4/simple-orders")
    public OrdersResV4 ordersV4() {
        List<OrderSimpleQueryDto> orders = orderSimpleQueryRepository.findOrdersDto();
        return new OrdersResV4(orders);
    }

    @Data
    @AllArgsConstructor
    static class OrdersRes {
        List<OrderSimpleDto> orders;
    }

    @Data
    @AllArgsConstructor
    static class OrdersResV4 {
        List<OrderSimpleQueryDto> orders;
    }
    @Data
    static class OrderSimpleDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
