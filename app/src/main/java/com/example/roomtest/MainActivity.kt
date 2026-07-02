package com.example.roomtest

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomtest.ui.theme.RoomTestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomTestTheme {
                val test = rememberNavController()
                NavHost(startDestination = "test1",navController = test,){
                    composable("test1") { SignIn(test)  }
                    composable("test2") { Login(test)  }
                }
            }
        }
    }
}

@Entity
data class Test(
    @PrimaryKey var email: String,
    @ColumnInfo var password: String
)

@Dao
interface TestDao{
    @Insert
    suspend fun insert(user: Test)

    @Query("Select * from Test WHERE email = :email And password = :password")
    suspend fun login(email:String,password: String): Test?
}

@Database(entities = [Test::class], version = 1)
abstract class TestDatabase : RoomDatabase(){
    abstract fun testDao(): TestDao
}

@OptIn(InternalCoroutinesApi::class)
object Data {
    @Volatile
    private var IntState: TestDatabase? = null

    fun getData(context: Context): TestDatabase {
        return IntState ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TestDatabase::class.java,
                "user-database"
            ).build()
            IntState = instance
            IntState
        } ?: throw IllegalArgumentException("false")
    }
}

@Composable
fun Login(navController: NavController){
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
            modifier = Modifier.padding(top = 20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        TextField(
            value = passwd,
            onValueChange = { passwd = it },
            label = { Text("passwd") },
            modifier = Modifier.padding(20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        Button(onClick = {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(context,"false",Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,"登入成功",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        },
            modifier = Modifier.padding(20.dp)
        ) {
            Text("chick")
        }

        Text("click me to page1", modifier = Modifier.clickable { navController.navigate("test1")  })
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SignIn(navController: NavController){
    var email by remember{ mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = remember { Data.getData(context    ) }

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
            modifier = Modifier.padding(top = 20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        TextField(
            value = passwd,
            onValueChange = { passwd = it },
            label = { Text("passwd") },
            modifier = Modifier.padding(20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        Button(onClick = {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(context,"false",Toast.LENGTH_SHORT).show()
            } else {
                val user = Test(email = email, password = passwd)
                kotlinx.coroutines.GlobalScope.launch {
                    db.testDao().insert(user)
                }
                Toast.makeText(context,"註冊成功",Toast.LENGTH_SHORT).show()
            }
        },
            modifier = Modifier.padding(20.dp)
        ) { Text("chick") }

        Text("click me to page2", modifier = Modifier.clickable { navController.navigate("test2")  })
    }
}