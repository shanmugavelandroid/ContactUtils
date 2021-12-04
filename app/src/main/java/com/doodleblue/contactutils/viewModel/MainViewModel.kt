package com.doodleblue.contactutils.viewModel

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel

import com.doodleblue.contactutils.db.data.ContactsInfo

import com.doodleblue.contactutils.model.repository.MainRepository

class MainViewModel(application: Application): AndroidViewModel(application) {

    val repository: MainRepository
     private var contacts: ObservableArrayList<ContactsInfo>? = null

    init {
          repository= MainRepository(application)
        contacts = ObservableArrayList()

    }

    fun getContacts(): List<ContactsInfo?>? {
        contacts!!.addAll(repository.getContacts()!!)
        return contacts
    }


}