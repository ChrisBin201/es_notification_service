package com.chris.notification_service.service.kafka;


import com.chris.common.constant.MessageEvent;
import com.chris.common.utils.JsonUtil;
import com.chris.data.elasticsearch.OrderInfo;
import com.chris.data.elasticsearch.ProductInfo;
import com.chris.data.entity.order.Order;
import com.chris.data.entity.order.OrderLine;
import com.chris.notification_service.model.NotificationModel;
import com.chris.notification_service.repo.NotificationRepo;
import com.chris.notification_service.service.FirebaseMessagingService;
import com.chris.notification_service.service.OrderNotificationService;
import com.chris.notification_service.service.ProductNotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    OrderNotificationService orderNotificationService;

    @Autowired
    ProductNotificationService productNotificationService;

    @Autowired
    NotificationRepo repo;
    //LISTEN FROM ORDER SERVICE
    //TODO add listener for create order
    @KafkaListener(
            topics = {MessageEvent.CREATE_ORDER},
            groupId="es_notification_service"
    )
    public void createOrderListener( String orderJson) {
        try {
            Order order = JsonUtil.convertJsonToObject(orderJson, Order.class);
            log.info("createOrderListener [{}]", order);
            if(order.getStatus().name().equals(Order.OrderStatus.CONFIRM_PENDING.name()))
                orderNotificationService.notifyCreateOrder(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @KafkaListener(
            topics = {MessageEvent.UPDATE_ORDER_STATUS},
            groupId="es_notification_service"
    )
    public void updateOrderStatusListener( String orderJson) {
        try {
            Order order = JsonUtil.convertJsonToObject(orderJson, Order.class);
            log.info("updateOrderStatusListener [{}]", order);
            String statusName = order.getStatus().name();

            if (statusName.equals(Order.OrderStatus.CONFIRM_PENDING.name())) {
                orderNotificationService.notifyUpdateStatusOrderToSeller(order, NotificationModel.Type.NEW_ORDER);
            } else if (statusName.equals(Order.OrderStatus.SHIPMENT_PENDING.name())) {
                orderNotificationService.notifyUpdateStatusOrderToCustomer(order, NotificationModel.Type.CONFIRM_ORDER);
            } else if (statusName.equals(Order.OrderStatus.DELIVERING.name())) {
                orderNotificationService.notifyUpdateStatusOrderToCustomer(order, NotificationModel.Type.SHIPPING_ORDER);
            }
//            else if (statusName.equals(Order.OrderStatus.RATING_PENDING.name())) {
//                orderNotificationService.notifyUpdateStatusOrderToSeller(order, NotificationModel.Type.RECEIVE_ORDER);
//            }
            else if (statusName.equals(Order.OrderStatus.COMPLETE.name())) {
//                orderNotificationService.notifyUpdateStatusOrderToSeller(order, NotificationModel.Type.NEW_RATING);
                orderNotificationService.notifyUpdateStatusOrderToSeller(order, NotificationModel.Type.RECEIVE_ORDER);
            } else if (statusName.equals(Order.OrderStatus.CANCEL.name())) {
                orderNotificationService.notifyUpdateStatusOrderToSeller(order, NotificationModel.Type.CANCEL_ORDER);
            } else {
                // Default case
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    //LISTEN FROM PRODUCT SERVICE
    @KafkaListener(
            topics = {MessageEvent.UPLOAD_PRODUCT},
            groupId="es_notification_service"
    )
    public void uploadProductListener( ProductInfo product) {
        try {
            log.info("uploadProductListener [{}]", product);
            productNotificationService.notifyUploadProduct(product);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(
            topics = {MessageEvent.REVIEW_PRODUCT},
            groupId="es_notification_service"
    )
    public void reviewProductListener( ProductInfo product) {
        try {
            log.info("reviewProductListener [{}]", product);
            productNotificationService.notifyReviewProduct(product);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
