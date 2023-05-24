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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final GoodService goodService;

    @Autowired
    public MenuController(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping
    public String mainMenu(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 8) Pageable pageable, Model model) {
        Page<Good> page = goodService.findAll(pageable);
        int[] pagination = ControllerUtils.computePagination(page);
        getMinMaxGoodPrice(model);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/menu");
        model.addAttribute("page", page);

        return "menu";
    }

    @GetMapping("{producer}")
    public String findByProducer(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 8) Pageable pageable,
            @PathVariable String producer,
            Model model
    ) {
        Page<Good> goodList = goodService.findByProducer(producer, pageable);
        int[] pagination = ControllerUtils.computePagination(goodList);
        getMinMaxGoodPrice(model);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/menu/" + producer);
        model.addAttribute("page", goodList);

        return "menu";
    }

    @GetMapping("breed/{breed}")
    public String findByBreed(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 8) Pageable pageable,
            @PathVariable("breed") String breed,
            Model model
    ) {
        Page<Good> goods = goodService.findByBreed(breed, pageable);
        int[] pagination = ControllerUtils.computePagination(goods);
        getMinMaxGoodPrice(model);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/menu/breed/" + breed);
        model.addAttribute("page", breed);

        return "menu";
    }

    @GetMapping("search")
    public String searchByParameters(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 8) Pageable pageable,
            @RequestParam(value = "breed", required = false, defaultValue = "") List<String> breed,
            @RequestParam(value = "type", required = false, defaultValue = "") List<String> type,
            @RequestParam(value = "producers", required = false, defaultValue = "") List<String> producers,
            @RequestParam(value = "startingPrice", required = false, defaultValue = "0") Integer startingPrice,
            @RequestParam(value = "endingPrice", required = false, defaultValue = "0") Integer endingPrice,
            Model model
    ) {
        StringBuilder urlBuilder = new StringBuilder();
        Page<Good> goodsSearch = null;
        getMinMaxGoodPrice(model);

        if (breed.isEmpty() && producers.isEmpty() && type.isEmpty()) {
            Page<Good> priceRange = goodService.findByPriceBetween(startingPrice, endingPrice, pageable);
            int[] pagination = ControllerUtils.computePagination(priceRange);

            model.addAttribute("pagination", pagination);
            model.addAttribute("url", "/menu/search?startingPrice=" + startingPrice + "&endingPrice=" + endingPrice);
            model.addAttribute("page", priceRange);

            return "menu";
        }

        if (breed.isEmpty() && type.isEmpty()) {
            goodsSearch = goodService.findByProducerIn(producers, pageable);
            urlBuilder = ControllerUtils.getUrlBuilder(producers);
        } else if (producers.isEmpty() && type.isEmpty()) {
            goodsSearch = goodService.findByBreedIn(breed, pageable);
            urlBuilder = ControllerUtils.getUrlBuilder(breed);
        } else if (producers.isEmpty() && breed.isEmpty()){
            goodsSearch = goodService.findByTypeIn(type, pageable);
            urlBuilder = ControllerUtils.getUrlBuilder(type);
        }
        else if (!breed.isEmpty() && !type.isEmpty()){
            goodsSearch = goodService.findByTypeInAndBreedIn(type, breed, pageable);
            List<String> urlArray = new ArrayList<String>(type);
            urlArray.addAll(breed);
            urlBuilder = ControllerUtils.getUrlBuilder(urlArray);
        }
        else if (!producers.isEmpty() && !type.isEmpty()){
            goodsSearch = goodService.findByProducerInAndTypeIn(producers, type, pageable);
            List<String> urlArray = new ArrayList<String>(producers);
            urlArray.addAll(type);
            urlBuilder = ControllerUtils.getUrlBuilder(urlArray);
        }
        else if (!breed.isEmpty() && !producers.isEmpty()) {
            goodsSearch = goodService.findByProducerInAndBreedIn(producers, breed, pageable);
            List<String> urlArray = new ArrayList<String>(producers);
            urlArray.addAll(breed);
            urlBuilder = ControllerUtils.getUrlBuilder(urlArray);
        }

        else if (!breed.isEmpty() && !type.isEmpty() && !producers.isEmpty()){
            goodsSearch = goodService.findByProducerInAndBreedInAndTypeIn(producers, breed, type, pageable);
            List<String> urlArray = new ArrayList<String>(producers);
            urlArray.addAll(breed);
            urlArray.addAll(type);
            urlBuilder = ControllerUtils.getUrlBuilder(urlArray);
        }

        int[] pagination = ControllerUtils.computePagination(goodsSearch);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/menu/search" + urlBuilder);
        model.addAttribute("page", goodsSearch);

        return "menu";
    }

    private void getMinMaxGoodPrice(Model model) {
        BigDecimal minGoodPrice = goodService.minGoodPrice();
        BigDecimal maxGoodPrice = goodService.maxGoodPrice();

        model.addAttribute("minGoodPrice", minGoodPrice);
        model.addAttribute("maxGoodPrice", maxGoodPrice);
    }
}