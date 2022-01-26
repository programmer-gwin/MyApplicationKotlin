package com.elkanah.myapplicationkotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ikonik.pos.activity.PurchaseActivity
import com.ikonik.pos.myutils.StringUtils
import com.ikonik.pos.myutils.utils.Constants
import com.ikonik.pos.myutils.utils.TransactionResponse
import com.ikonik.pos.myutils.utils.TransactionType
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_click_me = findViewById(R.id.btnTestIkonic) as Button
        btn_click_me.setOnClickListener {
            TestTransaction()
        }
    }

    private fun TestTransaction() {
        try {
            startAccountSelectionActivity(1.0);
        } catch (ex:Exception){
            Toast.makeText(this, "Error " + ex.message, Toast.LENGTH_LONG).show()
        }
    }

    val PURCHASE_REQUEST_CODE = 100
    private fun startAccountSelectionActivity(amount: Double) {
        val intent = Intent(this, PurchaseActivity::class.java)
        intent.putExtra(Constants.TRANSACTION_TYPE, TransactionType.PURCHASE)
        intent.putExtra(Constants.INTENT_EXTRA_AMOUNT_KEY, amount)
        var elkanahRef = StringUtils.getTransactionRef()
        Log.d("TestActivity", elkanahRef)
        intent.putExtra(Constants.PAYMENT_NUMBER_ARGUMENT_NAME, elkanahRef)
        startActivityForResult(intent, PURCHASE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.hasExtra(getString(R.string.data))) {
            val response = data.getSerializableExtra("data") as TransactionResponse?
            AlertDialog.Builder(this)
                .setTitle(response!!.responseCode)
                .setMessage(if (TextUtils.isEmpty(response.responseDescription)) "Empty Response" else response.responseDescription)
                .setPositiveButton(
                    "Okay"
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }.show()
        }
    }
}