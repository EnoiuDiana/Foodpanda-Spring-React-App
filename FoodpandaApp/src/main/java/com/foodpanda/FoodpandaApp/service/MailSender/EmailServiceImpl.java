package com.foodpanda.FoodpandaApp.service.MailSender;

import com.foodpanda.FoodpandaApp.model.Customer;
import com.foodpanda.FoodpandaApp.model.MenuItem;
import com.foodpanda.FoodpandaApp.model.Order;
import com.foodpanda.FoodpandaApp.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailServiceImpl {


    private JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public String buildOrderDetailsEmail(Customer customer, List<OrderItem> allOrderedItems, String address, String otherDetails) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Order for customer ")
                .append(customer.getFirstName())
                .append(" ")
                .append(customer.getLastName())
                .append("\n");
        float totalPrice = 0.0f;
        for (OrderItem orderItem : allOrderedItems) {
            totalPrice += orderItem.getMenuItemOrder().getPrice();
        }
        for (OrderItem orderItem : allOrderedItems) {
            stringBuilder.append(orderItem.getMenuItemOrder().getName())
                    .append(": ")
                    .append(orderItem.getMenuItemOrder().getPrice())
                    .append(" $")
                    .append("\n");
        }
        stringBuilder.append("Total price: ")
                .append(totalPrice)
                .append(" $")
                .append("\n");
        stringBuilder.append("Address: ").append(address).append("\n");
        stringBuilder.append("Other details: ").append(otherDetails).append("\n");

        return stringBuilder.toString();
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("diana.enoiu20@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
