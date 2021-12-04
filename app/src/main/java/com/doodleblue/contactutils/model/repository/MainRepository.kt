package com.doodleblue.contactutils.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import com.doodleblue.contactutils.db.data.ContactsInfo


class MainRepository(var context: Context){


    /* get the contact Details*/

    @SuppressLint("Range")
    public fun getContacts(): List<ContactsInfo>?
    {
        val contentResolver = context.contentResolver
        var contactId: String? = null
        var displayName: String? = null
        var contactsInfoList: List<ContactsInfo>? = ArrayList()
        val cursor: Cursor? = context.getContentResolver().query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    val hasPhoneNumber: Int =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt()
                    if (hasPhoneNumber > 0) {
                        val contactsInfo = ContactsInfo()
                        contactId =
                            cursor?.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        displayName =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        contactsInfo.contactId = contactId
                        contactsInfo.displayName = displayName
                        val phoneCursor: Cursor? = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String?>(contactId),
                            null
                        )
                        if (phoneCursor?.moveToNext() == true) {
                            val phoneNumber: String =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contactsInfo.phoneNumber = phoneNumber
                        }
                        phoneCursor?.close()
                        (contactsInfoList as ArrayList<ContactsInfo>)?.add(contactsInfo)
                    }
                }
            }
        }
        cursor?.close()

        Log.d("Contactlistsize", contactsInfoList?.size.toString())

        return  contactsInfoList
    }


}