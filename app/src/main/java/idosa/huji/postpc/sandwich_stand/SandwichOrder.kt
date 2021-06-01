package idosa.huji.postpc.sandwich_stand

import java.util.*

data class SandwichOrder(
    var customerName: String,
    var numPickles: Int,
    var isHummus: Boolean,
    var isTahini: Boolean,
    var comments: String
) {
    enum class OrderStatus {
        WAITING, IN_PROGRESS, READY, DONE
    }

    var id: String = UUID.randomUUID().toString()
    var status: OrderStatus = OrderStatus.WAITING
}