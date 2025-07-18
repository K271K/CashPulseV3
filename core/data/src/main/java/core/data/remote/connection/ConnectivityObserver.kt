package core.data.remote.connection

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
    fun isCurrentlyConnected(): Boolean
}