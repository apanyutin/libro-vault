package com.example.dto.cart;

import com.example.model.CartItem;
import com.example.model.User;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShoppingCartRequestDto {
    @NotNull
    private User user;
    @NotNull
    private Set<CartItem> cartItems;
}
