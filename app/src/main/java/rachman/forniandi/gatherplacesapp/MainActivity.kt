package rachman.forniandi.gatherplacesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import pl.kitek.rvswipetodelete.SwipeToEditCallback
import rachman.forniandi.gatherplacesapp.adapters.GatherPlaceAdapter
import rachman.forniandi.gatherplacesapp.databinding.ActivityMainBinding
import rachman.forniandi.gatherplacesapp.dbHandler.DatabaseHandler
import rachman.forniandi.gatherplacesapp.models.DataPlaceModel

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

        getDataPlacesListFromDbLocal()
    }

    private fun getDataPlacesListFromDbLocal(){
        val dbHandler = DatabaseHandler(this)
        val getDataPlaceList :ArrayList<DataPlaceModel> = dbHandler.getDataPlacesList()

        if (getDataPlaceList.size > 0){
            /*for (i in getDataPlaceList){
                Log.e("Title", i.title)
                Log.e("Description", i.description)
            }*/
            binding?.listItemPlaces?.visibility = View.VISIBLE
            binding?.tvNoDataYet?.visibility = View.GONE
            setupListPlaces(getDataPlaceList)
        }else{
            binding?.listItemPlaces?.visibility = View.GONE
            binding?.tvNoDataYet?.visibility = View.VISIBLE
        }
    }

    private fun setupListPlaces(placesList: ArrayList<DataPlaceModel>) {

        //binding?.listItemPlaces?.layoutManager = LinearLayoutManager(this)
        binding?.listItemPlaces?.setHasFixedSize(true)

        val gatherPlaceAdapter = GatherPlaceAdapter(this,placesList)
        binding?.listItemPlaces?.adapter = gatherPlaceAdapter

        gatherPlaceAdapter.setOnClickListener(object :GatherPlaceAdapter.OnClickListener{
            override fun onClick(position: Int, model: DataPlaceModel) {
                val intent = Intent(this@MainActivity,DetailPlaceActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)
                startActivity(intent)
            }

        })

        /*val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.listItemPlaces?.adapter as GatherPlaceAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }*/


    }


    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}