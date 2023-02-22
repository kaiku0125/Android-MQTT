package org.amobile.mqtt_k.prefs

import android.content.Context
import android.util.Log

private const val KEY_USERNAME: String = "userName"
private const val KEY_CLIENT_ID: String = "client_id"
private const val KEY_COMPANY_NAME: String = "company"

class Prefs {

    companion object {
        private const val TAG = "Prefs"

        //        const val isDarkMode: Boolean = false
        var userName: String = "777"
        var clientID: String = "888"
        var companyName: String = "999"

        fun load(ctx: Context) {
            userName = Shared.get(ctx, KEY_USERNAME, userName)
            clientID = Shared.get(ctx, KEY_CLIENT_ID, clientID)
            companyName = Shared.get(ctx, KEY_COMPANY_NAME, companyName)
            Log.e(
                TAG, "load -> UserName : $userName, \n" +
                        "ClientID : $clientID, \n" +
                        "Company : $companyName"
            )


        }

        fun save(ctx: Context) {
            Shared.put(ctx, KEY_USERNAME, userName)
            Shared.put(ctx, KEY_CLIENT_ID, clientID)
            Shared.put(ctx, KEY_COMPANY_NAME, companyName)

        }

    }


}