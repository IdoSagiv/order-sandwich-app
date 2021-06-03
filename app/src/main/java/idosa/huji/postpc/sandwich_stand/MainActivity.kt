package idosa.huji.postpc.sandwich_stand

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // in tests can inject value
    var db: LocalDb?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        if (db == null) {
            db = SandwichStandApp.getLocalDb()
        }

        val orderLiveData = db?.currentOrderLD
        orderLiveData?.observe(this, { sandwichOrder: SandwichOrder? ->
            when (sandwichOrder?.getStatus()) {
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
            orderLiveData.removeObservers(this)
            finish()
        })
    }
}