package idosa.huji.postpc.sandwich_stand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OrderReadyActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ready);
        LocalDb db = SandwichStandApp.getLocalDb();
        Button finishOrderBtn = findViewById(R.id.buttonFinishOrder);
        finishOrderBtn.setOnClickListener(v -> {
            SandwichOrder order = db.getCurrentOrder();
            order.changeStatus(SandwichOrder.OrderStatus.DONE);
            db.updateCurrentOrder(order);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}