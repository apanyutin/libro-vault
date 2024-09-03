package com.example.service.impl;

import com.example.dto.book.BookDto;
import com.example.dto.cart.CartItemDto;
import com.example.dto.cart.CreateCartItemRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.CartItemMapper;
import com.example.model.CartItem;
import com.example.repository.cart.CartItemRepository;
import com.example.service.BookService;
import com.example.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.save(cartItemMapper.toModel(requestDto));
        CartItemDto cartItemDto = cartItemMapper.toDto(cartItem);
        BookDto bookDto = bookService.getById(requestDto.getBookId());
        cartItemDto.setBookTitle(bookDto.getTitle());
        return cartItemDto;
    }

    @Override
    public CartItemDto updateQuantityById(Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem by id = " + id));
        cartItem.setQuantity(quantity);
        cartItemRepository.flush();
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
