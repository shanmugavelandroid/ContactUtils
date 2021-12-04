package com.doodleblue.contactutils.ui.service


import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

import com.doodleblue.contactutils.ui.activity.MainActivity


class ServiceReceiver : BroadcastReceiver() {

     var listener: OnSmsReceivedListener? = null

    fun setOnSmsReceivedListener(context: OnSmsReceivedListener?) {
        listener = context
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val tm = context?.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager

        when (tm.callState) {
            TelephonyManager.CALL_STATE_RINGING -> {
                val phoneNr = intent!!.getStringExtra("incoming_number")


                val i = Intent(context, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.putExtra("message", phoneNr)
                context.startActivity(i)

                /*  if (listener != null) {
                    listener?.onSmsReceived(phoneNr)
                }*/
            }
        }



    }

    interface OnSmsReceivedListener {
        fun onSmsReceived(message: String?)
    }
}