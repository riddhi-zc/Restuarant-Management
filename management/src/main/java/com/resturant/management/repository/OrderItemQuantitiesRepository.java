package com.resturant.management.repository;

import com.resturant.management.entity.OrderItemQuantitiesModel;
import com.resturant.management.entity.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemQuantitiesRepository extends JpaRepository<OrderItemQuantitiesModel,Long> {
    List<OrderItemQuantitiesModel> findByOrder(OrderModel orderModel);
}
