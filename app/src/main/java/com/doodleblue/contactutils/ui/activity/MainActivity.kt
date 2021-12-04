package com.doodleblue.contactutils.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat


import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doodleblue.contactutils.AppPreferences
import com.doodleblue.contactutils.R

import com.doodleblue.contactutils.db.data.ContactsInfo

import com.doodleblue.contactutils.ui.activity.adapter.MainAdapter
import com.doodleblue.contactutils.ui.service.ServiceReceiver
import com.doodleblue.contactutils.viewModel.MainViewModel


class MainActivity : AppCompatActivity(), MainAdapter.OnItemClickListener,
        ServiceReceiver.OnSmsReceivedListener {


    val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    var contactsInfoList: List<ContactsInfo>? = null
    lateinit var recyclerView: RecyclerView
    var mainAdapter: MainAdapter? = null
    var mainViewModel: MainViewModel? = null
    var progress: ProgressBar? = null
    var appPreferences: AppPreferences? = null

    var receiver: ServiceReceiver? = null

    //var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //  binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        receiver = ServiceReceiver()
        receiver?.setOnSmsReceivedListener(this)



        appPreferences = AppPreferences.getInstance(this, "SHARE_PREFERENCE")

        progress = findViewById(R.id.progress)
        progress?.visibility = View.VISIBLE


        val intent = intent
        val message = intent.getStringExtra("message")

        if (message != null) {


            if (message?.equals(appPreferences?.getString("Number"))) {
                showAlertdialog()
            }

        }

        // Inflate the layout for this fragment
        mainViewModel = MainViewModel(application)
        recyclerView = findViewById(R.id.recyclerView)

        requestContactPermission();
    }

    private fun showAlertdialog() {

        // custom dialog
        // custom dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custome_layout)
        dialog.setTitle(getString(R.string.app_name))

// set the custom dialog components - text, image and button

// set the custom dialog components - text, image and button
        dialog.findViewById<TextView>(R.id.text1).text = appPreferences?.getString("Name")

        dialog.findViewById<TextView>(R.id.text2).text = appPreferences?.getString("Number")
        dialog.show()

    }

    fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_CONTACTS
                    ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_PHONE_STATE
                    ) != PackageManager.PERMISSION_GRANTED
            ) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_CONTACTS
                        )
                ) {
                    val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(
                            this
                    )
                    builder.setTitle("Read contacts access needed")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener(DialogInterface.OnDismissListener {
                        requestPermissions(
                                arrayOf(
                                        Manifest.permission.READ_CONTACTS,
                                        Manifest.permission.READ_PHONE_STATE
                                ), PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    })
                    builder.show()
                } else {
                    requestPermissions(
                            this, arrayOf(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE
                    ),
                            PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {

                initAdapter()
            }
        } else {


            initAdapter()

        }
    }

    private fun initAdapter() {

        contactsInfoList = mainViewModel?.getContacts() as List<ContactsInfo>?
        mainAdapter = MainAdapter(contactsInfoList as ArrayList<ContactsInfo>, this)
        mainAdapter?.setOnItemClickListener(this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = mainAdapter
        progress?.visibility = View.GONE

    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    initAdapter()

                } else {
                    Toast.makeText(
                            this,
                            "You have disabled a contacts permission",
                            Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    override fun onItemClick(obj: ContactsInfo?, position: Int) {

        if (obj != null) {
            /* var contacttable = Contacttable(obj?.contactId, obj?.displayName, obj.phoneNumber)
             mainViewModel?.saveContact(contacttable)*/
            Toast.makeText(this, "save this  Contact ", Toast.LENGTH_SHORT).show()

            appPreferences?.saveString("Name", obj?.displayName)
            appPreferences?.saveString("Number", obj?.phoneNumber)
        }

    }

    override fun onSmsReceived(message: String?) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

}