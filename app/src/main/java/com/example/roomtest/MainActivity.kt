package com.example.roomtest

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Patterns
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roomtest.ui.theme.RoomTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomTestTheme {
                Change()
            }
        }
    }
}

@Composable
fun Change (){
    var test = rememberNavController()
    NavHost(startDestination = "test1",navController = test,){
        composable("test1") { RoomTest(test)  }
        composable("test2") { Test2(test)  }
    }
}

@Entity
data class Test(
    @PrimaryKey var email: Email,
    @ColumnInfo var password: String
)

@Composable
fun RoomTest(navController: NavController){
    var email by remember{ mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("註 冊", fontSize = 20.sp)

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") },
            modifier = Modifier.padding(top = 20.dp)
        )

        TextField(
            value = passwd,
            onValueChange = { passwd = it },
            label = { Text("passwd") },
            modifier = Modifier.padding(20.dp)
        )

        Button(onClick = {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(context,"false",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,"true",Toast.LENGTH_SHORT).show()
            }
        },
            modifier = Modifier.padding(20.dp)
        ) {
            Text("chick")
        }

        Text("click me to page2", modifier = Modifier.clickable { navController.navigate("test2")  })
    }
}

@Composable
fun Test2(navController: NavController){
    var email by remember{ mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("登 入", fontSize = 20.sp)

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") },
            modifier = Modifier.padding(top = 20.dp)
        )

        TextField(
            value = passwd,
            onValueChange = { passwd = it },
            label = { Text("passwd") },
            modifier = Modifier.padding(20.dp)
        )

        Button(onClick = {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(context,"false",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,"true",Toast.LENGTH_SHORT).show()
            }
        },
            modifier = Modifier.padding(20.dp)
        ) {
            Text("chick")
        }

        Text("click me to page1", modifier = Modifier.clickable { navController.navigate("test1")  })
    }
}