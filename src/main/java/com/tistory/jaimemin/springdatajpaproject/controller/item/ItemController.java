package com.tistory.jaimemin.springdatajpaproject.controller.item;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tistory.jaimemin.springdatajpaproject.domain.item.Book;
import com.tistory.jaimemin.springdatajpaproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", itemService.findItems());

        return "items/itemList";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());

        return "items/createItemForm";
    }

    @PostMapping("/new")
    public String create(BookForm form) {
        itemService.saveItem(getBook(form));

        return "redirect:/";
    }

    @GetMapping("/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        model.addAttribute("form", getForm(itemId));

        return "items/updateItemForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId
            , @ModelAttribute("form") BookForm form) {
        if (form.getId() != itemId) {
            throw new IllegalArgumentException("수정할 아이템과 수정된 아이템이 다릅니다.");
        }

        // itemService.saveItem(convertBook(form));
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

        return "redirect:/items";
    }

    private Book convertBook(BookForm form) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        return objectMapper.convertValue(form, Book.class);
    }

    private BookForm getForm(Long itemId) {
        Book book = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(book.getId());
        form.setName(book.getName());
        form.setPrice(book.getPrice());
        form.setStockQuantity(book.getStockQuantity());
        form.setAuthor(book.getAuthor());
        form.setIsbn(book.getIsbn());

        return form;
    }

    private Book getBook(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        return book;
    }
}
