package com.example.repository.cart;

import com.example.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.shoppingCart.id= ?1 AND c.book.id= ?2")
    CartItem findByCartIdAndBookId(Long cartId, Long bookId);
}
