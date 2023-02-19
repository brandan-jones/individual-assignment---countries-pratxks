package app.plantdiary.individualassignment304832

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    viewModel { MainViewModel() }
}