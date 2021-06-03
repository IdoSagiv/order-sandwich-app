package idosa.huji.postpc.sandwich_stand;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.slider.Slider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, application = Application.class)
public class PlaceOrderActivityTest {

    private LocalDb dbMockHolder;
    private SandwichOrder orderMockHolder;
    private MutableLiveData<SandwichOrder> orderLiveData;
    private ActivityController<PlaceOrderActivity> activityController;

    @Before
    public void setup() {
        dbMockHolder = Mockito.mock(LocalDb.class);
        orderLiveData = new MediatorLiveData<>();
        // when asking the `dbMockHolder` to get the current order, return our 'orderMockHolder'
        Mockito.when(dbMockHolder.getCurrentOrder()).thenReturn(orderMockHolder);
        // when asking the `dbMockHolder` to get the current order, return our 'ldMockHolder'
        Mockito.when(dbMockHolder.getCurrentOrderLD()).thenReturn(orderLiveData);

        activityController = Robolectric.buildActivity(PlaceOrderActivity.class);

        // let the activity use our `dbMockHolder` as the db
        PlaceOrderActivity activityUnderTest = activityController.get();
        activityUnderTest.db = dbMockHolder;
    }

    @Test
    public void when_openInNewOrderMode_than_viewsAreOnNewOrderMode() {
        String customerName = "ido";
        PlaceOrderActivity activityUnderTest = activityController.get();
        Mockito.when(dbMockHolder.getCustomerName()).thenReturn(customerName);
        orderLiveData.setValue(null);

        // set activity with newOrderMode intent
        Intent newOrderModeIntent = new Intent();
        newOrderModeIntent.putExtra("is_edit_mode", false);
        activityUnderTest.setIntent(newOrderModeIntent);

        activityUnderTest.onCreate(new Bundle());

        EditText customerNameEditText = activityUnderTest.findViewById(R.id.editTextCustomerName);
        Slider numPicklesSlider = activityUnderTest.findViewById(R.id.sliderNumOfPickles);
        CheckBox isHummusCheckBox = activityUnderTest.findViewById(R.id.checkBoxIsHummus);
        CheckBox isTahiniCheckBox = activityUnderTest.findViewById(R.id.checkBoxIsTahini);
        EditText orderCommentsEditText = activityUnderTest.findViewById(R.id.editTextOrderComment);
        Button placeOrderBtn = activityUnderTest.findViewById(R.id.buttonPlaceOrder);
        Button saveOrderChangesBtn = activityUnderTest.findViewById(R.id.buttonSaveOrderChanges);
        Button deleteOrderBtn = activityUnderTest.findViewById(R.id.buttonDeleteOrder);


        Assert.assertEquals(customerName, customerNameEditText.getText().toString());
        Assert.assertEquals(0.0, numPicklesSlider.getValue(), 0);
        Assert.assertFalse(isHummusCheckBox.isChecked());
        Assert.assertFalse(isTahiniCheckBox.isChecked());
        Assert.assertEquals("", orderCommentsEditText.getText().toString());
        Assert.assertEquals(View.VISIBLE, placeOrderBtn.getVisibility());
        Assert.assertEquals(View.GONE, saveOrderChangesBtn.getVisibility());
        Assert.assertEquals(View.GONE, deleteOrderBtn.getVisibility());
    }

