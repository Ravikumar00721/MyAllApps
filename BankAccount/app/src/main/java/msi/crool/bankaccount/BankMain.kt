package msi.crool.bankaccount

fun main()
{
     val denisesBankAccount=BankAccount("Denis", balance = 10000.12)
     denisesBankAccount.deposit(1000.1)
     denisesBankAccount.showTransactionHistory()
     denisesBankAccount.withDraw(50000.5)
}