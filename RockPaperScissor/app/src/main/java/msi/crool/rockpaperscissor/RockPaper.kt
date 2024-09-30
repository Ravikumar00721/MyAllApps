package msi.crool.rockpaperscissor

fun main()
{
   var a=1
   while (a==1)
   {
       var computerChoice=""
       var playerChoice=""
       println("Enter Your Choice :")
       playerChoice= readln()
       var randomnum=(1..3).random()
       when (randomnum) {
           1 -> {
               computerChoice="Rock"
           }
           2 -> {
               computerChoice="Paper"
           }
           3 -> {
               computerChoice="Scissor"
           }
       }

       val winner=when{
           computerChoice==playerChoice->"Tie"
           playerChoice=="Rock" && computerChoice=="Scissor"->"Player"
           playerChoice=="Paper" && computerChoice=="Rock"->"Player"
           playerChoice=="Scissor" && computerChoice=="Paper"->"Player"
           else->"Computer"
       }
//    when(winner)
//    {
//        "Player"->{
//            println("Player is winner")
//        }
//        "Computer"->{
//            println("computer is winner")
//        }
//        "Tie"->{
//            println("Match is Tie")
//        }
//    }
       if(winner=="Tie")
       {
           println("Match is Tie")
       }
       else
       {
           println("$winner is Won")
       }
       println("If you not want to play again than enter 0")
       a= readln().toInt()
   }
}