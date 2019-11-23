package vitaty14.kg.dispatchchecker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        buttonStart1.setOnClickListener {
            val alertTime = System.currentTimeMillis() + 10000
            val alarmIntent = Intent(this,AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            manager.setExact(AlarmManager.RTC_WAKEUP , alertTime ,pendingIntent)
            Toast.makeText(this,"SET",Toast.LENGTH_SHORT).show()
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"hoge",Toast.LENGTH_LONG).show()
    }
}
