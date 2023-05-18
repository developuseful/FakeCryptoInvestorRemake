package com.example.fakecryptoinvestorremake.presentation.util

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import com.example.fakecryptoinvestorremake.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


fun getTimePassedOld(time: Long): String {
    val diff: Long = System.currentTimeMillis() - time

    val diffSeconds = diff / 1000 % 60
    val diffMinutes = diff / (60 * 1000) % 60
    val diffHours = diff / (60 * 60 * 1000) % 24
    val diffDays = diff / (24 * 60 * 60 * 1000)
    val diffYears: Long = diff / (24 * 60 * 60 * 1000)


    val sb = StringBuilder()
    if (diffDays == 0L) {
        if (diffHours == 0L) {
            if (diffMinutes == 0L) {
                //sb.append("$diffSeconds sec ")
                sb.append("now")
            } else {
                sb.append("$diffMinutes min ")
            }
        } else {
            sb.append("$diffHours h ")
        }
    } else {
        sb.append("${diffDays}d ")
        sb.append("${diffHours}h")
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

fun getTimePassedTest(time: Long): String {
    val timePassedMilliseconds = System.currentTimeMillis() - time

    val seconds = timePassedMilliseconds / 1000 % 60
    val minutes = timePassedMilliseconds / (60 * 1000) % 60
    val hours = timePassedMilliseconds / (60 * 60 * 1000) % 24
    val days = timePassedMilliseconds / (24 * 60 * 60 * 1000)
    val weeks = timePassedMilliseconds / (7 * 24 * 60 * 60 * 1000)
    val month = timePassedMilliseconds / (31L * 24 * 60 * 60 * 1000)
    val years = timePassedMilliseconds / (365L * 24 * 60 * 60 * 1000)
    val thousands = timePassedMilliseconds / (100 * 365L * 24 * 60 * 60 * 1000)

    val sb = StringBuilder()

    when {
        minutes < 1L -> sb.append("$seconds sec")
        hours < 1L -> sb.append("$minutes min $seconds sec")
        days < 1L -> sb.append("$hours h $minutes min")
        weeks < 1L -> sb.append("$days d $hours h")
        month < 1L -> sb.append("$weeks w $days d")
        years < 1L -> sb.append("$month m $weeks w")
        thousands < 1L -> sb.append("$years y $month m")
    }
    return sb.toString()
}


@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getTimePassed(time: Long): String {

//    val firstInput = "01 01 2021"
//    val secondInput = "31 05 2023"
//
//    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MM yyyy")
//    val startDate = LocalDate.parse(firstInput, formatter)
//    val endDate = LocalDate.parse(secondInput, formatter)


    val startDate: LocalDate =
        LocalDate.from(Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate())
    val endDate: LocalDate = LocalDate.now()

    val timePassedMilliseconds = System.currentTimeMillis() - time

    val minutes = timePassedMilliseconds / (60 * 1000) % 60
    val hours = timePassedMilliseconds / (60 * 60 * 1000) % 24
    val days = ChronoUnit.DAYS.between(startDate, endDate)
    val month = ChronoUnit.MONTHS.between(startDate, endDate) % 12
    val years = ChronoUnit.YEARS.between(startDate, endDate)
    val thousands = ChronoUnit.CENTURIES.between(startDate, endDate)

    val sb = StringBuilder()

    when {
        minutes     < 1L && hours < 1L && days < 1L && month < 1L && years < 1L -> sb.append("now")
        hours       < 1L && days < 1L && month < 1L && years < 1L -> {
            sb.append(
                pluralStringResource(R.plurals.minutes, minutes.toInt(), minutes.toInt())
            )
        }

        days        < 1L && month < 1L && years < 1L -> {
            sb.append(
                pluralStringResource(R.plurals.hours, hours.toInt(), hours.toInt())
            )
        }
        month       < 1L && years < 1L -> {
            sb.append(
                pluralStringResource(R.plurals.days, days.toInt(), days.toInt())
            )
        }
        years       < 1L -> sb.append("$month month")
        thousands < 1L -> {
            if (month < 1L){
                sb.append(
                    pluralStringResource(R.plurals.years, years.toInt(), years.toInt())
                )
            } else{
                sb.append(
                    pluralStringResource(R.plurals.years, years.toInt(), years.toInt())
                )
                sb.append(" $month month")
            }

        }
    }

    return sb.toString()
}

fun getDateFormatted(date: Long): String? {
    val df =
        SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault())
    return df.format(date)
}

