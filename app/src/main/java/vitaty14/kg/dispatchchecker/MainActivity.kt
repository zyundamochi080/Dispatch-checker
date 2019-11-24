package vitaty14.kg.dispatchchecker

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val oldTime1 = sharedPreferences.getString("Time1",null)
        if (oldTime1 != null) {
            textView1.text = "派遣①終了時刻:$oldTime1"
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

        val notification= NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentTitle("派遣終了")
            .setContentText("指定時間が経過しました")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)

        if (requestCode == 10001) {
            edit.remove("TimerFlag1")
            edit.remove("Time1")
            edit.apply()
        }
    }
}