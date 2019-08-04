package io.arunbuilds.instagramclient.utils

/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


object Helper {


    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    fun getTimeAgofromNow(time: Long): String {
    var instanceTime = time
    if (instanceTime < 1000000000000L) {
        // if timestamp given in seconds, convert to millis
        instanceTime *= 1000
    }

    val now = System.currentTimeMillis()
    if (instanceTime > now || instanceTime <= 0) {
        return "Oops :P"
    }

    val diff = now - instanceTime
    return when {
        diff < MINUTE_MILLIS -> "just now"
        diff < 2 * MINUTE_MILLIS -> "a minute ago"
        diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + " minutes ago"
        diff < 90 * MINUTE_MILLIS -> "an hour ago"
        diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + " hours ago"
        diff < 48 * HOUR_MILLIS -> "yesterday"
        else -> (diff / DAY_MILLIS).toString() + " days ago"
    }
}
}
