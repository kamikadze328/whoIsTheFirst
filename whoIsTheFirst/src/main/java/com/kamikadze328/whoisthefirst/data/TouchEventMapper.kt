package com.kamikadze328.whoisthefirst.data

import android.view.MotionEvent
import com.kamikadze328.whoisthefirst.auxiliary_classes.Pointer
import javax.inject.Singleton

@Singleton
class TouchEventMapper {
    private var width = 0
    private var height = 0

    private fun setViewMetrics(width: Int, height: Int) {
        this.width = width
        this.height = height
    }


    //Convert from MotionEvent to List<Pointers>
    fun map(
        e: MotionEvent,
        width: Int = this.width,
        height: Int = this.height
    ): MutableList<Pointer> {
        setViewMetrics(width, height)
        val pointers = mutableListOf<Pointer>()
        //Log.d("kek", "${e.pointerCount} ${e.action == MotionEvent.ACTION_DOWN}(${e.actionMasked == MotionEvent.ACTION_POINTER_DOWN})/${e.action == MotionEvent.ACTION_UP}(${e.actionMasked == MotionEvent.ACTION_POINTER_UP})/${e.action == MotionEvent.ACTION_CANCEL}  (${e.action}/${e.actionMasked})")
        //could be problem with system gesture like three finger up or down.
        //So if user touch screen three fingers at one time action will be MotionEvent.CANCEL
        if (e.pointerCount == 1 && e.action == MotionEvent.ACTION_UP) return pointers

        //Log.d("kek", "${e.pointerCount} ${e.actionMasked == MotionEvent.ACTION_POINTER_UP}/${e.action == MotionEvent.ACTION_UP} ${e.actionMasked} ${e.actionIndex}")
        for (i in 0 until e.pointerCount) {
            val id = e.getPointerId(i)
            //Log.d("kek", "i - $i; id - $id; index - ${e.findPointerIndex(id)} index - ${e.actionIndex}")
            if (e.actionMasked == MotionEvent.ACTION_POINTER_UP && i == e.actionIndex) continue
            val x = e.getX(i) / width
            val y = e.getY(i) / height
            pointers.add(Pointer(x, y, id))
        }
        //Log.d("kek", "$pointers")
        return pointers
    }

    //Convert from List with percents x and y to pixel x and y for that screen.
    fun map(
        pointers: List<Pointer>,
        width: Int = this.width,
        height: Int = this.height
    ): List<Pointer> {
        setViewMetrics(width, height)

        //TODO
        return pointers.map {
            it.copy().apply {
                x *= width
                y *= height
            }
        }
    }
}