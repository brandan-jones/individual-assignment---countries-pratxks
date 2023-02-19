package app.plantdiary.individualassignment304832.dto

import com.google.gson.annotations.SerializedName

data class Country (@SerializedName("code")var code : String, var name: String){
    override fun toString():String{
        return name + " " + code

    }

}