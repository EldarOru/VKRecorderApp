package com.example.vkrecorderapp.utils

class TimeConverter {


    companion object {
        fun convertTime(millis: Long): String {
            val seconds = millis / 1000
            var minutes = ""
            var leftSeconds = ""

            minutes = if (seconds / 60 < 10){
                "0${seconds / 60}"
            }else{
                "${seconds / 60}"
            }

            leftSeconds = if (seconds % 60 < 10){
                "0${seconds % 60}"
            }else{
                "${seconds % 60}"
            }

            return "${minutes}:${leftSeconds}"
        }
    }
}