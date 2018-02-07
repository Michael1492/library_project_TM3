package com.lxdnz.bit794.tm3.library_project.web;

import com.lxdnz.bit794.tm3.library_project.persistence.model.concrete.Item;
import com.lxdnz.bit794.tm3.library_project.persistence.model.concrete.Loan;
import com.lxdnz.bit794.tm3.library_project.persistence.model.concrete.Reservation;
import com.lxdnz.bit794.tm3.library_project.persistence.model.concrete.User;
import com.lxdnz.bit794.tm3.library_project.services.ItemService;

import com.lxdnz.bit794.tm3.library_project.services.LoanService;
import com.lxdnz.bit794.tm3.library_project.services.ReserveService;
import com.lxdnz.bit794.tm3.library_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
public class ItemController {

    private ItemService itemService;
    private ReserveService reserveService;
    private LoanService loanService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setReserveService(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @Autowired
    public void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String listItem(Model model) {
        model.addAttribute("items", itemService.listAll());
        return "items";
    }

    @RequestMapping("item/{id}")
    public String showItem(@PathVariable Long id, Model model){
        model.addAttribute("item", itemService.getById(id));
        return "itemshow";
    }

    @RequestMapping("item/edit/{id}")
    public String editItem(@PathVariable Long id, Model model){
        model.addAttribute("item", itemService.getById(id));
        return "edititem";
    }

    @RequestMapping(value = "item", method = RequestMethod.POST)
    public String saveItem(Item item){
        itemService.saveOrUpdate(item);
        return "redirect:/item/" + item.getId();
    }

    @RequestMapping("item/delete/{id}")
    public String deleteItem(@PathVariable Long id){
        itemService.delete(id);
        return "redirect:/items";
    }

    @RequestMapping("item/reserve/{id}")
    public String reserveItem(@PathVariable Long id) {
        Item reserveItem = itemService.getById(id);
        Authentication auth = getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User reserveUser = userService.findByUsername(name);
        Reservation reservation = new Reservation(reserveItem, reserveUser);
        reserveItem.setReserved(true);
        itemService.saveOrUpdate(reserveItem);
        reserveService.saveOrUpdate(reservation);

        return "redirect:/";
    }

    @RequestMapping("item/checkout/{id}")
    public String checkoutItem(@PathVariable Long id) {
        Item checkoutItem = itemService.getById(id);
        Authentication auth = getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User checkoutUser = userService.findByUsername(name);
        Loan newLoan = new Loan(checkoutItem, checkoutUser);
        checkoutItem.setRented(true);
        itemService.saveOrUpdate(checkoutItem);
        loanService.saveOrUpdate(newLoan);
        return "redirect:/";
    }

    @RequestMapping("item/return/{id}")
    public String returnItem(@PathVariable Long id) {
        Item returnItem = itemService.getById(id);
        Loan returnLoan = loanService.getByItemID(returnItem.getId());
        returnItem.setRented(false);
        itemService.saveOrUpdate(returnItem);
        // check for reservation here
        loanService.delete(returnLoan.getId());

        return "redirect:/";
    }

}