package com.example.carpool.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.carpool.R
import com.example.carpool.api.RequestTravel
import com.example.carpool.data.room.UserDb
import com.example.carpool.data.room.Userdbclass
import com.example.carpool.MainActivity
import com.example.carpool.ui.register.RegisterPresenter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//import com.example.carpool.principalscreen

class ProfileActivity : AppCompatActivity(), ProfilePresenter.View  {
    lateinit var EditNombre:EditText
    lateinit var EditUsuario:EditText
    lateinit var EditTelefono:EditText
    lateinit var EditContra:EditText
    lateinit var actbtn:Button
    private val presenter = ProfilePresenter(this)

    //Inicio de sharedPreferences
    lateinit var preferences: SharedPreferences

    //Objeto User
    private lateinit var manduser: Userdbclass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_perfil)
        preferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE) //SE OBTIENEN LOS SHARED PREFERENCES DE MODO PRIVADO


        getSupportActionBar()?.setHomeButtonEnabled(true)
        EditUsuario=findViewById(R.id.edit_userP)
        EditContra=findViewById(R.id.edit_passwordP)
        EditNombre=findViewById(R.id.edit_nameP)
        EditTelefono=findViewById(R.id.edit_phoneP)
        actbtn = findViewById(R.id.Actualizar)
        setValues()
        dbOperation()
        EditUsuario.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updateUser(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        EditContra.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updatePassword(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        EditNombre.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updateName(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        EditTelefono.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter.updatePhone(s.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        actbtn.setOnClickListener {
            if(EditUsuario.text.toString().isEmpty() ||EditContra.text.toString().isEmpty() ||EditNombre.text.toString().isEmpty() ||EditTelefono.text.toString().isEmpty()){
                Toast.makeText(this, getString(R.string.valid_data), Toast.LENGTH_LONG).show()
                EditNombre.error = getString(R.string.empty_name)
                EditTelefono.error = getString(R.string.empty_phone)
                EditContra.error = getString(R.string.empty_password)
                EditUsuario.error = getString(R.string.empty_username)
                fun EditText.clearError() {
                    error = null
            }}
            else if (EditTelefono.text.length < 10){
                    Toast.makeText(this, getString(R.string.phone_number), Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, getString(R.string.update_data), Toast.LENGTH_LONG).show()
                    dbOperationUpdate()
                }
            }
        }

    override fun finish() {
        val intent = Intent(this, RequestTravel::class.java)
        startActivity(intent)

        // Activity finished ok, return the data
        setResult(RESULT_OK, intent)
        super.finish()
    }
    fun setValues(){
        //obtenemos los valores desde preferencias
        val user = preferences.getString(MainActivity.USERP,"")
        EditUsuario.setText(user)
    }

    private fun setValuesDrawer(userdbclass: Userdbclass) {
        runOnUiThread {
        EditUsuario.setText(userdbclass.User)
        EditContra.setText(userdbclass.Password)
        EditNombre.setText(userdbclass.Name)
        EditTelefono.setText(userdbclass.Phone)}

    }
    private fun dbOperation() {
        //Validacion si existe usuario en la base de datos de la clase User
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            val userdbclass = UserDb
                .getInstance(this)
                ?.userDao()
                ?.viewProfile(EditUsuario.text.toString()) as Userdbclass
            Log.d("user", userdbclass.toString())
            setValuesDrawer(userdbclass)
        })

    }

    override fun dbOperationUpdate() {
        presenter.dbOperationUpdate(this)
    }

}
