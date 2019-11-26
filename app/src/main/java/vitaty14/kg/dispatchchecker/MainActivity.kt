package vitaty14.kg.dispatchchecker

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "dispatch_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel =  NotificationChannel (
                CHANNEL_ID,
                "派遣終了",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableLights(true)
            channel.lightColor = Color.WHITE
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            manager.createNotificationChannel(channel)
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val oldTime1 = sharedPreferences.getString("Time1",null)
        val oldTime2 = sharedPreferences.getString("Time2",null)
        val oldTime3 = sharedPreferences.getString("Time3",null)

        if (oldTime1 != null) {
            textView1.text = "派遣①終了時刻:$oldTime1"
        }
        if (oldTime2 != null) {
            textView2.text = "派遣①終了時刻:$oldTime2"
        }
        if (oldTime3 != null) {
            textView3.text = "派遣①終了時刻:$oldTime3"
        }

        buttonStart1.setOnClickListener {
            val checkFlag1 = sharedPreferences.getBoolean("TimerFlag1",false)
            if (editTime1.text.trim().isNotEmpty() && !checkFlag1) {
                val getTime: Int = editTime1.text.toString().toInt()
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.MINUTE, getTime)

                val dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
                dataFormat.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
                val checkTime = dataFormat.format(calendar.time)

                val alarmIntent = Intent(this, AlarmReceiver::class.java)
                alarmIntent.putExtra("requestCode",10001)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    10001,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                manager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, null), pendingIntent)
                Toast.makeText(this, "SET:${editTime1.text.trim()}分", Toast.LENGTH_SHORT).show()
                textView1.text = "派遣①終了時刻:$checkTime"

                edit.putString("Time1",checkTime)
                edit.putBoolean("TimerFlag1",true)
                edit.apply()
            } else {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            }
        }
        buttonDelete1.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Alarm Cancel")
                setMessage("Do you cancel?")
                setPositiveButton("OK") { _, _ ->
                    val alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        this@MainActivity,
                        10001,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    pendingIntent.cancel()
                    manager.cancel(pendingIntent)
                    textView1.text = "派遣①"
                    edit.remove("TimerFlag1")
                    edit.remove("Time1")
                    edit.apply()
                }
                setNegativeButton("Cancel", null)
                show()
            }
        }
        buttonStart2.setOnClickListener {
            val checkFlag2 = sharedPreferences.getBoolean("TimerFlag2",false)
            if (editTime2.text.trim().isNotEmpty() && !checkFlag2) {
                val getTime: Int = editTime2.text.toString().toInt()
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.MINUTE, getTime)

                val dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
                dataFormat.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
                val checkTime = dataFormat.format(calendar.time)

                val alarmIntent = Intent(this, AlarmReceiver::class.java)
                alarmIntent.putExtra("requestCode",10002)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    10002,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                manager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, null), pendingIntent)
                Toast.makeText(this, "SET:${editTime2.text.trim()}分", Toast.LENGTH_SHORT).show()
                textView2.text = "派遣②終了時刻:$checkTime"

                edit.putString("Time2",checkTime)
                edit.putBoolean("TimerFlag2",true)
                edit.apply()
            } else {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            }
        }
        buttonDelete2.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Alarm Cancel")
                setMessage("Do you cancel?")
                setPositiveButton("OK") { _, _ ->
                    val alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        this@MainActivity,
                        10002,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    pendingIntent.cancel()
                    manager.cancel(pendingIntent)
                    textView2.text = "派遣②"
                    edit.remove("TimerFlag2")
                    edit.remove("Time2")
                    edit.apply()
                }
                setNegativeButton("Cancel", null)
                show()
            }
        }
        buttonStart3.setOnClickListener {
            val checkFlag3 = sharedPreferences.getBoolean("TimerFlag3",false)
            if (editTime3.text.trim().isNotEmpty() && !checkFlag3) {
                val getTime: Int = editTime3.text.toString().toInt()
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.MINUTE, getTime)

                val dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
                dataFormat.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
                val checkTime = dataFormat.format(calendar.time)

                val alarmIntent = Intent(this, AlarmReceiver::class.java)
                alarmIntent.putExtra("requestCode",10003)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    10003,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                manager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, null), pendingIntent)
                Toast.makeText(this, "SET:${editTime3.text.trim()}分", Toast.LENGTH_SHORT).show()
                textView3.text = "派遣③終了時刻:$checkTime"

                edit.putString("Time3",checkTime)
                edit.putBoolean("TimerFlag3",true)
                edit.apply()
            } else {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            }
        }
        buttonDelete3.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Alarm Cancel")
                setMessage("Do you cancel?")
                setPositiveButton("OK") { _, _ ->
                    val alarmIntent = Intent(this@MainActivity, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        this@MainActivity,
                        10003,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    pendingIntent.cancel()
                    manager.cancel(pendingIntent)
                    textView3.text = "派遣③"
                    edit.remove("TimerFlag3")
                    edit.remove("Time3")
                    edit.apply()
                }
                setNegativeButton("Cancel", null)
                show()
            }
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val requestCode:Int = intent.getIntExtra("requestCode",0)
        val notifyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, requestCode, notifyIntent, 0)

        val notification : Notification.Builder? = Notification.Builder(context)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentTitle("派遣終了")
            .setContentText("指定時間が経過しました")
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notification?.setChannelId("dispatch_channel")
                }
        
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification?.build())

        when(requestCode) {
            10001 -> {
                edit.remove("TimerFlag1")
                edit.remove("Time1")
                edit.apply()
            }
            10002 -> {
                edit.remove("TimerFlag2")
                edit.remove("Time2")
                edit.apply()
            }
            10003 -> {
                edit.remove("TimerFlag3")
                edit.remove("Time3")
                edit.apply()
            }
            else -> {
                //Nothing to do
            }
        }
    }
}