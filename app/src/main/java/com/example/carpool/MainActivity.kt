<<<<<<< HEAD
package com.example.carpool

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.example.carpool.R
import com.example.carpool.ui.register.RegisterActivity
import com.example.carpool.api.RequestTravel
import com.example.carpool.data.models.User


import com.example.carpool.data.room.UserDb
import com.example.carpool.data.room.Userdbclass
import com.example.carpool.ui.animations.AnimationCar
import com.google.android.material.button.MaterialButton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    //Inicio de componentes

    lateinit var usuario: EditText
    lateinit var contrasena:EditText
    lateinit var button: Button

    private lateinit var buttonRegister: Button

    //Variable global para Action Mode:
    private var actionMode: ActionMode? = null

    //material design button
    private lateinit var loginButton: MaterialButton


    //Valores de registro
    companion object{
        val PREFS_NAME = "org.example.carpool.controllers"
        val IS_LOGGED = "is_logged"
        val USERP ="user"
    }
    private lateinit var preferences: SharedPreferences


    override fun onStart() {
        super.onStart()
        if(isLogged()){
            goToLogged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme2)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Permisos
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        //Instancia de componentes
        button = findViewById(R.id.Login_button)
        usuario = findViewById(R.id.edit_user)
        buttonRegister = findViewById(R.id.sign_up)

        buttonRegister.setOnClickListener {
            val tempUsuario =
                User("Admin","5555555555",
                    "Admin", "Admin123")
            val prueba=tempUsuario.addUser()
            intent.putExtra("userDB",tempUsuario)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_left_side, R.anim.translate_left_out)
        }


        //Generacion de Listener para cambio de texto en el edit de usuario
        usuario.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                usuario.error = getString(R.string.empty_username)
                button.isEnabled = s.toString().isNotEmpty()

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        //Instancia de contrase??a
        contrasena = findViewById(R.id.edit_password)

        //Generacion de Listener para cambio de texto en el edit de contrase??a
        contrasena.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                button.isEnabled = s.toString().isNotEmpty()

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })


        //Iniciamos el listener sobre el boton de Login
        button.setOnClickListener {
            if (!validacionCampos(usuario.text.toString(),contrasena.text.toString())) //Validacion de campos
                 {
                Toast.makeText(this, getString(R.string.user_password_invalid), Toast.LENGTH_LONG).show()
                usuario.error = getString(R.string.username_invalid)
                contrasena.error = getString(R.string.password_invalid)

                fun EditText.clearError() {
                    error = null
                }

            } else {

                dbOperation()
            }
        }
        //Listener para implementacion de menu en el EditText usuario
        usuario.setOnLongClickListener{
                if (actionMode == null) actionMode = startSupportActionMode(ActionModeCallback())
                true
            }

        //Listener para implementacion de menu en el EditText contrase??a
        contrasena.setOnLongClickListener{
            if (actionMode == null) actionMode = startSupportActionMode(ActionModeCallback())
            true
        }
        }

    private fun dbOperation() {
        //Validacion si existe usuario en la base de datos de la clase User
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            val userArray = UserDb
                .getInstance(this)
                ?.userDao()
                ?.checkRegister(usuario.text.toString()) as MutableList<Userdbclass>

            if (validateUser(
                    userArray,
                    usuario.text.toString(),
                    contrasena.text.toString()
                )
            )  { saveData()

                val intent2 = Intent(this, AnimationCar::class.java)
                startActivity(intent2)

                runOnUiThread(Runnable{
                    Toast.makeText(
                        this,
                        getString(R.string.login_succesfully),
                        Toast.LENGTH_SHORT
                    ).show()

                    Executors.newSingleThreadScheduledExecutor().schedule({
                        val i= Intent(this, RequestTravel::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        overridePendingTransition(R.anim.translate_left_side,R.anim.translate_left_out)
                    }, 5, TimeUnit.SECONDS)

                })


            } else {
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this,
                        getString(R.string.invalid_user_password),
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
        })
    }

    //Clase interna para implementar el callback de ActionMode
    inner class ActionModeCallback: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = mode?.getMenuInflater()
            inflater?.inflate(R.menu.menu, menu)
            mode?.setTitle("Options Menu")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.option_1 -> {
                    actionMode?.setTitle(getString(R.string.copy))
                    Toast.makeText(this@MainActivity, getString(R.string.copy_action), Toast.LENGTH_SHORT).show()
                    return true
                }
                R.id.option_2->{
                    actionMode?.setTitle(getString(R.string.paste))
                    Toast.makeText(this@MainActivity, getString(R.string.paste_action), Toast.LENGTH_SHORT).show()
                }
            }
            return false
        }

    }

