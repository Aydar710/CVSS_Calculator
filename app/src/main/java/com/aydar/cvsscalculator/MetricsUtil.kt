package com.aydar.cvsscalculator

import java.math.BigDecimal
import java.math.RoundingMode

class MetricsUtil {

    companion object {
        lateinit var scoreChangedListener: (Double, Int) -> Unit
        private var metrics: MutableList<Metrics>? = null

        fun getMetricsList(): MutableList<Metrics> {
            if (metrics != null) {
                return metrics as MutableList<Metrics>
            } else {
                metrics = mutableListOf()

                val accessMethod = Metrics(ID_ACCESS_METHOD, -1.0)
                val accessDifficulty = Metrics(ID_ACCESS_DIFFICULTY, -1.0)
                val authentication = Metrics(ID_AUTHENTICATION, -1.0)
                val confidentiality = Metrics(ID_CONFIDENTIALITY, -1.0)
                val integrity = Metrics(ID_INTEGRITY, -1.0)
                val availability = Metrics(ID_AVAILABILITY, -1.0)

                metrics?.add(accessMethod)
                metrics?.add(accessDifficulty)
                metrics?.add(authentication)
                metrics?.add(confidentiality)
                metrics?.add(integrity)
                metrics?.add(availability)

                return metrics as MutableList<Metrics>
            }
        }

        fun getMetricsById(id: Int): Metrics {
            getMetricsList().forEach {
                if (it.id == id) {
                    return it
                }
            }

            return Metrics(-1, -1.0)
        }

        fun calculateVulnerabilityIfMetricsExists() {
            metrics?.forEach {
                if (it.value < 0)
                    return
            }
            calculateVulnerability()
        }

        private fun calculateVulnerability() {
            val impact = calculateImpact()
            val exploitability = calculateExploitability()

            val baseScore = round(((0.6 * impact) + (0.4 * exploitability) - 1.5) * f(impact), 1)

            var level =
                when (baseScore) {
                    in 0.1..3.9 -> {
                        LEVEL_LOW
                    }
                    in 4.0..6.9 -> {
                        LEVEL_AVERAGE
                    }
                    in 7.0..8.9 -> {
                        LEVEL_HIGH
                    }
                    in 9.0..10.0 -> {
                        LEVEL_CRITICAL
                    }
                    else -> {
                        LEVEL_INFO
                    }
                }
            scoreChangedListener.invoke(baseScore, level)
        }

        private fun calculateImpact(): Double {
            val confImpact = getMetricsById(ID_CONFIDENTIALITY).value
            val integImpact = getMetricsById(ID_INTEGRITY).value
            val availImpact = getMetricsById(ID_AVAILABILITY).value

            return 10.41 * (1 - (1 - confImpact) * (1 - integImpact) * (1 - availImpact))
        }

        private fun calculateExploitability(): Double {
            val accessVector = getMetricsById(ID_ACCESS_METHOD).value
            val accessComplexity = getMetricsById(ID_ACCESS_DIFFICULTY).value
            val authentication = getMetricsById(ID_AUTHENTICATION).value

            return 20 * accessVector * accessComplexity * authentication
        }

        private fun f(impact: Double): Double {
            return if (impact == 0.0) {
                0.0
            } else {
                1.176
            }
        }

        private fun round(value: Double, places: Int): Double {
            require(places >= 0)
            var bd: BigDecimal = BigDecimal.valueOf(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toDouble()
        }
    }
}