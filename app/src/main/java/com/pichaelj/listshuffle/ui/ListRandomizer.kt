package com.pichaelj.listshuffle.ui

import android.os.Build
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class ListRandomizer<T>(val originalList: List<T>) {

    fun newRandomizedOrder() : List<T> {
        val mutableList: MutableList<T> = originalList.toMutableList()
        fisherYatesShuffle(mutableList)
        return mutableList
    }

    private fun fisherYatesShuffle(list: MutableList<T>) {
        if (list.size > 1) {
            for (i in (list.size - 1) downTo(1)) {

                // Generate random number between 0 and current index
                //
                val swapI = if (Build.VERSION.SDK_INT >= 21) {
                    ThreadLocalRandom.current().nextInt(0, i + 1)
                } else {
                    Random.nextInt(0, i + 1)
                }

                val tmp = list[i]
                list[i] = list[swapI]
                list[swapI] = tmp
            }
        }
    }
}