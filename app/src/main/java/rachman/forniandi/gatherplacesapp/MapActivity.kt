package rachman.forniandi.gatherplacesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import rachman.forniandi.gatherplacesapp.databinding.ActivityMapBinding
import rachman.forniandi.gatherplacesapp.models.DataPlaceModel

class MapActivity : AppCompatActivity(),OnMapReadyCallback {

    private var binding: ActivityMapBinding?= null

    private var placeModel:DataPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            placeModel = intent.getSerializableExtra(
                MainActivity.EXTRA_PLACE_DETAILS)as DataPlaceModel
        }

        if (placeModel != null){
            setSupportActionBar(binding?.tbMap)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = placeModel?.title
        }

        binding?.tbMap?.setNavigationOnClickListener {
            onBackPressed()
        }

        val supportMapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(
            placeModel!!.latitude,
            placeModel!!.longitude
        )

        googleMap.addMarker(MarkerOptions().position(position)
            .title(placeModel?.location))

        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)
    }
}