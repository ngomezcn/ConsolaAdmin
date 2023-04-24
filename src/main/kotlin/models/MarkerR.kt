package models

open class MarkerR(
    var _id: org.bson.types.ObjectId = org.bson.types.ObjectId.get(),
    var latitude: Double = -0.1,
    var longitude: Double = -0.1,
    var title: String = "",
    var description: String = "",
    var date: String = "",
    var image: ByteArray? = null,
    var category: String = "",
    var owner_id: String = ""
)