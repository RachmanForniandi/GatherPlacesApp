package rachman.forniandi.gatherplacesapp.models

data class DataPlaceModel(
    val id:Int,
    val title:String,
    val image:String,
    val description:String,
    val date:String,
    val location:String,
    val latitude:Double,
    val longitude:Double
)