//FUNCIONES DE SHARED PREFERENCES
    private fun saveData(){ //A??adimos la data a preferences
        val userpre= usuario.text.toString()

        preferences.edit()
            .putString(USERP,userpre)
            .putBoolean(IS_LOGGED,true)
            .apply()
    }

    private fun isLogged():Boolean{//Si esta logeado
        return preferences.getBoolean(IS_LOGGED,false)
    }
    private fun goToLogged(){//Nos manda a la siguiente actividad
        val i= Intent(this, RequestTravel::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}

    //Funcion de validacion de campos
fun validacionCampos(campo1:String,campo2:String):Boolean{
    return campo1.isNotEmpty()&&campo1!=null || campo2.length>0 &&campo2!=null
        }

fun validateUser(userArray:MutableList<Userdbclass>, usuario:String, contra:String):Boolean{
    for(user in userArray){
        if(user.User==usuario && user.Password==contra){
            return true
        }
    }
    return false
}
=======
package com.example.carpool

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.example.carpool.R
import com.example.carpool.ui.register.RegisterActivity
import com.example.carpool.api.RequestTravel
import com.example.carpool.data.models.User


import com.example.carpool.data.room.UserDb
import com.example.carpool.data.room.Userdbclass
import com.example.carpool.ui.animations.AnimationCar
import com.google.android.material.button.MaterialButton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    //Inicio de componentes

    lateinit var usuario: EditText
    lateinit var contrasena:EditText
    lateinit var button: Button

    private lateinit var buttonRegister: Button

    //Variable global para Action Mode:
    private var actionMode: ActionMode? = null

    //material design button
    private lateinit var loginButton: MaterialButton


    //Valores de registro
    companion object{
        val PREFS_NAME = "org.example.carpool.controllers"
        val IS_LOGGED = "is_logged"
        val USERP ="user"
    }
    private lateinit var preferences: SharedPreferences


    override fun onStart() {
        super.onStart()
        if(isLogged()){
            goToLogged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme2)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Permisos
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        //Instancia de componentes
        button = findViewById(R.id.Login_button)
        usuario = findViewById(R.id.edit_user)
        buttonRegister = findViewById(R.id.sign_up)

        buttonRegister.setOnClickListener {
            val tempUsuario =
                User("Admin","5555555555",
                    "Admin", "Admin123")
            val prueba=tempUsuario.addUser()
            intent.putExtra("userDB",tempUsuario)
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.translate_left_side, R.anim.translate_left_out)
        }


        //Generacion de Listener para cambio de texto en el edit de usuario
        usuario.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                usuario.error = getString(R.string.empty_username)
                button.isEnabled = s.toString().isNotEmpty()

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        //Instancia de contrase??a
        contrasena = findViewById(R.id.edit_password)

        //Generacion de Listener para cambio de texto en el edit de contrase??a
        contrasena.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                button.isEnabled = s.toString().isNotEmpty()

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })


        //Iniciamos el listener sobre el boton de Login
        button.setOnClickListener {
            if (!validacionCampos(usuario.text.toString(),contrasena.text.toString())) //Validacion de campos
                 {
                Toast.makeText(this, getString(R.string.user_password_invalid), Toast.LENGTH_LONG).show()
                usuario.error = getString(R.string.username_invalid)
                contrasena.error = getString(R.string.password_invalid)

                fun EditText.clearError() {
                    error = null
                }

            } else {

                dbOperation()
            }
        }
        //Listener para implementacion de menu en el EditText usuario
        usuario.setOnLongClickListener{
                if (actionMode == null) actionMode = startSupportActionMode(ActionModeCallback())
                true
            }

        //Listener para implementacion de menu en el EditText contrase??a
        contrasena.setOnLongClickListener{
            if (actionMode == null) actionMode = startSupportActionMode(ActionModeCallback())
            true
        }
        }

    private fun dbOperation() {
        //Validacion si existe usuario en la base de datos de la clase User
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            val userArray = UserDb
                .getInstance(this)
                ?.userDao()
                ?.checkRegister(usuario.text.toString()) as MutableList<Userdbclass>

            if (validateUser(
                    userArray,
                    usuario.text.toString(),
                    contrasena.text.toString()
                )
            )  { saveData()

                val intent2 = Intent(this, AnimationCar::class.java)
                startActivity(intent2)

                runOnUiThread(Runnable{
                    Toast.makeText(
                        this,
                        getString(R.string.login_succesfully),
                        Toast.LENGTH_SHORT
                    ).show()

                    Executors.newSingleThreadScheduledExecutor().schedule({
                        val i= Intent(this, RequestTravel::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        overridePendingTransition(R.anim.translate_left_side,R.anim.translate_left_out)
                    }, 5, TimeUnit.SECONDS)

                })


            } else {
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this,
                        getString(R.string.invalid_user_password),
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
        })
    }

    //Clase interna para implementar el callback de ActionMode
    inner class ActionModeCallback: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = mode?.getMenuInflater()
            inflater?.inflate(R.menu.menu, menu)
            mode?.setTitle("Options Menu")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return true
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.option_1 -> {
                    actionMode?.setTitle(getString(R.string.copy))
                    Toast.makeText(this@MainActivity, getString(R.string.copy_action), Toast.LENGTH_SHORT).show()
                    return true
                }
                R.id.option_2->{
                    actionMode?.setTitle(getString(R.string.paste))
                    Toast.makeText(this@MainActivity, getString(R.string.paste_action), Toast.LENGTH_SHORT).show()
                }
            }
            return false
        }

    }

//FUNCIONES DE SHARED PREFERENCES
    private fun saveData(){ //A??adimos la data a preferences
        val userpre= usuario.text.toString()

        preferences.edit()
            .putString(USERP,userpre)
            .putBoolean(IS_LOGGED,true)
            .apply()
    }

    private fun isLogged():Boolean{//Si esta logeado
        return preferences.getBoolean(IS_LOGGED,false)
    }
    private fun goToLogged(){//Nos manda a la siguiente actividad
        val i= Intent(this, RequestTravel::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}

    //Funcion de validacion de campos
fun validacionCampos(campo1:String,campo2:String):Boolean{
    return campo1.isNotEmpty()&&campo1!=null || campo2.length>0 &&campo2!=null
        }

fun validateUser(userArray:MutableList<Userdbclass>, usuario:String, contra:String):Boolean{
    for(user in userArray){
        if(user.User==usuario && user.Password==contra){
            return true
        }
    }
    return false
}
>>>>>>> 86563378c17c2696ad38567ad2f0a6c554cff2bf
