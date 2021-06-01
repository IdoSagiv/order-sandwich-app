package idosa.huji.postpc.sandwich_stand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (SandwichStandApp.getLocalDb().currentOrder?.status) {
            null -> {
                val intent = Intent(this, PlaceOrderActivity::class.java)
                intent.putExtra("is_edit_mode", false)
                startActivity(intent)
            }
            SandwichOrder.OrderStatus.WAITING -> {
                val intent = Intent(this, PlaceOrderActivity::class.java)
                intent.putExtra("is_edit_mode", true)
                startActivity(intent)
            }
            SandwichOrder.OrderStatus.IN_PROGRESS -> {
                startActivity(Intent(this, WaitForOrderActivity::class.java))
            }
            SandwichOrder.OrderStatus.READY -> {
                startActivity(Intent(this, OrderReadyActivity::class.java))
            }
            else -> {
                // todo: verify tis is what should be done
                val intent = Intent(this, PlaceOrderActivity::class.java)
                intent.putExtra("is_edit_mode", false)
                startActivity(intent)
            }
        }
    }
}