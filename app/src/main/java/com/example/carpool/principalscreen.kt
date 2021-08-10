package com.example.carpool

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.RecyclerView
import com.example.carpool.RecyclerAdapter.TravelHistory
import com.example.carpool.controllers.TravelScreen
import com.example.carpool.controllers.VerPerfil
import com.example.carpool.model.User
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout

const val COORDENADAS_ACTUALES ="org.example.activity.COORDENADAS_ACTUALES"
const val COORDENADAS_DESTINO ="org.example.activity.COORDENADAS_DESTINO"



class principalscreen : AppCompatActivity() {


    lateinit var buttonCoordenadas: Button
    lateinit var coordenadaActual: EditText
    lateinit var coordenadaDestino: EditText
    lateinit var buttonverPerfil:  Button

    //Drawer header
    lateinit var  drawer_header:TextView
    lateinit var drawer_number:TextView


    //Descarga bundle de Login



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_principalscreen)
        val userDB:User?= intent.getParcelableExtra<User>("userDB")!!
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        //App Bar
        val appBar = findViewById<Toolbar>(R.id.app_bar)
        this.setSupportActionBar(appBar)







        coordenadaActual=findViewById(R.id.EditCoordenadaActual)
        coordenadaDestino=findViewById(R.id.EditCoordenadaDestino)
        buttonCoordenadas=findViewById(R.id.button)
        buttonverPerfil=findViewById(R.id.VerPerfilbtn)

        setupDrawer(appBar)

        val btnTravelHistory = findViewById<Button>(R.id.btnTravelHistory)

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
                }
            }




        }
        buttonverPerfil.setOnClickListener {


        }

        btnTravelHistory.setOnClickListener {
            //bundle.putString(USER_NAME, usuario.toString())
            val intent = Intent(this, TravelHistory::class.java).apply {
                //putExtras(bundle)
            }
            startActivity(intent)


        }


    }


    //Funciones appBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val userDB:User?= intent.getParcelableExtra<User>("userDB")!!
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        drawer_header =findViewById(R.id.drawer_user)
        drawer_number = findViewById(R.id.drawer_number)
        drawer_header.text=userDB?.name
        drawer_number.text=userDB?.phone
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item_contact: MenuItem): Boolean {
        var msg = ""
        val userDB:User?= intent.getParcelableExtra<User>("userDB")!!
        when(item_contact.itemId){
            R.id.profile ->{val intent = Intent(this, VerPerfil::class.java)
                intent.putExtra("userDB",userDB)
                startActivity(intent)}
            R.id.share -> msg=getString(R.string.sharing_element)
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item_contact)
    }
    private fun setupDrawer(toolbar: Toolbar){
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer)
    }





}

