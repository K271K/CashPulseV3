package core.domain.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

fun formatDateToISO8061(date: String, time: String): String{
    val formattedDate = LocalDateTime.parse(
        "${date}T${time.trim()}:00", // Удаляем пробелы с помощью trim()
        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:mm:ss")
    ).atZone(java.time.ZoneId.systemDefault()) // Используем локальную зону пользователя
        .withZoneSameInstant(java.time.ZoneId.of("UTC")) // Переводим в UTC
        .format(java.time.format.DateTimeFormatter.ISO_INSTANT) // Форматируем с Z
    return formattedDate
}

fun formatISO8601ToDate(iso8601: String): String {
    val instant = Instant.parse(iso8601)
    val localDateTime = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
    return localDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun formatISO8601ToTime(iso8601: String): String {
    val instant = Instant.parse(iso8601)
    val localDateTime = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
    return localDateTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
}

fun formatCurrencyFromTextToSymbol(currency: String): String {
    return when (currency) {
        "USD" -> "$"
        "EUR" -> "€"
        "RUB" -> "₽"
        else -> currency
    }
}

fun formatDateFromLongToHuman(date: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // Убираем UTC timezone, чтобы использовать локальную зону пользователя
    val result = sdf.format(Date(date))
    return result
}