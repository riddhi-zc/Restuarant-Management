package com.resturant.management.restcontroller;

import com.resturant.management.constants.Common;
import com.resturant.management.constants.ErrorConstants;
import com.resturant.management.dto.CategoryDto;
import com.resturant.management.dto.ErrorDto;
import com.resturant.management.dto.MenuDto;
import com.resturant.management.dto.OrderDto;
import com.resturant.management.response.ResponseMessageDto;
import com.resturant.management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/order")
public class OrderRestController {

    @Autowired
    OrderService orderService;

    @PostMapping("/place")
    private ResponseMessageDto placeOrder(HttpServletRequest request, @RequestBody List<OrderDto> dto)
    {
        try{
            this.orderService.placeOrder(dto);
            return new ResponseMessageDto(Boolean.TRUE,null, Common.ORDER_PLACED);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500, ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally
        {}

    }

    @PutMapping("/updateOrder")
    private ResponseMessageDto updateOrder(HttpServletRequest request,@RequestParam("orderId") Long orderId, @RequestBody List<OrderDto> dto)
    {
        try{
            this.orderService.updateOrder(dto,orderId);
            return new ResponseMessageDto(Boolean.TRUE,null, Common.ORDER_UPDATED);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500, ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally
        {}

    }

    @DeleteMapping("/removeOrder")
    private ResponseMessageDto removeOrder(HttpServletRequest request,@RequestParam("orderId") Long orderId)
    {
        try{
            this.orderService.removeOrder(orderId);
            return new ResponseMessageDto(Boolean.TRUE,null, Common.ORDER_DELETED);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500, ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally
        {}
    }

    @GetMapping("/getOrderById")
    private ResponseMessageDto getOrderById(HttpServletRequest request,@RequestParam("orderId") Long orderId)
    {
        try{
            return new ResponseMessageDto(Boolean.TRUE,null, this.orderService.getOrderById(orderId));
        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500, ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally
        {}

    }

    @GetMapping("/getAllOrders")
    private ResponseMessageDto getAllOrders(Principal principal,HttpServletRequest request)
    {
        try{
            return new ResponseMessageDto(Boolean.TRUE,null, this.orderService.getAllOrders());
        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500, ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally
        {}

    }

}


