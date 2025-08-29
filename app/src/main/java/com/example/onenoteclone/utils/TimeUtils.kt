package com.example.onenoteclone.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * Utility functions for formatting relative time
 */
object TimeUtils {
    
    fun formatRelativeTime(instant: Instant): String {
        val now = Clock.System.now()
        val duration = now - instant
        
        return when {
            duration < 1.minutes -> "Just now"
            duration < 1.hours -> "${duration.inWholeMinutes}m ago"
            duration < 1.days -> "${duration.inWholeHours}h ago"
            duration < 7.days -> {
                val days = duration.inWholeDays
                if (days == 1L) "Yesterday" else "${days}d ago"
            }
            else -> {
                val days = duration.inWholeDays
                "${days}d ago"
            }
        }
    }
    
    fun formatDate(instant: Instant): String {
        // Simple date formatting - in a real app you'd use proper date formatting
        val now = Clock.System.now()
        val duration = now - instant
        
        return when {
            duration < 1.days -> "Today"
            duration < 2.days -> "Yesterday"
            else -> {
                // Return MM/DD format for demo
                val dayOfMonth = (instant.epochSeconds / 86400L % 30 + 1).toInt()
                val month = ((instant.epochSeconds / 86400L / 30) % 12 + 1).toInt()
                String.format("%02d/%02d", month, dayOfMonth)
            }
        }
    }
}