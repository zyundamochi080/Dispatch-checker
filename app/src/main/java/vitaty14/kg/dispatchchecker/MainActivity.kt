package vitaty14.kg.dispatchchecker

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.deploygate.sdk.DeployGate
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        buttonStart1.setOnClickListener {
            if (editTime1.text.trim().isNotEmpty()) {
                val getTime: Int = editTime1.text.toString().toInt()
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.MINUTE, getTime)

                val alarmIntent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    10001,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                manager.setAlarmClock(AlarmManager.AlarmClockInfo(calendar.timeInMillis, null), pendingIntent)
                Toast.makeText(this, "SET:${editTime1.text.trim()}分", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"not found",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context,"hoge",Toast.LENGTH_LONG).show()
        DeployGate.logDebug("onReceive")

        val notifyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0)

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
    }
}
