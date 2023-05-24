package my.project.service.Impl;

import my.project.domain.Order;
import my.project.domain.User;
import my.project.repos.OrderRepository;
import my.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;



@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final MailSenderService mailSenderService;

    @Value("${hostname}")
    private String hostname;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, MailSenderService mailSenderService) {
        this.orderRepository = orderRepository;
        this.mailSenderService = mailSenderService;
    }


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }


    @Override
    public Order save(Order order) {
        order.setStatus("На рассмотрение");
        return orderRepository.save(order);
    }

    @Override
    public void sendMessage(User user, Order order) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Спасибо за заказ, %s! \n " +
                            "Скоро вам позвонит оператор для подтверждения.\n" +
                            "Ваш заказ: \n %s \n" +
                            "Сумма заказа: %s",
                    user.getUsername(),
                    order.getGoodList().toString(),
                    order.getTotalPrice()
            );
            mailSenderService.send(user.getEmail(), "Новый заказ", message);
        }
    }

    @Override
    public void sendStatusUpdate(User user, Order order) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Здраствуйте, %s! \n " +
                            "Статус вашего заказа изменился на: %s\n" +
                            "Ваш заказ: \n %s \n" +
                            "Сумма заказа: %s",
                    user.getUsername(),
                    order.getStatus(),
                    order.getGoodList().toString(),
                    order.getTotalPrice()
            );
            mailSenderService.send(user.getEmail(), "Изменение статуса заказа", message);
        }
    }
    @Override
    public void feedbackMessage(User user, Order order) {
        String message = String.format("Имя пользователя, %s! \n " +
                        "Номер заказа: %s\n" +
                        "Номер телефона: %s \n" +
                        "Имя: %s" +
                        " Фамилия: %s" +
                        " Адресс: %s",
                user.getUsername(),
                order.getId(),
                order.getPhoneNumber(),
                order.getFirstName(),
                order.getLastName(),
                order.getAddress()
            );
        mailSenderService.send(System.getenv("MAIL_ADMIN"), "Пользователь создал новый заказ", message);
    }

    @Override
    public void updateStatus(Order order, String status) {
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public List<Order> findOrderByUser(User user) {
        return orderRepository.findOrderByUser(user);
    }
}
