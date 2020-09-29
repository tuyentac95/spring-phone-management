package com.codegym.controller;

import com.codegym.model.Cart;
import com.codegym.model.Smartphone;
import com.codegym.service.SmartphoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/smartphones")
public class SmartphoneController {
    @Autowired
    private SmartphoneService smartphoneService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createSmartphonePage(){
        ModelAndView modelAndView = new ModelAndView("phones/new-phone");
        modelAndView.addObject("sPhone", new Smartphone());
        return modelAndView;
    }

    @RequestMapping(value = "/createNewPhone", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Smartphone createSmartphone(@RequestBody Smartphone smartphone){
        return smartphoneService.save(smartphone);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Iterable<Smartphone> allPhones(){
        return smartphoneService.findAll();
    }

    @GetMapping("")
    public ModelAndView allPhonesPage(){
        ModelAndView modelAndView = new ModelAndView("phones/all-phone");
        modelAndView.addObject("allphones", allPhones());
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editSmartphonePage(@PathVariable int id) {
        ModelAndView mav = new ModelAndView("phones/edit-phone");
        Smartphone smartphone = smartphoneService.findById(id);
        mav.addObject("sPhone", smartphone);
        return mav;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Smartphone editSmartphone(@PathVariable int id, @RequestBody Smartphone smartphone) {
        smartphone.setId(id);
        return smartphoneService.save(smartphone);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Smartphone deleteSmartphone(@PathVariable Integer id){
        return smartphoneService.remove(id);
    }

    @GetMapping("/shop")
    public ModelAndView showAllPhones(){
        ModelAndView modelAndView = new ModelAndView("phones/test-shopping-cart");
        modelAndView.addObject("allPhones",allPhones());
        return modelAndView;
    }

    @RequestMapping("/shop/cart")
    public ModelAndView showCart(HttpSession session){
        ModelAndView modelAndView = new ModelAndView("phones/test-cart");
        double total = (double) session.getAttribute("total");
        List<Cart> cartList = (List<Cart>) session.getAttribute("cart");
        modelAndView.addObject("total",total);
        modelAndView.addObject("listCart",cartList);
        return modelAndView;
    }


    @RequestMapping("/shop/cart/add")
    public ModelAndView addCart(@RequestParam("id") int id, HttpSession session){
        ModelAndView modelAndView = new ModelAndView("redirect:/smartphones/shop/cart");
        Smartphone smartphone = smartphoneService.findById(id);
        Cart cart = new Cart();
        List<Cart> cartList = (List<Cart>) session.getAttribute("cart");
        if(cartList == null){
            cartList = new ArrayList<>();
        }
        if(smartphone != null){
            cart.ToCart(smartphone);
            double total = addToCart(cartList, cart);
            session.setAttribute("total",total);
            session.setAttribute("cart",cartList);
        }
        return modelAndView;
    }

    private double addToCart(List<Cart> cartList, Cart cart) {
        double total = 0;
        boolean isExist = false;
        for (Cart c : cartList){
            if(c.equals(cart)){
                c.setQuantity(c.getQuantity() + 1);
                isExist = true;
            }
            total = total + (c.getPrice() * c.getQuantity());
        }
        if(!isExist) {
            cartList.add(cart);
            total = total + (cart.getPrice() * cart.getQuantity());
        }
        return total;
    }

    @RequestMapping("/shop/cart/remove")
    public ModelAndView removeCart(@RequestParam int id, HttpSession session){
        ModelAndView modelAndView = new ModelAndView("redirect:/smartphones/shop/cart");
        List<Cart> cartList = (List<Cart>) session.getAttribute("cart");
        if(cartList != null){
            double total = removeCartItem(cartList, id);
            session.setAttribute("total", total);
            session.setAttribute("cart",cartList);
        }
        return modelAndView;
    }

    private double removeCartItem(List<Cart> cartList, int id) {
        double total = 0;
        Cart temp = null;
        for (Cart c : cartList){
            if(c.getId() == id){
                temp = c;
                continue;
            }
            total = total + (c.getPrice() * c.getQuantity());
        }
        if(temp != null){
            cartList.remove(temp);
        }
        return total;
    }
}
