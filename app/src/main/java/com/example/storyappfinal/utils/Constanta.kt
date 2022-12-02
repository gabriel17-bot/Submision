package com.example.storyappfinal.utils

object Constanta {

    enum class UserPreferences {
        UserUID, UserName, UserEmail, UserToken, UserLastLogin
    }

    enum class StoryDetail {
        UserName, ImageURL, ContentDescription, Latitude, Longitude
    }

    const val preferenceName = "Settings"
    const val preferenceDefaultValue = "Not Set"
    const val preferenceDefaultDateValue = "2022/12/02 00:00:00"

    val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

    /* PERMISSION REQUEST CODE */
    const val CAMERA_PERMISSION_CODE = 10
    const val LOCATION_PERMISSION_CODE = 30
    const val TAG_STORY = "TEST_STORY"
    const val TAG_AUTH = "TEST_AUTH"
}