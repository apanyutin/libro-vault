package com.example.service.impl;

import com.example.dto.cart.CreateCartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.exception.ProcessingException;
import com.example.mapper.CartItemMapper;
import com.example.mapper.ShoppingCartMapper;
import com.example.model.Book;
import com.example.model.CartItem;
import com.example.model.ShoppingCart;
import com.example.repository.book.BookRepository;
import com.example.repository.cart.CartItemRepository;
import com.example.repository.cart.ShoppingCartRepository;
import com.example.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getCartByUserId(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user id = " + userId));
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
        return shoppingCartDto;
    }

    @Override
    @Transactional
    public ShoppingCartDto addCartItemToCartByUserId(
            CreateCartItemRequestDto requestDto, Long userId) {
        Long bookId = requestDto.getBookId();
        Book bookById = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id = " + bookId));

        CartItem existingCartItem = cartItemRepository.findByCartIdAndBookId(userId, bookId);
        if (existingCartItem != null) {
            throw new ProcessingException(
                    "There is no way to add the same book. You can update its quantity");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user id = " + userId));
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(bookById);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.flush();
        return getCartByUserId(userId);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItemQuantityById(
            Long cartItemId, UpdateCartItemRequestDto requestDto, Long userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem by id = " + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.flush();
        return getCartByUserId(userId);
    }

    @Override
    public void deleteCartItemById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
