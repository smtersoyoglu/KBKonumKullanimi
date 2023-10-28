package com.sametersoyoglu.kbkonumkullanimi

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.sametersoyoglu.kbkonumkullanimi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var izinKontrol = 0

    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask : Task<Location> // konumla ilgili bilgi almamızı sağlar. son konumu almamızı.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        flpc = LocationServices.getFusedLocationProviderClient(this)

        binding.buttonKonumAl.setOnClickListener {
            // izni kontrol etme onaylandı mı onaylanmadı mı diye bakıyoruz.
            izinKontrol = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)

            if (izinKontrol == PackageManager.PERMISSION_GRANTED) { // izin onaylanmışsa
                locationTask = flpc.lastLocation // son konumu alma
                konumBilgisiAl()
            }else{// izin onaylanmamışsa
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)

            }
        }
    }

    // konum bilgisini alıp textviewlere aktarma
    fun konumBilgisiAl() {
        locationTask.addOnSuccessListener {
            if (it != null) {// eğer bir konum varsa burası çalışacak
                binding.textViewEnlem.text = "Enlem : ${it.latitude}"
                binding.textViewBoylam.text = "Boylam : ${it.longitude}"

            }else {
                binding.textViewEnlem.text = "Enlem : Bulunamadı"
                binding.textViewBoylam.text = "Boylam : Bulunamadı"
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){ // gönderildimi kontrol etme
            izinKontrol = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // onaylandınmı kontrol etme
                locationTask = flpc.lastLocation
                konumBilgisiAl()
            }else {
                Toast.makeText(applicationContext,"İzin onaylanmadı",Toast.LENGTH_SHORT).show()
            }

        }

    }
}