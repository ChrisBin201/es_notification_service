package com.chris.notification_service.service;

import com.chris.data.entity.order.Order;
import com.chris.data.entity.order.OrderLine;
import com.chris.notification_service.model.NotificationModel;
import com.chris.notification_service.repo.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class OrderNotificationService {

    @Autowired
    FirebaseMessagingService firebaseMessagingService;

    @Autowired
    NotificationRepo notificationRepo;

    public void notifyCreateOrder(Order orderLine) throws ExecutionException, InterruptedException {
        //send notification to seller that they have a new order (notify to firebase)
        NotificationModel notification = NotificationModel.builder()
                .objectReceiveId(orderLine.getSellerId())
                .objectSendId(orderLine.getInvoice().getCustomerId())
                .status(NotificationModel.Status.UNSEEN)
                .type(NotificationModel.Type.NEW_ORDER)
                .message(NotificationModel.Type.NEW_ORDER.message)
                .dataId(String.valueOf(orderLine.getId()))
                .build();
        notification = notificationRepo.save(notification);
        firebaseMessagingService.sendNotification(notification);
    }

    public void notifyUpdateStatusOrderToCustomer(Order orderLine, NotificationModel.Type type) throws ExecutionException, InterruptedException {
        //send notification to seller that they have a new order (notify to firebase)
        NotificationModel notification = NotificationModel.builder()
                .objectReceiveId(orderLine.getInvoice().getCustomerId())
                .objectSendId(orderLine.getSellerId())
                .status(NotificationModel.Status.UNSEEN)
                .type(type)
                .message(type.message)
                .dataId(String.valueOf(orderLine.getId()))
                .build();
        notification = notificationRepo.save(notification);
        firebaseMessagingService.sendNotification(notification);
    }

    public void notifyUpdateStatusOrderToSeller(Order orderLine, NotificationModel.Type type) throws ExecutionException, InterruptedException {
        //send notification to seller that they have a new order (notify to firebase)
        NotificationModel notification = NotificationModel.builder()
                .objectReceiveId(orderLine.getSellerId())
                .objectSendId(orderLine.getInvoice().getCustomerId())
                .status(NotificationModel.Status.UNSEEN)
                .type(type)
                .message(type.message)
                .dataId(String.valueOf(orderLine.getId()))
                .build();
        notification = notificationRepo.save(notification);

        firebaseMessagingService.sendNotification(notification);
    }

    public void notifyCreateRating(Order orderLine) throws ExecutionException, InterruptedException {
        //send notification to seller that they have a new order (notify to firebase)
        NotificationModel notification = NotificationModel.builder()
                .objectReceiveId(orderLine.getSellerId())
                .objectSendId(orderLine.getInvoice().getCustomerId())
                .status(NotificationModel.Status.UNSEEN)
                .type(NotificationModel.Type.NEW_RATING)
                .message(NotificationModel.Type.NEW_RATING.message)
                .dataId(String.valueOf(orderLine.getId()))
                .build();
        notification = notificationRepo.save(notification);

        firebaseMessagingService.sendNotification(notification);
    }


}
