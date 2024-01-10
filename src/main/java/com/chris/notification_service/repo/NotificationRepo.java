package com.chris.notification_service.repo;

import com.chris.notification_service.model.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<NotificationModel,Long> {
}
