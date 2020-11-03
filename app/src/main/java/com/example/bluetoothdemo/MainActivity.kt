package com.example.bluetoothdemo

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT: Int = 1;
    private val REQUEST_CODE_DISCOVERABLE_BT: Int = 2;

    // bluetooth adapter
    lateinit var bAdapter: BluetoothAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // init
        bAdapter = BluetoothAdapter.getDefaultAdapter()
        var bluetoothStatusTv = findViewById<TextView>(R.id.bluetoothStatusTv)
        var pairedTv = findViewById<TextView>(R.id.pairedTv)
        var bluetoothIv = findViewById<ImageView>(R.id.bluetoothIv)
        var turnOnBtn = findViewById<Button>(R.id.turnOnBtn)
        var turnOffBtn = findViewById<Button>(R.id.turnOffBtn)
        var discoverableBtn = findViewById<Button>(R.id.discoverableBtn)
        var pairedBtn = findViewById<Button>(R.id.pairedBtn)

        // check if bluetooth is on/off
        if (bAdapter == null) {
            bluetoothStatusTv.text = "Bluetooth is not available"
        } else {
            bluetoothStatusTv.text = "bluetooth is available"
        }
        // Set image according to bluetooth status(on/off)
        if (bAdapter.isEnabled) {
            // On
            bluetoothIv.setImageResource(R.drawable.ic_bluetooth_on)
        } else {
            // Off
            bluetoothIv.setImageResource(R.drawable.ic_bluetooth_off)
        }

        // turn on bluetooth
        turnOnBtn.setOnClickListener {
            if (bAdapter.isEnabled) {
                // already enabled
                Toast.makeText(this, "Already on", Toast.LENGTH_LONG).show()
            } else {
                // turn on bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
                bluetoothIv.setImageResource(R.drawable.ic_bluetooth_on)
            }
        }
        // turn off bluetooth
        turnOffBtn.setOnClickListener {
            if (!bAdapter.isEnabled) {
                // already enabled
                Toast.makeText(this, "Already off", Toast.LENGTH_LONG).show()
            } else {
                // turn on bluetooth
                bAdapter.disable()
                bluetoothIv.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_LONG).show()
            }
        }

        // discoverable the bluetooth
        discoverableBtn.setOnClickListener {
            if (!bAdapter.isDiscovering) {
                Toast.makeText(this, "Making Your device discoverable", Toast.LENGTH_LONG).show()
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)
                pairedTv.text = "Your Devices Are Discoverable"
                bluetoothIv.setImageResource(R.drawable.ic_bluetooth_on)
            }
        }

        // get list of paired devices
        pairedBtn.setOnClickListener {
            if (bAdapter.isEnabled) {
                pairedTv.text = "Paired Devices"
                // get list of paired devices
                val devices = bAdapter.bondedDevices
                for (device in devices) {
                    val deviceName = device.name
                    pairedTv.append("\nDevice: $deviceName , $device")
                }
            } else {
                Toast.makeText(this, "Turn on bluetooth first", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ENABLE_BT ->
                if (requestCode == 1) {
                    findViewById<ImageView>(R.id.bluetoothIv).setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Could not on bluetooth", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}