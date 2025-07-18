package com.example.cashpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cashpulse.navigation.DefaultNavigator
import com.example.cashpulse.navigation.MainNavigation
import com.example.cashpulse.ui.theme.CashPulseTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var defaultNavigator: DefaultNavigator

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as BaseApplication).appComponent.inject(this)
        enableEdgeToEdge()
        setContent {
            CashPulseTheme {
                MainNavigation(
                    defaultNavigator = defaultNavigator,
                    mainActivityViewModelFactory = mainActivityViewModelFactory
                )
            }
        }
    }
}