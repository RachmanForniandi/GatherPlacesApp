package rachman.forniandi.gatherplacesapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import rachman.forniandi.gatherplacesapp.databinding.ActivityAddPlaceBinding
import rachman.forniandi.gatherplacesapp.dbHandler.DatabaseHandler
import rachman.forniandi.gatherplacesapp.models.DataPlaceModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@Suppress("DEPRECATION")
class AddPlaceActivity : AppCompatActivity(),View.OnClickListener {
    private var binding: ActivityAddPlaceBinding?= null
    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    private var mHappyPlaceDetails:DataPlaceModel? = null
    private var selectedSaveImgToInternalStorage:Uri? = null

    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbAddPlace)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "INPUT PLACES"
        }

        binding?.tbAddPlace?.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDateInView()
        }

        updateDateInView()
        binding?.etDate?.setOnClickListener(this)
        binding?.txtAddImg?.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.et_date->{
                DatePickerDialog(this,
                dateSetListener,calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                    .show()
            }
            R.id.txt_add_img->{
                val pictureDialog= AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogOptions = arrayOf("Select photo from gallery",
                    "Select photo from camera")
                pictureDialog.setItems(pictureDialogOptions){
                    dialog,options->
                    when(options){
                        0-> takePhotoFromGallery()
                        1-> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }

            R.id.btn_save->{
                when{
                    binding?.etTitle?.text.isNullOrEmpty()->{
                        Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()

                    }
                    binding?.etDescription?.text.isNullOrEmpty()->{
                        Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding?.etLocation?.text.isNullOrEmpty()->{
                        Toast.makeText(this, "Please select location", Toast.LENGTH_SHORT)
                            .show()
                    }
                    selectedSaveImgToInternalStorage == null->{
                        Toast.makeText(this, "Please add image", Toast.LENGTH_SHORT).show()
                    }
                    else->{
                        val gatherPlaceModel = DataPlaceModel(
                            0,
                            binding?.etTitle?.text.toString(),
                            selectedSaveImgToInternalStorage.toString(),
                            binding?.etDescription?.text.toString(),
                            binding?.etDate?.text.toString(),
                            binding?.etLocation?.text.toString(),
                            mLatitude,
                            mLongitude
                        )

                        val dbHandler = DatabaseHandler(this)
                        if (mHappyPlaceDetails == null){
                            val addDataPlace = dbHandler.addDataPlaces(gatherPlaceModel)

                            if (addDataPlace > 0){
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }else{
                            val updateDataPlace = dbHandler.updateDataPlacesList(gatherPlaceModel)
                            if (updateDataPlace > 0){
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }

        }

    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.getDefault())
        binding?.etDate?.setText(sdf.format(calendar.time).toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY){
                if (data != null){
                    val contentUri = data.data
                    try {
                        @Suppress("DEPRECATION")
                        val selectedImgBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver,contentUri)

                        selectedSaveImgToInternalStorage = saveImgToInternalStorage(selectedImgBitmap)

                        Log.e("saved img_1: ", "Path :: $selectedSaveImgToInternalStorage")
                        binding!!.imgPlace.setImageBitmap(selectedImgBitmap)
                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this,"Failed to load the image from Gallery.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }else if (requestCode == CAMERA){
                val thumbnailImg:Bitmap = data!!.extras!!.get("data") as Bitmap
                selectedSaveImgToInternalStorage = saveImgToInternalStorage(thumbnailImg)
                Log.e("saved img_2: ", "Path :: $selectedSaveImgToInternalStorage")
                binding?.imgPlace?.setImageBitmap(thumbnailImg)

            }
        }
    }





    private fun takePhotoFromGallery(){
        Dexter.withContext(this@AddPlaceActivity)
            .withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .withListener(object:MultiplePermissionsListener{

                override fun onPermissionsChecked(
                    report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted()){

                            val galleryIntent = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(galleryIntent,GALLERY)
                        }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread()
            .check()
    }

    private fun takePhotoFromCamera(){

        Dexter.withContext(this@AddPlaceActivity)
            .withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
            .withListener(object:MultiplePermissionsListener{
                override fun onPermissionsChecked(
                    report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted()){
                        /*Toast
                            .makeText(this,"Storage Read/Write Permission granted. Now you can select an image from storage.",Toast.LENGTH_SHORT).show()
                        */
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(cameraIntent, CAMERA)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread()
            .check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. " +
                    "It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS"){_,_->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }.show()
    }

    private fun saveImgToInternalStorage(bitmap: Bitmap):Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    companion object{
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "CollectionPlacesImages"
        // A constant variable for place picker
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 3
    }
}