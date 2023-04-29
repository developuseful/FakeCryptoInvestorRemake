package com.example.fakecryptoinvestorremake.presentation.util

fun getTimePassed(time: Long): String {
    val diff: Long = System.currentTimeMillis() - time

    val diffSeconds = diff / 1000 % 60
    val diffMinutes = diff / (60 * 1000) % 60
    val diffHours = diff / (60 * 60 * 1000) % 24
    val diffDays = diff / (24 * 60 * 60 * 1000)


    val sb = StringBuilder()
    if (diffDays == 0L) {
        if (diffHours == 0L) {
            if (diffMinutes == 0L) {
                sb.append("$diffSeconds sec ")
            } else {
                sb.append("$diffMinutes min ")
            }
        } else {
            sb.append("$diffHours h ")
        }
    } else {
        sb.append("$diffDays d ")
    }
    return sb.toString()

    /*
    when{
        diffMinutes < 1 ->  sb.append("$diffSeconds сек")
        diffHours < 24   ->  sb.append("$diffMinutes мин ")
        //diffDays     ->  sb.append("$diffHours ч ")
    }
     */

    /*
        if (diffMinutes == 0 || diffMinutes > 60){
            // Ни чего не делаем
        } else {
            sb.append(diffMinutes + " мин ");
        }
         */


}