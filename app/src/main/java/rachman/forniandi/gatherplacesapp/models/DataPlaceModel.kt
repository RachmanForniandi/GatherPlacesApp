package rachman.forniandi.gatherplacesapp.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class DataPlaceModel(
    val id:Int,
    val title:String?,
    val image:String?,
    val description:String?,
    val date:String?,
    val location:String?,
    val latitude:Double,
    val longitude:Double
): Serializable
/*{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeString(location)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataPlaceModel> {
        override fun createFromParcel(parcel: Parcel): DataPlaceModel {
            return DataPlaceModel(parcel)
        }

        override fun newArray(size: Int): Array<DataPlaceModel?> {
            return arrayOfNulls(size)
        }
    }

}*/
