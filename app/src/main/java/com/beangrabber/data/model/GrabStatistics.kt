package com.beangrabber.data.model

/**
 * Statistics for tracking grabbed beans bags
 */
data class GrabStatistics(
    val totalGrabbed: Int = 0,
    val successfulGrabs: Int = 0,
    val missedGrabs: Int = 0,
    val todayGrabs: Int = 0,
    val lastGrabTime: Long? = null
) {
    /**
     * Increment successful grabs
     */
    fun incrementSuccess(): GrabStatistics {
        return copy(
            totalGrabbed = totalGrabbed + 1,
            successfulGrabs = successfulGrabs + 1,
            todayGrabs = todayGrabs + 1,
            lastGrabTime = System.currentTimeMillis()
        )
    }

    /**
     * Increment missed grabs
     */
    fun incrementMissed(): GrabStatistics {
        return copy(missedGrabs = missedGrabs + 1)
    }

    /**
     * Calculate success rate
     */
    fun getSuccessRate(): Float {
        if (totalGrabbed == 0) return 0f
        return (successfulGrabs.toFloat() / totalGrabbed.toFloat()) * 100
    }
}
