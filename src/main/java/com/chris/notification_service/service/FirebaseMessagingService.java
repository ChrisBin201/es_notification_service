package com.chris.notification_service.service;

import com.chris.notification_service.model.NotificationModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseMessagingService  {
//    private final FirebaseMessaging firebaseMessaging;
    private static final String COLLECTION_NAME = "notifications";

//    protected FirebaseMessagingService(Firestore firestore, String collection) {
//        super(firestore, collection);
//    }

//    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
//        this.firebaseMessaging = firebaseMessaging;
//    }


//    public String sendNotification(Note note, String token) throws FirebaseMessagingException {
//
//        Notification notification = Notification
//                .builder()
//                .setTitle(note.getSubject())
//                .setBody(note.getContent())
//                .build();
//
//        Message message = Message
//                .builder()
//                .setToken(token)
//                .setNotification(notification)
//                .putAllData(note.getData())
//                .build();
//
//        return firebaseMessaging.send(message);
//    }

    public String sendNotification(NotificationModel notification) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(notification.getId().toString()).create(notification);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public boolean readNotification(List<Long> ids) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME)
                .whereIn("id", ids)
                .getFirestore().runTransaction(transaction -> {
                    for (Long id : ids) {
                        transaction.update(dbFirestore.collection(COLLECTION_NAME).document(id.toString()), "status", NotificationModel.Status.SEEN);
                    }
                    return null;
                });
        return true;
    }

}
