package com.app4funbr.themoviedb.view.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.app4funbr.themoviedb.R

class SettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}