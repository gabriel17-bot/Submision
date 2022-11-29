package com.example.storyappfinal.utils

import com.google.android.gms.maps.model.LatLng

object Constanta {

    enum class UserPreferences {
        UserUID, UserName, UserEmail, UserToken, UserLastLogin
    }

    enum class StoryDetail {
        UserName, ImageURL, ContentDescription, UploadTime, Latitude, Longitude
    }

    enum class LocationPicker {
        IsPicked, Latitude, Longitude
    }

    val indonesiaLocation = LatLng(-2.3932797, 108.8507139)
    const val preferenceName = "Settings"
    const val preferenceDefaultValue = "Not Set"
    const val preferenceDefaultDateValue = "2000/04/30 00:00:00"

    val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")


    /* PERMISSION REQUEST CODE */
    const val CAMERA_PERMISSION_CODE = 10
    const val STORAGE_PERMISSION_CODE = 20
    const val LOCATION_PERMISSION_CODE = 30
    const val TAG_STORY = "TEST_STORY"
    const val TAG_DOWNLOAD = "TEST_DOWNLOAD"
    const val TAG_AUTH = "TEST_AUTH"
}