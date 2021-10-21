package com.auditionstreet.castingagency.utils.chat.callBacks

import android.os.Bundle
import com.quickblox.core.QBEntityCallback


open class QbEntityCallbackWrapper<T>(callback: QBEntityCallback<T>) : QbEntityCallbackTwoTypeWrapper<T, T>(callback) {
    override fun onSuccess(t: T, bundle: Bundle?) {
        onSuccessInMainThread(t, bundle)
    }
}