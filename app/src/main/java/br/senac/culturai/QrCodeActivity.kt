package br.senac.culturai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.senac.culturai.databinding.ActivityQrCodeBinding
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode

import com.google.zxing.BarcodeFormat

class QrCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityQrCodeBinding
    lateinit var leitorQr: CodeScanner
    var permissaoSucess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissao()
    }

    fun iniciarQr() {
        leitorQr = CodeScanner(this, binding.ScannerView)

        leitorQr.camera = CodeScanner.CAMERA_BACK
        leitorQr.formats = listOf(BarcodeFormat.QR_CODE)
        leitorQr.isAutoFocusEnabled = true
        leitorQr.autoFocusMode = AutoFocusMode.SAFE
        leitorQr.isFlashEnabled = false
        leitorQr.scanMode = ScanMode.SINGLE

        leitorQr.setDecodeCallback {
            runOnUiThread{
                val respIntent = Intent()
                respIntent.putExtra("qrcode", it.text)
                setResult(RESULT_OK, respIntent)
                finish()
            }

        }

        leitorQr.setErrorCallback {
            runOnUiThread{
                Toast.makeText(this, "Não foi possível mostrar a câmera", Toast.LENGTH_LONG)
                    .show()

                Log.e("QrCodeActivity", "iniciarQr", it)

                setResult(RESULT_CANCELED)
                finish()
            }
        }

        leitorQr.startPreview()
    }

    fun permissao() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }else{
            permissaoSucess = true
            iniciarQr()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissaoSucess = true
                iniciarQr()
            }else {
                permissaoSucess = false
                Toast.makeText(this, "Permissão de acesso a camêra negada", Toast.LENGTH_LONG)
                    .show()
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(permissaoSucess) {
            leitorQr.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        if(permissaoSucess) {
            leitorQr.releaseResources()
        }
    }
}