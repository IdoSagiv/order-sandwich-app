package idosa.huji.postpc.sandwich_stand;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class WaitForOrderActivity extends AppCompatActivity {

    // in tests can inject value
    @VisibleForTesting
    public LocalDb db = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_order);

        if (db == null) {
            db = SandwichStandApp.getLocalDb();
        }

        LiveData<SandwichOrder> orderLiveData = db.getCurrentOrderLD();
        orderLiveData.observe(this,
                sandwichOrder -> {
                    if (sandwichOrder == null) {
                        // todo: do something??
                    } else if (sandwichOrder.getOrderStatus() == SandwichOrder.OrderStatus.READY) {
                        startActivity(new Intent(this, OrderReadyActivity.class));
                        orderLiveData.removeObservers(this);
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    super.onBackPressed();
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Close the app?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
