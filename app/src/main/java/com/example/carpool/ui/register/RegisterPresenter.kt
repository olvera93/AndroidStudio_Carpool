package com.example.carpool.ui.register

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.carpool.R
import com.example.carpool.data.models.User
import com.example.carpool.data.room.UserDb
import com.example.carpool.data.room.Userdbclass
import com.example.carpool.MainActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class RegisterPresenter(view: View) : AppCompatActivity() {
    var user= User()

    fun updateName(name: String){
        user.name = name
    }

    fun updatePhone(phone: String){
        user.phone = phone
    }

    fun updateUser(usuario: String){
        user.user = usuario
    }

    fun updatePassword(contra: String){
        user.password = contra
    }

    fun addExistingUsersToDB(activity: RegisterActivity){
        val tempUsuario =
            User("Admin","5555555555",
                "Admin", "Admin123")
        val prueba=tempUsuario.addUser()
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("userDB",tempUsuario)
        activity.startActivity(intent)
    }

    fun addNewUserToDB(activity: RegisterActivity){
        //Database register
        val baseUser= Userdbclass(
            User=user.user,
            Password = user.password,
            Phone = user.phone,
            Name = user.name
        )
        val tempUsuario =
            User(user.name,user.phone,
                user.user, user.password)

        val executor: ExecutorService = Executors.newSingleThreadExecutor()

        executor.execute (Runnable {
            UserDb
                .getInstance(activity)
                ?.userDao()
                ?.insertUser(baseUser)
            runOnUiThread(Runnable {
                expandableNotification()
                Toast.makeText(this, getString(R.string.successfully_registered), Toast.LENGTH_LONG).show()
            })
        })
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

    private fun expandableNotification() {
        val notification = NotificationCompat.Builder(this, RegisterActivity.CHANNEL_TRAVEL)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setColor(ContextCompat.getColor(this, R.color.primaryColor))
            .setContentTitle(getString(R.string.simple_title))
            .setContentText(getString(R.string.large_text))
            .setLargeIcon(getDrawable(R.mipmap.carpool)?.toBitmap())
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.large_text)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this).run {
            notify(++RegisterActivity.notificationId, notification)

        }

    }

    interface View{
        fun addExistingUsersToDB()
        fun addNewUserToDB()
    }
}