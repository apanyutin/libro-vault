package com.example.controller;

import com.example.dto.cart.CreateCartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemRequestDto;
import com.example.model.User;
import com.example.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get cart", description = "Get cart of user")
    @GetMapping
    public ShoppingCartDto getUserShoppingCart(Authentication authentication) {
        Long userId = getUserIdByAuthentication(authentication);
        return shoppingCartService.getCartByUserId(userId);
    }

    @Operation(summary = "Add book to cart", description = "Add book to the shopping cart")
    @PostMapping
    public ShoppingCartDto addBookToShoppingCart(
            @RequestBody @Valid CreateCartItemRequestDto requestDto,
            Authentication authentication) {
        Long userId = getUserIdByAuthentication(authentication);
        return shoppingCartService.addCartItemToCartByUserId(requestDto, userId);
    }

    @Operation(summary = "Update book quantity", description = "Update book quantity in cart")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateBookQuantityByCartItemId(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto,
            Authentication authentication) {
        Long userId = getUserIdByAuthentication(authentication);
        return shoppingCartService.updateCartItemQuantityById(cartItemId, requestDto, userId);
    }

    @Operation(summary = "Delete cartItem from cart", description = "Remove a book from cart")
    @DeleteMapping("/items/{cartItemId}")
    public void deleteByCartItemId(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItemById(cartItemId);
    }

    private Long getUserIdByAuthentication(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
