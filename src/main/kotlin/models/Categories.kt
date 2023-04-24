package models

open class Categories(
    var _id: org.bson.types.ObjectId = org.bson.types.ObjectId.get(),
    var title: String = "",
    var description: String = "",
    var owner_id: String = ""
)