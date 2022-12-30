package rachman.forniandi.gatherplacesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rachman.forniandi.gatherplacesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        binding?.fbAddPlaces?.setOnClickListener {
            val intent = Intent(this@MainActivity,AddPlaceActivity::class.java)
            startActivity(intent)
        }
    }
}