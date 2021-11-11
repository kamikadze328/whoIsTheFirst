package com.kamikadze328.whoisthefirst.repository

import com.kamikadze328.whoisthefirst.auxiliary_classes.Pointer
import com.kamikadze328.whoisthefirst.data.MultiTouchState

class OnlineRepository {
    private val pointers: MutableList<Pointer> = mutableListOf()
    private var id = 0

    fun notifyAllOther(pointers: MutableList<Pointer>, state: MultiTouchState) {

    }

    fun onOtherChanged() {

    }

    fun changeState(state: MultiTouchState) {

    }

    private val maxPersons = 10
    private val maxFingersAtPerson = 10
    private val freeId = MutableList(maxPersons) { it * maxFingersAtPerson }
    private val errorId = -1

    fun getNewId(): Int = if (freeId.isNotEmpty()) freeId.removeFirst() else errorId
}