package com.elsharif.landmarkrecognition

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elsharif.landmarkrecognition.models.tfLiteLandmarkClassifierOfEurope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController : NavController){

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )


            }
        },
        modifier = Modifier.fillMaxSize(),
         //   containerColor = Color(0xFF818A6F),


        ) {


        val buttonList = listOf("Africa", "Antarctica","Asia" , "Europe","NorthAmerica","SouthAmerica")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),

        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .shadow(4.dp)
                 //   .background(Color(0xFF818A6F))
                ,
                shape = RoundedCornerShape(10.dp),
             //   colors=CardDefaults.cardColors(Color(0xFF818A6F))

            ){
                Text(
                    text = "Welcome! Explore a curated collection of global landmarks within our app. Easily navigate through distinct sections , to uncover its rich treasures. We hope this guide enhances your journey.              Warm Regards.",
                    maxLines = 7,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.W100,
                    fontFamily = FontFamily.Serif
                )
            }

          //  Spacer(modifier = Modifier.padding(1.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(buttonList) { buttonText ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Button(
                            onClick = {
                                buttonClickTf(navController, buttonText)

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF52733B)) // Set the text color here

                        ) {
                            Text(text = buttonText,
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold

                                )
                        }
                    }
                }
                /*
E4EBF2   5
52733B   1
84A45A   2
818A6F   3
715E4E   4
        */


            }



        }







    }





}

private fun buttonClickTf(
    navController:NavController,
    buttonText: String,
//    context: Context
) {
    when (buttonText) {
        "Africa", "Asia", "Europe","SouthAmerica","NorthAmerica","Antarctica"  -> {
            navController.navigate(buttonText.toLowerCase())
        }
    }
}
/*
#7B8937
#6B7436
#F4D9C1
* */
/*
E4EBF2
52733B
84A45A
818A6F
715E4E
        */