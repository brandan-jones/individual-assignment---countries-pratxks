package app.plantdiary.individualassignment304832

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.plantdiary.individualassignment304832.Service.CountryService
import app.plantdiary.individualassignment304832.dto.Country
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var countries = MutableLiveData<List<Country>>()
    var countryService : CountryService = CountryService()

    //for test

    fun fetchCountries() {
        viewModelScope.launch {
            val countryList: List<Country>? = countryService.fetchCountries()
            if (countryList != null) {
                for (country in countryList) {
                    countries.postValue(countryList)
                }
            }
        }
    }

}