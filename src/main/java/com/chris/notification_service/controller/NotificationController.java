package com.chris.notification_service.controller;

import com.chris.data.dto.ResponseData;
import com.chris.data.dto.order.RatingDTO;
import com.chris.data.entity.order.Rating;
import com.chris.notification_service.service.FirebaseMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;


    @PostMapping("/read")
    public ResponseEntity<?> readNotification(@RequestParam(name = "ids") String idsStr) {
        List<Long> ids = Arrays.stream(idsStr.split(",")).map(Long::parseLong).collect(Collectors.toList());
        try {
            ResponseData<Boolean> response = new ResponseData<>();
            boolean result =  firebaseMessagingService.readNotification(ids);
            response.initData(result);
            return ResponseEntity.ok(response);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
