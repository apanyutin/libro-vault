package com.example.dto.cart;

import com.example.model.CartItem;
import com.example.model.User;
import jakarta.persistence.Column;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private User user;
    private Set<CartItem> cartItems;
}
