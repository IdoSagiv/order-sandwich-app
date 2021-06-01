package idosa.huji.postpc.sandwich_stand;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.EventListener;

public class LocalDb {
    private static final String ORDERS_COLLECTION = "orders";
    private static final String SP_DB = "sandwich_stand_local_db";
    private static final String SP_CURRENT_ORDER = "current_order_id";
    private final SharedPreferences sp;
    private final FirebaseFirestore db;

    private SandwichOrder currentOrder = null;
    private final MutableLiveData<SandwichOrder> currentOrderMutableLD = new MutableLiveData<>();
    private final LiveData<SandwichOrder> currentOrderLD = currentOrderMutableLD;

    private ListenerRegistration currListener = null;

    private final com.google.firebase.firestore.EventListener<DocumentSnapshot> orderChangedEventListener = (value, error) -> {
        if (error != null) {
            //error
        } else if (value == null || !value.exists()) {
            //order deleted
            deleteLocalCurrentOrder();
        } else {
            // update local data base
            currentOrder = value.toObject(SandwichOrder.class);
            currentOrderMutableLD.setValue(currentOrder);
            updateSp(currentOrder.getId());
        }
    };

    public LocalDb(Context context) {
        this.sp = context.getSharedPreferences(SP_DB, Context.MODE_PRIVATE);
        this.db = FirebaseFirestore.getInstance();
        String currentOrderId = sp.getString(SP_CURRENT_ORDER, null);

        if (currentOrderId != null) {
            downloadOrder(currentOrderId);
        }
    }

    private void downloadOrder(String orderId) {
        db.collection(ORDERS_COLLECTION).document(orderId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // update locally
                currentOrder = documentSnapshot.toObject(SandwichOrder.class);
                currentOrderMutableLD.setValue(currentOrder);
                // set updates listener
                currListener = db.collection(ORDERS_COLLECTION).document(currentOrder.getId()).addSnapshotListener(orderChangedEventListener);
            } else {
                // todo: local or also from db?
                deleteLocalCurrentOrder();
            }
        });
    }

    public void addOrder(SandwichOrder newOrder) {
        db.collection(ORDERS_COLLECTION).document(newOrder.getId()).set(newOrder);
        currListener = db.collection(ORDERS_COLLECTION).document(currentOrder.getId()).addSnapshotListener(orderChangedEventListener);
    }

    public void deleteCurrentOrder() {
        if (currentOrder == null) return;
        // todo: remove listener
        if (currListener != null) currListener.remove();
        db.collection(ORDERS_COLLECTION).document(currentOrder.getId()).delete();
    }

    public void UpdateCurrentOrder(SandwichOrder newOrder) {
        if (currentOrder == null || !newOrder.getId().equals(currentOrder.getId())) return;
        db.collection(ORDERS_COLLECTION).document(currentOrder.getId()).set(newOrder);
        // todo: stop listening for done orders?
//        if (newOrder.getStatus() == SandwichOrder.OrderStatus.DONE && currListener != null)
//            currListener.remove();
    }

    public SandwichOrder getCurrentOrder() {
        return currentOrder;
    }

    public LiveData<SandwichOrder> getCurrentOrderLD() {
        return currentOrderLD;
    }

    private void deleteLocalCurrentOrder() {
        currentOrder = null;
        updateSp(null);
        currentOrderMutableLD.setValue(currentOrder);
    }

    private void updateSp(@Nullable String newOrderId) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString(SP_CURRENT_ORDER, newOrderId);
        spEditor.apply();
    }
}
