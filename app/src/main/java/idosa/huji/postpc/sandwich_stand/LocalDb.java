package idosa.huji.postpc.sandwich_stand;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.FirebaseFirestore;

public class LocalDb {
    private String currentOrderId;
    private final SharedPreferences sp;
    private final FirebaseFirestore db;


    public LocalDb(Context context) {
        this.sp = context.getSharedPreferences("sandwich_stand_local_db", Context.MODE_PRIVATE);
        this.currentOrderId = sp.getString("current_order_id", null);
        this.db = FirebaseFirestore.getInstance();
    }
}
