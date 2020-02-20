package com.aydar.cvsscalculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MetricsUtil.getMetricsList()

        setAccessMethodListener()
        setAccessDifficultyListener()
        setAuthenticationListener()
        setConfidentialityListener()
        setIntegrityListener()
        setAvailabilityListener()

        MetricsUtil.scoreChangedListener = {score, level ->
            tv_score.text = score.toString()
            when(level){
                LEVEL_CRITICAL -> {
                    tv_score.setTextColor(resources.getColor(R.color.critical))
                }
                LEVEL_HIGH -> {
                    tv_score.setTextColor(resources.getColor(R.color.high))
                }
                LEVEL_AVERAGE -> {
                    tv_score.setTextColor(resources.getColor(R.color.average))
                }
                LEVEL_LOW -> {
                    tv_score.setTextColor(resources.getColor(R.color.low))
                }
                LEVEL_INFO -> {
                    tv_score.setTextColor(resources.getColor(R.color.info))
                }
            }
        }
    }

    private fun setAvailabilityListener() {
        switch_availability.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                val metrics = MetricsUtil.getMetricsById(ID_AVAILABILITY)
                changeMetricsByValue(metrics, position, 0.0, 0.275, 0.66)
            }
        }
    }

    private fun setIntegrityListener() {
        switch_integrity.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                val metrics = MetricsUtil.getMetricsById(ID_INTEGRITY)
                changeMetricsByValue(metrics, position, 0.0, 0.275, 0.66)
            }
        }
    }

    private fun setConfidentialityListener() {
        switch_confidentiality.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                val metrics = MetricsUtil.getMetricsById(ID_CONFIDENTIALITY)
                changeMetricsByValue(metrics, position, 0.0, 0.275, 0.66)
            }
        }
    }

    private fun setAuthenticationListener() {
        switch_authentication.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                val metrics = MetricsUtil.getMetricsById(ID_AUTHENTICATION)
                changeMetricsByValue(metrics, position, 0.45, 0.56, 0.704)
            }
        }
    }

    private fun setAccessDifficultyListener() {
        switch_access_difficulty.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                val metrics = MetricsUtil.getMetricsById(ID_ACCESS_DIFFICULTY)
                changeMetricsByValue(metrics, position, 0.35, 0.61, 0.71)
            }

        }
    }

    private fun setAccessMethodListener() {
        switch_access_method.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                val metrics = MetricsUtil.getMetricsById(ID_ACCESS_METHOD)
                changeMetricsByValue(metrics, position, 0.395, 0.646, 1.0)
            }
        }
    }

    private fun changeMetricsByValue(
        metrics: Metrics,
        position: Int,
        val1: Double,
        val2: Double,
        val3: Double
    ) {
        when (position) {
            0 -> {
                metrics.value = val1
            }
            1 -> {
                metrics.value = val2
            }
            2 -> {
                metrics.value = val3
            }
        }

        MetricsUtil.calculateVulnerabilityIfMetricsExists()
    }
}
