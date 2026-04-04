package com.example.krushiscan.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.krushiscan.data.api.User

class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "krushi_scan_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_FARM_NAME = "farm_name"
        private const val KEY_FARM_SIZE = "farm_size"
        private const val KEY_STATE = "state"
        private const val KEY_DISTRICT = "district"
        private const val KEY_VILLAGE = "village"
        private const val KEY_SOIL_HEALTH = "soil_health"
        private const val KEY_TOTAL_FIELDS = "total_fields"
        private const val KEY_TOTAL_SENSORS = "total_sensors"
        private const val KEY_MEMBER_SINCE = "member_since"
    }
    
    fun saveUserSession(user: User) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_PHONE, user.phone)
            putString(KEY_FARM_NAME, user.farmName)
            putString(KEY_FARM_SIZE, user.farmSize)
            putString(KEY_STATE, user.state)
            putString(KEY_DISTRICT, user.district)
            putString(KEY_VILLAGE, user.village)
            putString(KEY_SOIL_HEALTH, user.soilHealth)
            putInt(KEY_TOTAL_FIELDS, user.totalFields ?: 1)
            putInt(KEY_TOTAL_SENSORS, user.totalSensors ?: 0)
            putString(KEY_MEMBER_SINCE, user.memberSince)
            apply()
        }
    }
    
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }
    
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }
    
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    fun getUser(): User? {
        if (!isLoggedIn()) return null
        
        val userId = getUserId() ?: return null
        val userName = getUserName() ?: return null
        val userEmail = getUserEmail() ?: return null
        
        return User(
            id = userId,
            name = userName,
            email = userEmail,
            phone = prefs.getString(KEY_USER_PHONE, null),
            farmName = prefs.getString(KEY_FARM_NAME, null),
            farmSize = prefs.getString(KEY_FARM_SIZE, null),
            state = prefs.getString(KEY_STATE, null),
            district = prefs.getString(KEY_DISTRICT, null),
            village = prefs.getString(KEY_VILLAGE, null),
            cropsGrown = null,
            bio = null,
            soilHealth = prefs.getString(KEY_SOIL_HEALTH, "Good"),
            totalFields = prefs.getInt(KEY_TOTAL_FIELDS, 1),
            totalSensors = prefs.getInt(KEY_TOTAL_SENSORS, 0),
            memberSince = prefs.getString(KEY_MEMBER_SINCE, null),
            profileSettings = null
        )
    }
    
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
