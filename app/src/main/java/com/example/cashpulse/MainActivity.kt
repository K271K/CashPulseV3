package com.example.cashpulse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.cashpulse.navigation.DefaultNavigator
import com.example.cashpulse.navigation.MainNavigation
import com.example.cashpulse.ui.theme.CashPulseTheme
import core.data.sync.SyncManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var defaultNavigator: DefaultNavigator

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory

    @Inject
    lateinit var syncManager: SyncManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as BaseApplication).appComponent.inject(this)

        lifecycleScope.launch {
            val result = syncManager.performInitialSync()
            if (result.isSuccess) {
                Log.d("syncManager","✅ Initial sync completed successfully")
            } else {
                Log.d("syncManager","❌ Initial sync failed: ${result.exceptionOrNull()?.message}")
            }
        }

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