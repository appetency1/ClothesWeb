package org.example.clothesweb.service;

import org.example.clothesweb.dao.OrderDAO;
import org.example.clothesweb.entity.Order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO = new OrderDAO();

    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return "ORD" + sdf.format(new Date());
    }

    public Order createOrder(Long userId, String clothesDetails, BigDecimal totalAmount, String address) {
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setClothesDetails(clothesDetails);
        order.setTotalAmount(totalAmount);
        order.setAddress(address);
        order.setStatus(0);

        if (orderDAO.insert(order)) {
            return orderDAO.findByUserId(userId, null).get(0);
        }
        throw new RuntimeException("创建订单失败");
    }

    public List<Order> getUserOrders(Long userId, Integer status) {
        return orderDAO.findByUserId(userId, status);
    }

    public List<Order> getAllOrders(Integer status, String username) {
        return orderDAO.findAll(status, username);
    }

    public Order getOrderDetail(Long id) {
        return orderDAO.findById(id);
    }

    public boolean payOrder(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null || order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }
        return orderDAO.updateStatus(orderId, 1);
    }

    public boolean shipOrder(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null || order.getStatus() != 1) {
            throw new RuntimeException("订单状态不正确");
        }
        return orderDAO.updateStatus(orderId, 2);
    }

    public boolean confirmReceive(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null || order.getStatus() != 2) {
            throw new RuntimeException("订单状态不正确");
        }
        return orderDAO.updateStatus(orderId, 3);
    }

    public boolean deleteOrder(Long orderId) {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != 0 && order.getStatus() != 3) {
            throw new RuntimeException("当前状态不能删除订单");
        }
        return orderDAO.delete(orderId);
    }
}
