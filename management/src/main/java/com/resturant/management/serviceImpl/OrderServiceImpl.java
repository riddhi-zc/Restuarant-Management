package com.resturant.management.serviceImpl;

import com.resturant.management.constants.ErrorConstants;
import com.resturant.management.dto.OrderDto;
import com.resturant.management.entity.CategoryModel;
import com.resturant.management.entity.MenuModel;
import com.resturant.management.entity.OrderItemQuantitiesModel;
import com.resturant.management.entity.OrderModel;
import com.resturant.management.exception.NotFoundException;
import com.resturant.management.repository.CategoryRepository;
import com.resturant.management.repository.MenuRepository;
import com.resturant.management.repository.OrderItemQuantitiesRepository;
import com.resturant.management.repository.OrderRepository;
import com.resturant.management.response.ItemQuantityResponseDto;
import com.resturant.management.response.OrderResponseDto;
import com.resturant.management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MenuRepository menuRepo;

    @Autowired
    private OrderItemQuantitiesRepository quantitiesRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void placeOrder(List<OrderDto> dto) {
        try{
            OrderModel orderModel=new OrderModel();

            List<OrderItemQuantitiesModel> quantities = new ArrayList<>();

            orderModel.setOrderId(this.generateOrderId());
            for(OrderDto order: dto){
                OrderItemQuantitiesModel quantitiesModel=new OrderItemQuantitiesModel();
                quantitiesModel.setOrder(orderModel);
                Optional<MenuModel> menu=this.menuRepo.findById(order.getMenuId());
                if(menu.isPresent()) {
                    quantitiesModel.setMenu(menu.get());
                }
                quantitiesModel.setQuantity(order.getQuantity());
                this.quantitiesRepository.save(quantitiesModel);
                quantities.add(quantitiesModel);
            }
            orderModel.setTotalPrice(this.calcultePrice(dto));
            orderModel.setQuantities(quantities);
            orderModel.setCreatedDate(new Date());
            this.orderRepository.save(orderModel);
        }
        finally {

        }

    }

    private Double calcultePrice(List<OrderDto> dto) {
        Double totalPrice=new Double(0.0);
        for (OrderDto order :dto)
        {
           totalPrice =totalPrice+ Double.valueOf(order.getQuantity()) * order.getPrice();
        }
        return  totalPrice;
    }

    @Override
    public void updateOrder(List<OrderDto> dto, Long orderId) {
        try{
            Optional<OrderModel> orderModel=this.orderRepository.findById(orderId);
            if(orderModel.isPresent())
            {
                OrderModel updateOrder=orderModel.get();
                List<OrderItemQuantitiesModel> quantities =updateOrder.getQuantities();
                for(int i=0; i<=dto.size();i++){
                    OrderItemQuantitiesModel quantitiesModel=quantities.get(i);
                    Optional<MenuModel> menu=this.menuRepo.findById(dto.get(i).getMenuId());
                    if(menu.isPresent()) {
                        quantitiesModel.setMenu(menu.get());
                    }
                    quantitiesModel.setQuantity(dto.get(i).getQuantity());
                    this.quantitiesRepository.save(quantitiesModel);
                    quantities.add(quantitiesModel);
                }
                updateOrder.setTotalPrice(this.calcultePrice(dto));
                updateOrder.setQuantities(quantities);
                updateOrder.setModifiedDate(new Date());
                this.orderRepository.save(updateOrder);
            }
            else
            {
                throw new NotFoundException(ErrorConstants.ORDER_NOT_FOUND);
            }
        }
        finally {

        }

    }

    @Override
    public void removeOrder(Long orderId) {
            try{

                Optional<OrderModel> orderModel=this.orderRepository.findById(orderId);
                if(orderModel.isPresent())
                {
                    List<OrderItemQuantitiesModel>  quantitiesModel=this.quantitiesRepository.findByOrder(orderModel.get());
                    this.quantitiesRepository.deleteAll(quantitiesModel);
                    this.orderRepository.delete(orderModel.get());
                }
            }finally {

            }
    }

    @Override
    public Object getOrderById(Long orderId) {
        OrderResponseDto response=new OrderResponseDto();
        List<ItemQuantityResponseDto> res=new ArrayList<>();
        try{

            Optional<OrderModel> orderModel=this.orderRepository.findById(orderId);
            if(orderModel.isPresent())
            {
                OrderModel orders=orderModel.get();
                List<OrderItemQuantitiesModel> quantities=orders.getQuantities();
                for (OrderItemQuantitiesModel quantity : quantities)
                {
                    res.add(ItemQuantityResponseDto.builder().item(quantity.getMenu().getItem())
                            .price(quantity.getMenu().getPrice())
                            .build());

                }
                response.setItems(res);
                response.setTotalPrice(orders.getTotalPrice());
                response.setOrderId(orders.getOrderId());

            }
            else
            {
                throw new NotFoundException(ErrorConstants.ORDER_NOT_FOUND);
            }

        }finally {

        }

        return response;
    }

    @Override
    public Object getAllOrders() {
        OrderResponseDto response=new OrderResponseDto();
        List<ItemQuantityResponseDto> res=new ArrayList<>();
        try{
                List<OrderModel> orders=this.orderRepository.findAll();
                for(OrderModel order : orders)
                {
                    List<OrderItemQuantitiesModel> quantities=order.getQuantities();
                    for (OrderItemQuantitiesModel quantity : quantities)
                    {
                        res.add(ItemQuantityResponseDto.builder().item(quantity.getMenu().getItem())
                                .price(quantity.getMenu().getPrice())
                                .build());

                    }
                    response.setItems(res);
                    response.setTotalPrice(order.getTotalPrice());
                    response.setOrderId(order.getOrderId());
                }

        }finally {
        }
        return response;
    }

    public  String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        String orderId = uuid.toString().replace("-", "").substring(0, 10); // Generate a 10-character random ID
        return orderId;
    }

}
