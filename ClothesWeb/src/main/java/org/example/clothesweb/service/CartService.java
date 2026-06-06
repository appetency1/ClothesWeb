package org.example.clothesweb.service;

import org.example.clothesweb.dao.CartDAO;
import org.example.clothesweb.entity.Cart;

import java.util.List;

public class CartService {
    private CartDAO cartDAO = new CartDAO();

    public List<Cart> getCartList(Long userId) {
        return cartDAO.findByUserId(userId);
    }

    public boolean addToCart(Long userId, Long clothesId, String clothesSize, Integer quantity) {
        Cart existCart = cartDAO.findByUserAndClothesAndSize(userId, clothesId, clothesSize);
        if (existCart != null) {
            return cartDAO.updateQuantity(existCart.getId(), existCart.getQuantity() + quantity);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setClothesId(clothesId);
            cart.setClothesSize(clothesSize);
            cart.setQuantity(quantity);
            return cartDAO.insert(cart);
        }
    }

    public boolean updateCartQuantity(Long cartId, Integer quantity) {
        if (quantity <= 0) {
            return cartDAO.delete(cartId);
        }
        return cartDAO.updateQuantity(cartId, quantity);
    }

    public boolean removeFromCart(Long cartId) {
        return cartDAO.delete(cartId);
    }

    public boolean clearCart(Long userId) {
        return cartDAO.deleteByUserId(userId);
    }
}
