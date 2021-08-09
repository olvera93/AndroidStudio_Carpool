package com.example.carpool

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.example.carpool.model.User
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity() {

    //Inicio de componentes
    lateinit var usuario: EditText
    lateinit var contrasena:EditText
    lateinit var button: Button
    //Variable global para Action Mode:
    private var actionMode: ActionMode? = null

    //material design button
    private lateinit var loginButton: MaterialButton


    //Creacion de interfaz
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme2)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Creacion del back button
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        //Listener botones material design

        //Descarga de usuarios provinientes de Registros
        val userDB: User = intent.getParcelableExtra("userDB")!!

        //Instancia de componentes
        button = findViewById(R.id.Login_button)
        usuario = findViewById(R.id.EditUsuario)


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
        //Instancia de contraseña
        contrasena = findViewById(R.id.EditContrasena)

        //Generacion de Listener para cambio de texto en el edit de contraseña
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
                //Validacion si existe usuario en la base de datos de la clase User
                if(userDB.validateUser(usuario.text.toString(),contrasena.text.toString())){
                    Toast.makeText(this, getString(R.string.login_succesfully), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, principalscreen::class.java)
                    intent.putExtra("userDB",userDB)

                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, getString(R.string.invalid_user_password), Toast.LENGTH_SHORT).show()
                }
            }
        }
        usuario.setOnLongClickListener{
                if (actionMode == null) actionMode = startSupportActionMode(ActionModeCallback())
                true
            }

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


}



    //Funcion de validacion de campos
fun validacionCampos(campo1:String,campo2:String):Boolean{
    return campo1.isNotEmpty()&&campo1!=null || campo2.length>0 &&campo2!=null
        }
