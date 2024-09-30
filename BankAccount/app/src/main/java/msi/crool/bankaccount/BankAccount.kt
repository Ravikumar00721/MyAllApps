package msi.crool.bankaccount

class BankAccount(var accountHolder:String,var balance:Double) {
    private val transactionHistory= mutableListOf<String>()
    fun deposit(amount:Double)
    {
       balance+=amount
       transactionHistory.add("$amount is Added")
    }
    fun withDraw(amount: Double)
    {
       if(amount<=balance)
       {
           balance-=amount
           transactionHistory.add("$amount is Deducted from your Account")
       }
        else
       {
           println("Not Much Balance")
       }
    }
    fun showTransactionHistory()
    {
       println("This transaction is done by $accountHolder")
       for (i in transactionHistory)
       {
           println(i)
       }
        println("Balance in his Account $balance")
    }

}