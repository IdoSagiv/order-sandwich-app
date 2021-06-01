package idosa.huji.postpc.sandwich_stand;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.google.firestore.v1.StructuredQuery;

import org.jetbrains.annotations.NotNull;

public class NewOrderActivity extends AppCompatActivity {
    private EditText customerNameEditText;
    private Slider numPicklesSlider;
    private CheckBox isHummusCheckBox;
    private CheckBox isTahiniCheckBox;
    private EditText orderCommentsEditText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_new_order);

        customerNameEditText = findViewById(R.id.editTextCustomerNameInNewOrder);
        numPicklesSlider = findViewById(R.id.sliderNumOfPicklesInNewOrder);
        isHummusCheckBox = findViewById(R.id.checkBoxIsHummusInNewOrder);
        isTahiniCheckBox = findViewById(R.id.checkBoxIsTahiniInNewOrder);
        orderCommentsEditText = findViewById(R.id.editTextOrderCommentInNewOrder);

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

        Button saveOrderBtn = findViewById(R.id.buttonPlaceOrderInNewOrder);
        saveOrderBtn.setOnClickListener(v -> {
            if (customerNameEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "enter customer name", Toast.LENGTH_SHORT);
                return;
            }
            SandwichOrder newOrder = new SandwichOrder(customerNameEditText.getText().toString(),
                    (int) numPicklesSlider.getValue(),
                    isHummusCheckBox.isChecked(),
                    isTahiniCheckBox.isChecked(),
                    orderCommentsEditText.getText().toString());
            SandwichStandApp.getLocalDb().addOrder(newOrder);
            startActivity(new Intent(this, EditOrderActivity.class));
        });
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
}
