package io.github.gcky.remembermeds.util

/**
 * Created by Gordon on 03-Feb-18.
 */

class Utils() {

    fun timeToString(hour: Int, minute: Int): String {
        return "$hour:${if (minute >= 10) minute else "0$minute"}"
    }

}