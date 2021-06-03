package idosa.huji.postpc.sandwich_stand

import java.util.*

open class SandwichOrder(
    var customerName: String,
    var numPickles: Int,
    var isHummus: Boolean,
    var isTahini: Boolean,
    var comments: String
) {
    // empty constructor for FireStore
    constructor() : this("", 0, false, false, "")

    var id: String = UUID.randomUUID().toString()

    private var status: String = OrderStatus.WAITING.asString

    enum class OrderStatus(var asString: String) {

        WAITING("waiting"), IN_PROGRESS("in_progress"), READY("ready"), DONE("done");

        override fun toString(): String {
            return asString;
        }

        companion object {
            fun parse(asString: String): OrderStatus? {
                return when (asString.lowercase()) {
                    WAITING.asString -> WAITING
                    IN_PROGRESS.asString -> IN_PROGRESS
                    READY.asString -> READY
                    DONE.asString -> DONE
                    else -> null
                }
            }
        }
    }

    fun getOrderStatus(): OrderStatus? {
        return OrderStatus.parse(status)
    }

    fun changeStatus(newStatus: OrderStatus) {
        this.status = newStatus.asString
    }
}