package com.kamikadze328.whoisthefirst.data

interface DoubleTapObservable {
    val listeners: MutableSet<() -> Boolean>
    fun addDoubleTapListener(listener: () -> Boolean) = listeners.add(listener)
    fun removeDoubleTopListener(listener: () -> Boolean) = listeners.remove(listener)

    fun notifyAllDoubleTap(): Boolean = listeners.count { it.invoke() } > 0
}

