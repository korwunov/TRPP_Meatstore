package my.project.controller;

import my.project.domain.Good;
import my.project.domain.User;
import my.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CartController {

    private final UserService userService;

    @Autowired
    public CartController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String getCart(@AuthenticationPrincipal User userSession, Model model) {
        User userFromDB = userService.findByUsername(userSession.getUsername());
        model.addAttribute("goods", userFromDB.getGoodList());

        return "cart";
    }


    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("add") Good good,
            @AuthenticationPrincipal User userSession
    ) {
        User user = userService.findByUsername(userSession.getUsername());
        user.getGoodList().add(good);
        userService.save(user);

        return "redirect:/cart";
    }

    @PostMapping("/cart/back")
    public String addToCartAndBack(
            @RequestParam("add") Good good,
            @AuthenticationPrincipal User userSession
    ) {
        User user = userService.findByUsername(userSession.getUsername());
        user.getGoodList().add(good);
        userService.save(user);

        return "redirect:/menu";
    }


    @PostMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam(value = "goodId") Good good,
            @AuthenticationPrincipal User userSession
    ) {
        User user = userService.findByUsername(userSession.getUsername());

        if (good != null) {
            user.getGoodList().remove(good);
        }

        userService.save(user);

        return "redirect:/cart";
    }
}