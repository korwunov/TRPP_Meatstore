package my.project.controller;

import my.project.domain.Order;
import my.project.domain.Role;
import my.project.domain.User;
import my.project.service.OrderService;
import my.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class OrderController {
    private final UserService userService;

    private final OrderService orderService;


    @Autowired
    public OrderController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public String getOrder(@AuthenticationPrincipal User userSession, Model model) {
        User userFromDB = userService.findByUsername(userSession.getUsername());
        List<Order> orders = orderService.findOrderByUser(userFromDB);
        if(!orders.isEmpty()){
            Order order = orders.get(orders.size()-1);
            model.addAttribute("lastOrder", order);
        }
        else {
            model.addAttribute("lastOrder", null);
        }
        model.addAttribute("goods", userFromDB.getGoodList());

        return "order/order";
    }


    @PostMapping("/order")
    public String postOrder(
            @AuthenticationPrincipal User userSession,
            @Valid Order validOrder,
            BindingResult bindingResult,
            Model model
    ) {
        User user = userService.findByUsername(userSession.getUsername());
        Order order = new Order(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("goods", user.getGoodList());

            return "order/order";
        } else {
            order.getGoodList().addAll(user.getGoodList());
            order.setTotalPrice(validOrder.getTotalPrice());
            order.setFirstName(validOrder.getFirstName());
            order.setLastName(validOrder.getLastName());
            order.setCity(validOrder.getCity());
            order.setAddress(validOrder.getAddress());
            order.setPostIndex(validOrder.getPostIndex());
            order.setEmail(validOrder.getEmail());
            order.setPhoneNumber(validOrder.getPhoneNumber());

            user.getGoodList().clear();

            orderService.save(order);
            orderService.sendMessage(user, order);
            orderService.feedbackMessage(user, order);

            log.debug("User {} id={} made an order: FirstName={}, LastName={}, TotalPrice={}, City={}, " +
                            "Address={}, PostIndex={}, Email={}, PhoneNumber={}",
                    user.getUsername(), user.getId(), order.getFirstName(), order.getLastName(), order.getTotalPrice(),
                    order.getCity(), order.getAddress(), order.getPostIndex(), order.getEmail(), order.getPhoneNumber());
        }

        return "redirect:/finalizeOrder";
    }


    @GetMapping("/finalizeOrder")
    public String finalizeOrder(Model model) {
        List<Order> orderList = orderService.findAll();
        Order orderIndex = orderList.get(orderList.size() - 1);

        model.addAttribute("orderIndex", orderIndex.getId());

        return "order/finalizeOrder";
    }


    @GetMapping("/userOrders")
    public String getUserOrdersList(@AuthenticationPrincipal User userSession, Model model) {
        User userFromDB = userService.findByUsername(userSession.getUsername());
        List<Order> orders = orderService.findOrderByUser(userFromDB);
        model.addAttribute("orders", orders);

        return "order/orders";
    }


    @GetMapping("/orders")
    public String getAllOrdersList(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        return "order/orders";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/order/{order}")
    public String orderEditForm(@PathVariable Order order, Model model) {
        model.addAttribute("order", order);

        return "admin/orderEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/orderupdate")
    public String userSaveEditForm(
            @RequestParam String status,
            @RequestParam("orderId") Order order
    ) {
        orderService.updateStatus(order, status);
        orderService.sendStatusUpdate(order.getUser(),order);

        log.debug("ADMIN save edited order form: id={}, user_id={}, status={}", order.getId(), order.getUser().getId(), status);

        return "redirect:/userOrders";
    }
}
