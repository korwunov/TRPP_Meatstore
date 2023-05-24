package my.project.controller;

import my.project.domain.Good;
import my.project.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Locale;


@Controller
public class MainController {

    private final GoodService goodService;

    @Autowired
    public MainController(GoodService goodService) {
        this.goodService = goodService;
    }


    @GetMapping("/")
    public String home(Model model) {
        List<Good> goods = goodService.findAll();
        model.addAttribute("goods", goods);

        return "main";
    }

    @GetMapping("/contacts")
    public String getContacts() {
        return "contacts";
    }

    @GetMapping("/cabinet")
    public String userCabinet() {
        return "user/userCabinet";
    }

    @GetMapping("/search")
    public String search(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 12) Pageable pageable,
            @RequestParam String keyword,
            Model model
    ) {
        Page<Good> page = goodService.findByKeyword(keyword.toLowerCase(), pageable);
        int[] pagination = ControllerUtils.computePagination(page);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/menu");
        model.addAttribute("page", page);

        return "menu";
    }


    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") Good good, Model model) {
        model.addAttribute("good", good);

        return "product";
    }
}