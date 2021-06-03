package idosa.huji.postpc.sandwich_stand;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

public class PlaceOrderActivity extends AppCompatActivity {
    private EditText customerNameEditText;
    private Slider numPicklesSlider;
    private CheckBox isHummusCheckBox;
    private CheckBox isTahiniCheckBox;
    private EditText orderCommentsEditText;
    private Button placeOrderBtn;
    private Button saveOrderChangesBtn;
    private Button deleteOrderBtn;

    private LocalDb db;

    private SandwichOrder currentOrder = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        db = SandwichStandApp.getLocalDb();
        LiveData<SandwichOrder> orderLiveData = db.getCurrentOrderLD();
        orderLiveData.observe(this, sandwichOrder -> {
            currentOrder = sandwichOrder;
            if (currentOrder == null) {
                setScreenMode(false);
            } else if (currentOrder.getStatus() == SandwichOrder.OrderStatus.WAITING) {
                setScreenMode(true);
            } else if (currentOrder.getStatus() == SandwichOrder.OrderStatus.IN_PROGRESS) {
                startActivity(new Intent(this, WaitForOrderActivity.class));
                orderLiveData.removeObservers(this);
                finish();
            }
        });

        customerNameEditText = findViewById(R.id.editTextCustomerName);
        numPicklesSlider = findViewById(R.id.sliderNumOfPickles);
        isHummusCheckBox = findViewById(R.id.checkBoxIsHummus);
        isTahiniCheckBox = findViewById(R.id.checkBoxIsTahini);
        orderCommentsEditText = findViewById(R.id.editTextOrderComment);
        placeOrderBtn = findViewById(R.id.buttonPlaceOrder);
        saveOrderChangesBtn = findViewById(R.id.buttonSaveOrderChanges);
        deleteOrderBtn = findViewById(R.id.buttonDeleteOrder);

        // todo: load right in the beginning, or wait for update???
        setScreenMode(getIntent().getBooleanExtra("is_edit_mode", false));

        customerNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (customerNameEditText.getText().toString().isEmpty()) {
                    customerNameEditText.setError("name is required");
                }
            }
        });


        placeOrderBtn.setOnClickListener(v -> {
            if (customerNameEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "enter customer name", Toast.LENGTH_SHORT).show();
                return;
            }
            SandwichOrder newOrder = new SandwichOrder(customerNameEditText.getText().toString(),
                    (int) numPicklesSlider.getValue(),
                    isHummusCheckBox.isChecked(),
                    isTahiniCheckBox.isChecked(),
                    orderCommentsEditText.getText().toString());
            db.addOrder(newOrder);
        });

        saveOrderChangesBtn.setOnClickListener(v -> {
            if (customerNameEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "enter customer name", Toast.LENGTH_SHORT).show();
                return;
            }

            SandwichOrder order = db.getCurrentOrder();
            order.setCustomerName(customerNameEditText.getText().toString());
            order.setNumPickles((int) numPicklesSlider.getValue());
            order.setHummus(isHummusCheckBox.isChecked());
            order.setTahini(isTahiniCheckBox.isChecked());
            order.setComments(orderCommentsEditText.getText().toString());

            db.updateCurrentOrder(order);
        });

        deleteOrderBtn.setOnClickListener(v -> {
            db.deleteCurrentOrder();
            setScreenMode(false);
        });
    }


    private void setScreenMode(boolean isEditMode) {
        TextView screenTitle = findViewById(R.id.textViewPlaceOrderScreenTitle);
        if (isEditMode) {
            screenTitle.setText(R.string.edit_order_title);
            placeOrderBtn.setVisibility(View.GONE);
            saveOrderChangesBtn.setVisibility(View.VISIBLE);
            deleteOrderBtn.setVisibility(View.VISIBLE);

            SandwichOrder order = db.getCurrentOrder();
            customerNameEditText.setText(order.getCustomerName());
            numPicklesSlider.setValue(order.getNumPickles());
            isHummusCheckBox.setChecked(order.isHummus());
            isTahiniCheckBox.setChecked(order.isTahini());
            orderCommentsEditText.setText(order.getComments());
        } else {
            screenTitle.setText(R.string.new_order_title);
            saveOrderChangesBtn.setVisibility(View.GONE);
            deleteOrderBtn.setVisibility(View.GONE);
            placeOrderBtn.setVisibility(View.VISIBLE);

            customerNameEditText.setText(db.getCustomerName());
            numPicklesSlider.setValue(0);
            isHummusCheckBox.setChecked(false);
            isTahiniCheckBox.setChecked(false);
            orderCommentsEditText.setText("");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("customer_name", customerNameEditText.getText().toString());
        outState.putFloat("num_of_pickles", numPicklesSlider.getValue());
        outState.putBoolean("is_hummus", isHummusCheckBox.isChecked());
        outState.putBoolean("is_tahini", isTahiniCheckBox.isChecked());
        outState.putString("order_comments", orderCommentsEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        customerNameEditText.setText(savedInstanceState.getString("customer_name", ""));
        numPicklesSlider.setValue(savedInstanceState.getFloat("num_of_pickles", 0));
        isHummusCheckBox.setChecked(savedInstanceState.getBoolean("is_hummus", false));
        isTahiniCheckBox.setChecked(savedInstanceState.getBoolean("is_tahini", false));
        orderCommentsEditText.setText(savedInstanceState.getString("order_comments", ""));
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
