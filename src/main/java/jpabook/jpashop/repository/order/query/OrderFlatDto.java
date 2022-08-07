package jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {

    @JsonIgnore
    private Long orderId;

    private String name;

    private LocalDateTime orderdate;

    private OrderStatus orderStatus;

    private Address address;

    private String itemName;

    private int orderPrice;

    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderdate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderdate = orderdate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
