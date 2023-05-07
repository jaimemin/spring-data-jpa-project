package com.tistory.jaimemin.springdatajpaproject.controller.order;

import com.tistory.jaimemin.springdatajpaproject.repository.OrderSearch;
import com.tistory.jaimemin.springdatajpaproject.service.ItemService;
import com.tistory.jaimemin.springdatajpaproject.service.MemberService;
import com.tistory.jaimemin.springdatajpaproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final ItemService itemService;

    private final OrderService orderService;

    private final MemberService memberService;

    @GetMapping("/list")
    public String orderList(@ModelAttribute OrderSearch orderSearch, Model model) {
        model.addAttribute("orders", orderService.findOrders(orderSearch));

        return "order/orderList";
    }

    @GetMapping
    public String createForm(Model model) {
        model.addAttribute("members", memberService.findMembers());
        model.addAttribute("items", itemService.findItems());

        return "/order/orderForm";
    }

    @PostMapping
    public String order(@RequestParam Long memberId
            , @RequestParam Long itemId
            , @RequestParam int count) {
        orderService.order(memberId, itemId, count);

        return "redirect:/order/list";
    }

    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);

        return "redirect:/order/list";
    }
}
