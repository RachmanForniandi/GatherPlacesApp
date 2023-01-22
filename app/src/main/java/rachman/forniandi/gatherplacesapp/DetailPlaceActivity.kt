package rachman.forniandi.gatherplacesapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rachman.forniandi.gatherplacesapp.databinding.ActivityAddPlaceBinding
import rachman.forniandi.gatherplacesapp.databinding.ActivityDetailPlaceBinding
import rachman.forniandi.gatherplacesapp.models.DataPlaceModel

class DetailPlaceActivity : AppCompatActivity() {

    private var binding: ActivityDetailPlaceBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var placeModel:DataPlaceModel? = null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            placeModel = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as DataPlaceModel
        }

        if (placeModel != null){
            setSupportActionBar(binding?.tbPlaceDetail)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = placeModel.title

            binding?.tbPlaceDetail?.setNavigationOnClickListener {
                onBackPressed()
            }

            binding?.imgPlace?.setImageURI(Uri.parse(placeModel.image))
        }
    }
}