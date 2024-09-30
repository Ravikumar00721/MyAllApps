package msi.crool.musicapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView() {
    val viewModel: MainViewModel = viewModel()
    val currentScreen = remember { viewModel.currentScreen.value }
    val dialogOpen = remember { mutableStateOf(false) }
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val title = remember { mutableStateOf(currentScreen.title) }
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }
    val modifier=if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    val modalSheetState= androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {it!=ModalBottomSheetValue.HalfExpanded}
    )

    val roundedCornerRadius=if(isSheetFullScreen) 0.dp else 12.dp

    val bottomBar : @Composable () ->Unit = {
        if(currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home)
        {
            BottomNavigation( Modifier.wrapContentSize()) {
               KingBowser.forEach {
                   item->
                   val selected =currentRoute==item.bRoute
                   val tint=if(selected) Color.White else Color.Black
                   BottomNavigationItem(
                       onClick = { controller.navigate(item.bRoute )
                                 title.value=item.bTitle},
                       selected =currentRoute==item.bRoute,
                       icon = {
                           Icon(tint=tint,painter = painterResource(id = item.icon),
                               contentDescription = item.bTitle)},
                       label = { Text(text = item.bTitle, color = tint)},
                       selectedContentColor = Color.White,
                       unselectedContentColor = Color.Black
                       )
               }
            }
        }
    }

   ModalBottomSheetLayout(
       sheetState = modalSheetState,
       sheetShape = RoundedCornerShape(topEnd = roundedCornerRadius, topStart = roundedCornerRadius),
       sheetContent = {
       MoreBottonSheet(modifier = modifier)
   }) {
       androidx.compose.material.Scaffold(

           topBar = {
               TopAppBar(
                   actions = {
                             IconButton(
                                 onClick = {scope.launch {
                                     if(modalSheetState.isVisible)
                                     {
                                        modalSheetState.hide()
                                     }else
                                     {
                                       modalSheetState.show()
                                     }
                                 }  }) {
                                 Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                             }
                   },
                   title = { Text(text = title.value) },
                   navigationIcon = {
                       IconButton(onClick = {
                           scope.launch {
                               scaffoldState.drawerState.open()
                           }
                       }) {
                           Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
                       }
                   },
                   colors = TopAppBarDefaults.topAppBarColors(
                       containerColor = Color.LightGray
                   )
               )
           },
           scaffoldState = scaffoldState,
           bottomBar = bottomBar,
           drawerContent = {
               LazyColumn(Modifier.padding(16.dp)) {
                   items(drawerInScreen) { item ->
                       DrawerItem(
                           selected = currentRoute == item.dRoute,
                           item = item,
                           onDrawerItemClicked = {
                               scope.launch {
                                   scaffoldState.drawerState.close()
                               }
                               if (item.dRoute == "add_account") {
                                   dialogOpen.value = true
                               } else {
                                   controller.navigate(item.dRoute)
                                   title.value = item.dTitle
                               }
                           }
                       )
                   }
               }
           }
       ) {
           Navigation(navController = controller, viewModel = viewModel, pd = it)
           AccountDialog(dialogOpen = dialogOpen)
       }
   }
   }



@Composable
fun MoreBottonSheet(modifier: Modifier)
{
    Box (
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colors.primarySurface)
    ){
       Column(
           modifier=modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
       ) {
           Row (
               modifier=modifier.padding(end = 16.dp, top = 16.dp)
           ){
               Icon(painter = painterResource(id = R.drawable.r_settings),
                   contentDescription = "Settings")
               Text(text = "Settings", fontSize = 20.sp, color = Color.White)
           }
           Row (
               modifier=modifier.padding(end = 16.dp,top = 16.dp)
           ){
               Icon(painter = painterResource(id = R.drawable.r_share),
                   contentDescription = "Share")
               Text(text = "Share", fontSize = 20.sp, color = Color.White)
           }
           Row (
               modifier=modifier.padding(end = 16.dp,top = 16.dp)
           ){
               Icon(painter = painterResource(id = R.drawable.r_help),
                   contentDescription = "Help")
               Text(text = "Help", fontSize = 20.sp, color = Color.White)
           }
       }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun DrawerItem(
    selected:Boolean,
    item:Screen.DrawerScreen,
    onDrawerItemClicked:()->Unit
)
{
    val background=if(selected) Color.Gray else Color.White
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }
    )
    {
        Icon(painter = painterResource(id = item.icon), contentDescription =item.dTitle ,Modifier.padding(end=8.dp,top=4.dp))
        Text(text = item.dTitle, style = MaterialTheme.typography.h5)
    }
}
//This is our Graph
@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd:PaddingValues){

    NavHost(navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route, modifier = Modifier.padding(pd) ){

        composable(Screen.BottomScreen.Home.route)
        {
           Home()
        }
        composable(Screen.BottomScreen.Browse.route)
        {
           Browse()
        }
        composable(Screen.BottomScreen.Library.route)
        {
            Library()
        }
        composable(Screen.DrawerScreen.Account.route){
            AccountView()
        }
        composable(Screen.DrawerScreen.Subscription.route)
        {
            SubscriptionView()
        }
    }
}