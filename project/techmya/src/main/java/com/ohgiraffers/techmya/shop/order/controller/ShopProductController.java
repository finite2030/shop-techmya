package com.ohgiraffers.techmya.shop.order.controller;

import com.ohgiraffers.techmya.admin.order.model.dto.OrderDTO;
import com.ohgiraffers.techmya.shop.order.model.dto.OrderCartDTO;
import com.ohgiraffers.techmya.shop.order.model.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/shop")
@Slf4j
public class ShopProductController {

    private final OrderService orderService;

    public ShopProductController(OrderService orderService) {
        this.orderService = orderService;
    }

    private int cartNo = 0;

    @GetMapping("/detail")
    public String pageProductDetail() { return "shop/productdetail";}

    @GetMapping("/products")
    public String pageShopProducts(){
        return "shop/products";
    }

    @GetMapping("/order")
    public String pageOrderProduct(){
        return "shop/orderproduct";
    }

    @GetMapping("/ordercart")
    public String  pageOrderCart(Model model){

        List<OrderDTO> productList = orderService.findAllOrderProduct();

        model.addAttribute("productlist", productList);

        return "shop/ordercart"; }



    /**
     * 장바구니 리스트에 상품을 저장
     */
    @PostMapping("/cart")
    public String cartOrder(@ModelAttribute OrderCartDTO product, RedirectAttributes rttr){

        int totalPrice = orderService.getPriceByProductNo(product);
        System.out.println("totalPrice = " + totalPrice);
        product.setTotalPrice(totalPrice);

        log.info("[ShopProductController] cartOrder product : {}", product);
        if(product != null) {

            // 장바구니 정보가 있는지 확인
            if(cartNo == 0 ) {
                // 없다면 새로운 장바구니를 저장
                cartNo = orderService.registNewCart(product);
                product.setOrderNo(cartNo);
                rttr.addFlashAttribute("message", "새 장바구니에 저장되었습니다!");
            } else {
                // 있다면 현재 장바구니에 업데이트
                orderService.updateCart(product);
                rttr.addFlashAttribute("message", "장바구니에 추가되었습니다!");
            }
            return "redirect:/shop/ordercart";

        } else {

            rttr.addFlashAttribute("message", "장바구니 저장에 실패했습니다.");
            return "redirect:/shop/orderproduct";
        }
    }

    /**
     * 장바구니 상품을 구매
     */



}
