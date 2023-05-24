package my.project.service;

import my.project.domain.Order;
import my.project.domain.User;
import org.aspectj.weaver.ast.Or;

import java.util.List;


public interface OrderService {

    List<Order> findAll();


    Order save(Order order);

    void  sendMessage(User user, Order order);

    void feedbackMessage(User user, Order order);

    void sendStatusUpdate(User user, Order order);
    void updateStatus(Order order, String status);


    List<Order> findOrderByUser(User user);
}
