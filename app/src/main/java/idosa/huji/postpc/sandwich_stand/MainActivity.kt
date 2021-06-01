package idosa.huji.postpc.sandwich_stand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (SandwichStandApp.getLocalDb().currentOrder?.status) {
            null -> {
                startActivity(Intent(this, NewOrderActivity::class.java))
            }
            SandwichOrder.OrderStatus.WAITING -> {
                startActivity(Intent(this, EditOrderActivity::class.java))
            }
            SandwichOrder.OrderStatus.IN_PROGRESS -> {
                startActivity(Intent(this, WaitForOrderActivity::class.java))
            }
            SandwichOrder.OrderStatus.READY -> {
                startActivity(Intent(this, OrderReadyActivity::class.java))
            }
            else -> {
                // todo: verify tis is what should be done
                startActivity(Intent(this, NewOrderActivity::class.java))
            }
        }
    }
}