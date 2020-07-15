package com.bee.android.common.persistence

import android.content.SharedPreferences

class Persistence(private val spInterface: SharedPreferences) : SpInterface {

    override fun getAll(): MutableMap<String, *> {
        return spInterface.all
    }

    override fun getString(key: String?, defValue: String?): String? {
        return spInterface.getString(key, defValue)
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        return spInterface.getStringSet(key, defValues) as MutableSet<String>
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return spInterface.getInt(key, defValue)
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return spInterface.getLong(key, defValue)
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return spInterface.getFloat(key, defValue)
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return spInterface.getBoolean(key, defValue)
    }

    override fun contains(key: String?): Boolean {
        return spInterface.contains(key)
    }

    override fun edit(): SharedPreferences.Editor {
        return spInterface.edit()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        if (listener != null) {
            spInterface.registerOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        if (listener != null) {
            spInterface.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override fun putString(key: String?, value: String?): SharedPreferences.Editor {
        return spInterface.edit().putString(key, value)
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor {
        return spInterface.edit().putStringSet(key, values)
    }

    override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
        return spInterface.edit().putInt(key, value)
    }

    override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
        return spInterface.edit().putLong(key, value)
    }

    override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
        return spInterface.edit().putFloat(key, value)
    }

    override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
        return spInterface.edit().putBoolean(key, value)
    }

    override fun remove(key: String?): SharedPreferences.Editor {
        return spInterface.edit().remove(key)
    }

    override fun clear(): SharedPreferences.Editor {
        return spInterface.edit().clear()
    }

    override fun commit(): Boolean {
        return spInterface.edit().commit()
    }

    override fun apply() {
    }
}