package app.plantdiary.individualassignment304832

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.plantdiary.individualassignment304832.dto.Country
import app.plantdiary.individualassignment304832.ui.theme.IndividualAssignment304832Theme
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.Observer

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchCountries()
            val countries by viewModel.countries.observeAsState(initial = emptyList())
            IndividualAssignment304832Theme {
                Column {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        Greeting("Android")
                    }
                    Countries(countries)
                    TextFieldWithDropdownUsage(countries)
                    ButtonBar()
                }
            }

        }
    }


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Composable
    fun ButtonBar() {
        Row(modifier = Modifier.padding(all = 2.dp)) {
        }

        val context = LocalContext.current
        Button(
            onClick = {
                Toast.makeText(context, "Selected Country $strSelectedCountry", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier.padding(all = Dp(10F)),
            enabled = true,
            border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Blue)),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = "Sample", color = Color.White)
        }

    }

    @Composable
    fun Countries(countriesIn: List<Country>) {
        var countryName : String by remember { mutableStateOf("United States") }
        var expanded by remember { mutableStateOf(false) }

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                Modifier
                    .padding(24.dp)
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text= countryName, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                    countriesIn.forEach {
                            country -> DropdownMenuItem(onClick = {
                        expanded = false
                        countryName = country.toString()
                    }) {
                        Text(text = country.toString())
                    }
                    }
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        IndividualAssignment304832Theme {
            Column {
                Greeting("Android")
                ButtonBar()
            }
        }
    }

    var strSelectedCountry = "No country selected"
    var selectedCountrty = Country("","")

    @Composable
    fun TextFieldWithDropdownUsage(countriesIn: List<Country>) {

        val dropDownOptions = remember{ mutableStateOf(listOf<Country>())}
        val textFieldValue = remember {mutableStateOf(TextFieldValue())}
        val dropDownExpanded = remember {mutableStateOf(false)}

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            strSelectedCountry = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = countriesIn.filter { it.toString().startsWith(value.text) && it.toString() != value.text }.take(3)
        }

        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = "Country"
        )
    }

    @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<Country>,
        label: String = ""
    ) {
        Box(modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = { Text(label) },
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                list.forEach { text ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text.toString(),
                                TextRange(text.toString().length)
                            )
                        )
                    }) {
                        Text(text = text.toString())
                    }
                }
            }
        }
    }
}