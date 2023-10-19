package com.example.scaminspector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scaminspector.Adapters.MessagesAdapter
import com.example.scaminspector.Models.ClassMessages


class MainActivity : AppCompatActivity() {

    private val permission: String = Manifest.permission.READ_SMS
    private val requestCode: Int = 1
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // val setSmsAppIntent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        //setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
        //startActivityForResult(setSmsAppIntent, R.layout.activity_main)

        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {

            recyclerView = findViewById(R.id.recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = MessagesAdapter(this ,readSms())

        }


    }


    private fun readSms(): ArrayList<ClassMessages> {
        //Log.d("Reading Started", "Reading Started")
        val numberCol = Telephony.TextBasedSmsColumns.ADDRESS
        val textCol = Telephony.TextBasedSmsColumns.BODY
        val typeCol = Telephony.TextBasedSmsColumns.TYPE // 1 - Inbox, 2 - Sent

        val projection = arrayOf(numberCol, textCol, typeCol)

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val numberColIdx = cursor!!.getColumnIndex(numberCol)
        val textColIdx = cursor.getColumnIndex(textCol)
        val typeColIdx = cursor.getColumnIndex(typeCol)
        var classMessages: ClassMessages?
        var listOfMessages: ArrayList<ClassMessages> = ArrayList()

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)
            val type = cursor.getString(typeColIdx)

            classMessages = ClassMessages(number, text, type)
            listOfMessages.add(classMessages)

            //Log.d("ScamInspector", "$number $text $type")
        }

        cursor.close()

        return listOfMessages
    }

    public fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}