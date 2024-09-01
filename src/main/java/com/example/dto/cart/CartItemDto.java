package com.example.dto.cart;

import com.example.model.Book;
import com.example.model.ShoppingCart;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long id;
    private ShoppingCart shoppingCart;
    private Book book;
    private int quantity;
}
