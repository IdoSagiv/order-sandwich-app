package idosa.huji.postpc.sandwich_stand;

import android.app.Application;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, application = Application.class)
public class MainActivityTest {
    private LocalDb dbMockHolder;
    private SandwichOrder orderMockHolder;
    private MutableLiveData<SandwichOrder> orderLiveData;
    private ActivityController<MainActivity> activityController;

    @Before
    public void setup() {
        dbMockHolder = Mockito.mock(LocalDb.class);
        orderMockHolder = Mockito.mock(SandwichOrder.class);
        orderLiveData = new MediatorLiveData<>();
        // when asking the `dbMockHolder` to get the current order, return our 'orderMockHolder'
        Mockito.when(dbMockHolder.getCurrentOrder())
                .thenReturn(orderMockHolder);
        // when asking the `dbMockHolder` to get the current order, return our 'ldMockHolder'
        Mockito.when(dbMockHolder.getCurrentOrderLD())
                .thenReturn(orderLiveData);

        activityController = Robolectric.buildActivity(MainActivity.class);

        // let the activity use our `dbMockHolder` as the db
        MainActivity activityUnderTest = activityController.get();
        activityUnderTest.setDb(dbMockHolder);
    }

    @Test
    public void when_openAppWithNoOrder_than_openThePlaceOrderActivityOnNewOrderMode() {
        // todo: implement
    }

    @Test
    public void when_openAppWithDoneOrder_than_openThePlaceOrderActivityOnNewOrderMode() {
        // todo: implement
    }

    @Test
    public void when_openAppWithWaitingOrder_than_openThePlaceOrderActivityOnEditMode() {
        // todo: implement
    }

    @Test
    public void when_openAppWithInProgressOrder_than_openTheWaitForOrderActivity() {
        // todo: implement
    }

    @Test
    public void when_openAppWithReadyOrder_than_openTheOrderReadyActivity() {
        // todo: implement
    }

}
