package com.chris.notification_service.service;

import com.chris.common.caller.UserServiceCaller;
import com.chris.common.repo.redis.RedisAccessTokenRepo;
import com.chris.common.service.redis.RedisAccessTokenService;
import com.chris.data.dto.user.UserDTO;
import com.chris.data.elasticsearch.ProductInfo;
import com.chris.data.entity.order.OrderLine;
import com.chris.data.entity.product.Product;
import com.chris.data.redis.AccessToken;
import com.chris.notification_service.model.NotificationModel;
import com.chris.notification_service.repo.NotificationRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProductNotificationService {

    @Autowired
    FirebaseMessagingService firebaseMessagingService;

    @Autowired
    RedisAccessTokenService redisAccessTokenService;

    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    UserServiceCaller userServiceCaller;

    public void notifyUploadProduct(ProductInfo product) throws ExecutionException, InterruptedException {
        //send notification to seller that they have a new order (notify to firebase)
        List<UserDTO> userAdminRoles = userServiceCaller.getAllAdminRoles().block();
        if(userAdminRoles == null || userAdminRoles.isEmpty()){
            log.info("No admin role found when notify upload product");
            return;
        }
        for(UserDTO token : userAdminRoles){
            NotificationModel notification = NotificationModel.builder()
                    .objectReceiveId(token.getId())
                    .objectSendId(product.getSellerId())
                    .status(NotificationModel.Status.UNSEEN)
                    .type(NotificationModel.Type.UPLOAD_PRODUCT)
                    .message(NotificationModel.Type.UPLOAD_PRODUCT.message)
                    .dataId(String.valueOf(product.getId()))
                    .build();
            notification = notificationRepo.save(notification);
            firebaseMessagingService.sendNotification(notification);
        }
    }

    public void notifyReviewProduct(ProductInfo product) throws ExecutionException, InterruptedException {
        //send notification to seller that they have a new order (notify to firebase)
        List<UserDTO> userAdminRoles = userServiceCaller.getAllAdminRoles().block();
        if(userAdminRoles == null || userAdminRoles.isEmpty()){
            log.info("No admin role found when notify review product");
            return;
        }
        for(UserDTO token : userAdminRoles){
            NotificationModel notification = NotificationModel.builder()
                    .objectReceiveId(product.getSellerId())
                    .objectSendId(token.getId())
                    .status(NotificationModel.Status.UNSEEN)
                    .type(NotificationModel.Type.REVIEW_PRODUCT)
                    .message(NotificationModel.Type.REVIEW_PRODUCT.message)
                    .dataId(String.valueOf(product.getId()))
                    .build();
            notification = notificationRepo.save(notification);

            firebaseMessagingService.sendNotification(notification);
        }
    }

}
