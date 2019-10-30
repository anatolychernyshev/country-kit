package com.anatolychernyshev.countrykit

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.RawRes
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

object CountryKit {

    private const val DRAWABLE_TYPE = "drawable"

    /*
    * Get a country flag drawable from country code string. Example: CountryFlagKit.getFrom(this, "US").
    * It will return circle drawable for US. If country won`t be found, it will throw an exception.
    * You can handle it with try catch and put some placeholder.
    * */
    @Throws(Resources.NotFoundException::class)
    fun getFrom(context: Context, countryCode: String): Drawable? {
        val correctName = countryCode.toLowerCase(Locale.US)
        val res = context.resources
        val resourceId = res.getIdentifier("${correctName}_svg", DRAWABLE_TYPE, context.packageName)
        return ResourcesCompat.getDrawable(res, resourceId, null)
    }

    /*
    * Get a list of countries like Country(name, dialCode, code)
    * */
    fun countries(context: Context): List<Country> = fromJson(context.resources.rawToString(R.raw.countries))

    data class Country(
        val name: String,
        @SerializedName("dial_code")
        val dialCode: String,
        val code: String
    )

    private inline fun <reified T> fromJson(json: String): T = Gson().fromJson(json, genericType<T>())
    private inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type

    private fun Resources.rawToString(@RawRes resourceId: Int) =
        openRawResource(resourceId).use { it.bufferedReader().readText() }
}