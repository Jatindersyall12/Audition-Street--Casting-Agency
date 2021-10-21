package com.auditionstreet.castingagency.utils.chat

import android.util.Log
import android.view.View
import com.auditionstreet.castingagency.AppCastingAgency
import com.auditionstreet.castingagency.R
import com.google.android.material.snackbar.Snackbar
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection


open class VerboseQbChatConnectionListener(private val rootView: View) : ConnectionListener {
    private val TAG = VerboseQbChatConnectionListener::class.java.simpleName
    private var snackbar: Snackbar? = null

    override fun connected(connection: XMPPConnection) {
        Log.i(TAG, "connected()")
        snackbar?.dismiss()
    }

    override fun authenticated(connection: XMPPConnection, authenticated: Boolean) {
        Log.i(TAG, "authenticated()")
    }

    override fun connectionClosed() {
        Log.i(TAG, "connectionClosed()")
    }

    override fun connectionClosedOnError(e: Exception) {
        Log.i(TAG, "connectionClosedOnError(): " + e.localizedMessage)
        snackbar = Snackbar.make(rootView, R.string.connection_error, Snackbar.LENGTH_INDEFINITE)
        snackbar!!.show()
    }

    override fun reconnectingIn(seconds: Int) {
        if (seconds % 5 == 0 && seconds != 0) {
            Log.i(TAG, "reconnectingIn(): $seconds")
            val message = AppCastingAgency.getInstance().getString(R.string.reconnect_alert, seconds.toString())
            snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
            snackbar!!.show()
        }
    }

    override fun reconnectionSuccessful() {
        Log.i(TAG, "reconnectionSuccessful()")
        snackbar?.dismiss()
    }

    override fun reconnectionFailed(error: Exception) {
        Log.i(TAG, "reconnectionFailed(): " + error.localizedMessage)
    }
}