    @Test
    public void when_openInEditMode_than_viewsContainsTheOrderData() {
        PlaceOrderActivity activityUnderTest = activityController.get();
        orderMockHolder = new SandwichOrder("myName", 3, true, false, "some comments");
        Mockito.when(dbMockHolder.getCurrentOrder()).thenReturn(orderMockHolder);
        orderLiveData.setValue(orderMockHolder);

        // set activity with newOrderMode intent
        Intent newOrderModeIntent = new Intent();
        newOrderModeIntent.putExtra("is_edit_mode", true);
        activityUnderTest.setIntent(newOrderModeIntent);

        activityUnderTest.onCreate(new Bundle());

        EditText customerNameEditText = activityUnderTest.findViewById(R.id.editTextCustomerName);
        Slider numPicklesSlider = activityUnderTest.findViewById(R.id.sliderNumOfPickles);
        CheckBox isHummusCheckBox = activityUnderTest.findViewById(R.id.checkBoxIsHummus);
        CheckBox isTahiniCheckBox = activityUnderTest.findViewById(R.id.checkBoxIsTahini);
        EditText orderCommentsEditText = activityUnderTest.findViewById(R.id.editTextOrderComment);
        Button placeOrderBtn = activityUnderTest.findViewById(R.id.buttonPlaceOrder);
        Button saveOrderChangesBtn = activityUnderTest.findViewById(R.id.buttonSaveOrderChanges);
        Button deleteOrderBtn = activityUnderTest.findViewById(R.id.buttonDeleteOrder);

        Assert.assertEquals(orderMockHolder.getCustomerName(), customerNameEditText.getText().toString());
        Assert.assertEquals(orderMockHolder.getNumPickles(), numPicklesSlider.getValue(), 0);
        Assert.assertEquals(orderMockHolder.isHummus(), isHummusCheckBox.isChecked());
        Assert.assertEquals(orderMockHolder.isTahini(), isTahiniCheckBox.isChecked());
        Assert.assertEquals(orderMockHolder.getComments(), orderCommentsEditText.getText().toString());
        Assert.assertEquals(View.GONE, placeOrderBtn.getVisibility());
        Assert.assertEquals(View.VISIBLE, saveOrderChangesBtn.getVisibility());
        Assert.assertEquals(View.VISIBLE, deleteOrderBtn.getVisibility());
    }

    @Test
    public void when_createOrder_than_OrderCreateWithViewsValues() {
        String customerName = "name";
        int numPickles = 5;
        boolean isHummus = false;
        boolean isTahini = true;
        String comments = "bla bla bla";

        PlaceOrderActivity activityUnderTest = activityController.get();
        Mockito.when(dbMockHolder.getCurrentOrder()).thenReturn(orderMockHolder);

        // set activity with newOrderMode intent
        Intent newOrderModeIntent = new Intent();
        newOrderModeIntent.putExtra("is_edit_mode", false);
        activityUnderTest.setIntent(newOrderModeIntent);

        activityUnderTest.onCreate(new Bundle());
        orderLiveData.setValue(orderMockHolder);

        EditText customerNameEditText = activityUnderTest.findViewById(R.id.editTextCustomerName);
        Slider numPicklesSlider = activityUnderTest.findViewById(R.id.sliderNumOfPickles);
        CheckBox isHummusCheckBox = activityUnderTest.findViewById(R.id.checkBoxIsHummus);
        CheckBox isTahiniCheckBox = activityUnderTest.findViewById(R.id.checkBoxIsTahini);
        EditText orderCommentsEditText = activityUnderTest.findViewById(R.id.editTextOrderComment);
        Button placeOrderBtn = activityUnderTest.findViewById(R.id.buttonPlaceOrder);

        customerNameEditText.setText(customerName);
        numPicklesSlider.setValue(numPickles);
        isHummusCheckBox.setChecked(isHummus);
        isTahiniCheckBox.setChecked(isTahini);
        orderCommentsEditText.setText(comments);

        // click place order
        placeOrderBtn.performClick();

        // verify
        ArgumentCaptor<SandwichOrder> passedOrderCaptor = ArgumentCaptor.forClass(SandwichOrder.class);
        Mockito.verify(dbMockHolder).addOrder(passedOrderCaptor.capture());
        SandwichOrder passedOrder = passedOrderCaptor.getValue();

        Assert.assertEquals(customerName, passedOrder.getCustomerName());
        Assert.assertEquals(numPickles, passedOrder.getNumPickles());
        Assert.assertEquals(isHummus, passedOrder.isHummus());
        Assert.assertEquals(isTahini, passedOrder.isTahini());
        Assert.assertEquals(comments, passedOrder.getComments());
        Assert.assertEquals(SandwichOrder.OrderStatus.WAITING, passedOrder.getOrderStatus());
    }
}
