package com.resturant.management.service;

import com.resturant.management.dto.OrderDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {

    void placeOrder(List<OrderDto> dto);

    void updateOrder(List<OrderDto> dto, Long orderId);

    void removeOrder(Long orderId);

    Object getOrderById(Long orderId);

    Object getAllOrders();
}
