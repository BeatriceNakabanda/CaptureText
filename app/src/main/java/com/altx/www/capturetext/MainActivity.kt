package com.altx.www.capturetext

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var signatureView: SignatureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        signatureView = findViewById(R.id.signature_view)

        val btnSave: Button = findViewById(R.id.btn_save)
        btnSave.setOnClickListener {
            saveSignature()
        }
    }

    private fun saveSignature() {
        val signatureBitmap: Bitmap = signatureView.getSignatureBitmap()
        val file = File(getExternalFilesDir(null), "signature.png")
        try {
            FileOutputStream(file).use { out ->
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                Log.e("MAIN", "Signature saved to ${file.absolutePath}")
                Toast.makeText(this, "Signature saved to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save signature", Toast.LENGTH_SHORT).show()
        }

        // Notify the media scanner to make the file visible
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        sendBroadcast(intent)

        // Start DisplaySignatureActivity with the file path
        val displayIntent = Intent(this, DisplaySignatureActivity::class.java)
        displayIntent.putExtra("filePath", file.absolutePath)
        startActivity(displayIntent)
    }
}