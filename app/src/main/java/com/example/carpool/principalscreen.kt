package com.example.carpool

import android.content.Context
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.carpool.RecyclerAdapter.TravelHistory
import com.example.carpool.controllers.TravelScreen
import com.example.carpool.controllers.VerPerfil
import com.example.carpool.model.User
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.carpool.model.UserDb
import com.example.carpool.model.Userdbclass
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


const val COORDENADAS_ACTUALES ="org.example.activity.COORDENADAS_ACTUALES"
const val COORDENADAS_DESTINO ="org.example.activity.COORDENADAS_DESTINO"



class principalscreen : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    lateinit var imageCarpool: ImageView
    lateinit var imageView2: ImageView
    lateinit var textView: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView

    lateinit var buttonCoordenadas: Button
    lateinit var coordenadaActual: EditText
    lateinit var coordenadaDestino: EditText

    private lateinit var drawer:DrawerLayout
   // private lateinit var toggle: ActionBarDrawerToggle

    //Drawer header
    lateinit var drawer_header:TextView
    lateinit var drawer_number:TextView
    //Drawe body

    //Inicio de sharedPreferences
    lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = getSharedPreferences(MainActivity.PREFS_NAME,Context.MODE_PRIVATE) //SE OBTIENEN LOS SHARED PREFERENCES DE MODO PRIVADO


        setContentView(R.layout.activity_principalscreen)

        //App Bar
        val appBar = findViewById<Toolbar>(R.id.app_bar)
        this.setSupportActionBar(appBar)
        setupDrawer(appBar)

        //requestLocationPermission()


        imageCarpool = findViewById(R.id.imageView)
        imageView2 = findViewById(R.id.imageView2)
        textView = findViewById(R.id.textView3)
        textView2 = findViewById(R.id.textView4)
        textView3 = findViewById(R.id.textView5)
        coordenadaActual=findViewById(R.id.edit_coordinate_latitude)
        coordenadaDestino=findViewById(R.id.edit_coordinate_longitude)
        buttonCoordenadas=findViewById(R.id.button)


        buttonCoordenadas.setOnClickListener {

            if (coordenadaActual.text.isEmpty()||coordenadaDestino.text.isEmpty()){
                coordenadaActual.error = getString(R.string.coordinates_not_be_empty)
                coordenadaDestino.error = getString(R.string.coordinates_not_be_empty)
            } else {
                if (coordenadaActual.text.toString().toDouble() <= -90.0 && coordenadaActual.text.toString().toDouble() <= 90.0 && coordenadaDestino.text.toString().toDouble() <= -180.0 && coordenadaDestino.text.toString().toDouble() <= 180.0 ) {
                    coordenadaActual.error = getString(R.string.coordinate_invalid)
                    coordenadaDestino.error = getString(R.string.coordinate_invalid)
                } else {
                    val bundle2 =Bundle()
                    bundle2.putString(COORDENADAS_ACTUALES, coordenadaActual.text.toString())
                    bundle2.putString(COORDENADAS_DESTINO, coordenadaDestino.text.toString())
                    val intent = Intent(this, TravelScreen::class.java).apply {
                        putExtras(bundle2)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.translate_left_side,R.anim.translate_left_out);

                }
            }

        }




    }


    //Funciones appBar

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        drawer_header =findViewById(R.id.drawer_user)
        setValues()
        dbOperation()
        drawer_number = findViewById(R.id.drawer_number)




        return super.onCreateOptionsMenu(menu)
    }



    private fun setupDrawer(toolbar: Toolbar){

        drawer = findViewById(R.id.drawer_layout)
        val toggle:ActionBarDrawerToggle = object:ActionBarDrawerToggle (this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer){
                override fun onDrawerClosed(view: View){
                    super.onDrawerClosed(view)

                    //toast("Drawer closed")
                }

                override fun onDrawerOpened(drawerView: View){
                    super.onDrawerOpened(drawerView)

                    //toast("Drawer opened")
                }
            }

        drawer.addDrawerListener(toggle)
        val navigationView:NavigationView= findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener (this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.profile ->{val intent = Intent(this, VerPerfil::class.java)
                startActivity(intent)
            }
            R.id.History ->{val intent = Intent(this, TravelHistory::class.java)
                startActivity(intent)
            }
        }
        drawer = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    //SharedPreferences
    fun goToLogged(){
        //cambiar de actividad sin poder regresar a esta con back button
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    fun setValues(){
        //obtenemos los valores desde preferencias
        val user = preferences.getString(MainActivity.USERP,"")

        drawer_header.text = user
    }

    fun resetShared(){
        preferences.edit().clear().commit() //a diferencia de apply, este cambio se hace de forma asíncrona
    }

    private fun dbOperation() {
        //Validacion si existe usuario en la base de datos de la clase User
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            val userArray = UserDb
                .getInstance(this)
                ?.userDao()
                ?.checkRegister(drawer_header.text.toString()) as MutableList<Userdbclass>
            setValuesDrawer(userArray)
        })
    }

    private fun setValuesDrawer(userArray: MutableList<Userdbclass>) {
        for (user in userArray) {
            drawer_header.text = user.User
            drawer_number.text = user.Phone

        }
    }
    // Para que el usuario acepte los permisos
    private fun requestLocationPermission() {
        // Ya se le habia pedido al usuario dar permiso pero los rechazo
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, getString(R.string.settings_accept), Toast.LENGTH_LONG).show()
        } else {
            // Es la primera vez que se le pide permisos al usuario
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                TravelScreen.REQUEST_CODE_LOCATION
            )
        }
    }

}

