package com.kamikadze328.whoisthefirst.auxiliary_classes

data class Pointer(
    var x: Float,
    var y: Float,
    var id: Int,
    var placeInQueue: Int = 0,
    val color: Int = 0
)