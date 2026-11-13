/*
 * Copyright (C) 2026 The PenguinOS Project
 * Copyright (C) 2026 Lunaris AOSP
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lunaris.settings.fragments.miscellaneous

import android.content.Context
import android.provider.Settings

object TabBarStore {
    const val ENABLED = "penguin_glass_tab_bars"
    const val EXCLUDED = "penguin_glass_tab_bars_excluded"
    const val FLOAT = "penguin_glass_tab_bars_float"

    const val ENABLED_LUNARIS = "lunaris_glass_tab_bars"
    const val EXCLUDED_LUNARIS = "lunaris_glass_tab_bars_excluded"
    const val FLOAT_LUNARIS = "lunaris_glass_tab_bars_float"

    enum class Mode { OFF, GLASS, FLOAT }

    fun isEnabled(context: Context): Boolean =
        Settings.Secure.getInt(context.contentResolver, ENABLED, 0) == 1
                || Settings.Secure.getInt(context.contentResolver, ENABLED_LUNARIS, 0) == 1

    fun setEnabled(context: Context, enabled: Boolean) {
        val v = if (enabled) 1 else 0
        Settings.Secure.putInt(context.contentResolver, ENABLED, v)
        Settings.Secure.putInt(context.contentResolver, ENABLED_LUNARIS, v)
    }

    private fun list(context: Context, key: String): Set<String> {
        val raw = Settings.Secure.getString(context.contentResolver, key)
        return if (raw.isNullOrEmpty()) emptySet() else raw.split(",").filter { it.isNotEmpty() }.toSet()
    }

    private fun putList(context: Context, key: String, set: Set<String>) {
        Settings.Secure.putString(context.contentResolver, key, set.filter { it.isNotEmpty() }.joinToString(","))
    }

    private const val SEEN = "seen"
    private fun prefs(context: Context) = context.getSharedPreferences("tab_bars", Context.MODE_PRIVATE)

    fun seen(context: Context): Set<String> = prefs(context).getStringSet(SEEN, null) ?: emptySet()

    fun addSeen(context: Context, pkg: String) {
        val seen = seen(context)
        if (pkg !in seen) prefs(context).edit().putStringSet(SEEN, seen + pkg).apply()
    }

    fun mode(context: Context, pkg: String): Mode {
        val excluded = list(context, EXCLUDED) + list(context, EXCLUDED_LUNARIS)
        val floating = list(context, FLOAT) + list(context, FLOAT_LUNARIS)
        return when {
            pkg in excluded -> Mode.OFF
            pkg in floating -> Mode.FLOAT
            else -> Mode.GLASS
        }
    }

    fun setMode(context: Context, pkg: String, mode: Mode) {
        val excluded = (list(context, EXCLUDED) + list(context, EXCLUDED_LUNARIS)).toMutableSet()
        val floating = (list(context, FLOAT) + list(context, FLOAT_LUNARIS)).toMutableSet()
        when (mode) {
            Mode.OFF -> {
                excluded.add(pkg)
                floating.remove(pkg)
            }
            Mode.GLASS -> {
                excluded.remove(pkg)
                floating.remove(pkg)
            }
            Mode.FLOAT -> {
                excluded.remove(pkg)
                floating.add(pkg)
            }
        }
        putList(context, EXCLUDED, excluded)
        putList(context, FLOAT, floating)
        putList(context, EXCLUDED_LUNARIS, excluded)
        putList(context, FLOAT_LUNARIS, floating)
    }
}
