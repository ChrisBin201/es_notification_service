package com.chris.notification_service.model;

import com.chris.data.entity.Auditable;
import com.chris.notification_service.config.DocumentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "notification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModel extends Auditable<String,NotificationModel> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DocumentId
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "object_receive_id")
    @JsonProperty("object_receive_id")
    private Long objectReceiveId;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(name = "object_send_id")
    @JsonProperty("object_send_id")
    private Long objectSendId;
    private String message;
    @Column(name = "data_id")
    @JsonProperty("data_id")
    private String dataId;
    @Enumerated(EnumType.STRING)
    private Status status;
    public enum  Type {


        NEW_COMMENT(""),
        NEW_RATING("A product has a new rating"),
        NEW_ORDER("You have a new order"),
        CONFIRM_ORDER("Your order has been confirmed"),
        CANCEL_ORDER("Your order has been canceled"),
        SHIPPING_ORDER("Your order has been shipped"),
        RECEIVE_ORDER("An order has been received"),
        REVIEW_ORDER("An order has been reviewed"),
        UPLOAD_PRODUCT("A product has been uploaded"),
        REVIEW_PRODUCT("A product has been reviewed");
        public  String message;

        Type(String message) {
            this.message = message;
        }
    }

    public enum  Status {
        SEEN, UNSEEN
    }
}
