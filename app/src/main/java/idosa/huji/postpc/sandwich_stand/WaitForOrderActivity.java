package idosa.huji.postpc.sandwich_stand;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

public class WaitForOrderActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_order);

        LocalDb db = SandwichStandApp.getLocalDb();
        LiveData<SandwichOrder> orderLiveData = db.getCurrentOrderLD();
        orderLiveData.observe(this,
                sandwichOrder -> {
                    if (sandwichOrder == null) {
                        // todo: do something??
                    } else if (sandwichOrder.getStatus() == SandwichOrder.OrderStatus.READY) {
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
