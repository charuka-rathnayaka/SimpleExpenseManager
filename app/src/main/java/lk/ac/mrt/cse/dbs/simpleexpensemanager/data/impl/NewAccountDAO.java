package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class NewAccountDAO implements AccountDAO {
    private transient SQLiteDatabase database;

    public NewAccountDAO(SQLiteDatabase database) {
        this.database=database;
    }

    @Override
    public List<String> getAccountNumbersList() {
        String[] projection = {
                "AccountNumber"
        };
        Cursor curs =  database.query("AccountTable",
                projection,
                null ,
                null ,
                null,
                null,
                null);

        List<String> accountNumberList = new ArrayList<>();

        while(curs.moveToNext()){
            String accountNo=curs.getString (curs.getColumnIndex ("AccountNumber"));
            accountNumberList.add(accountNo);
        }
        curs.close();
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor curs =  database.query("AccountTable",
                null,
                null ,
                null ,
                null,
                null,
                null);

        List<Account> accountList = new ArrayList<>();

        while(curs.moveToNext()){
            String AccountNumber=curs.getString (curs.getColumnIndex ("AccountNumber"));
            String Bank=curs.getString(curs.getColumnIndex("Bank"));
            String AccountHolder=curs.getString(curs.getColumnIndex("AccountHolder"));
            double balance=curs.getDouble(curs.getColumnIndex("Balance"));
            Account account=new Account(AccountNumber,Bank,AccountHolder,balance);
            accountList.add(account);
        }
        curs.close();
        return accountList;
    }

    @Override
    public Account getAccount(String AccountNumber) throws InvalidAccountException {
        String selection = "AccountNumber" + " = ?";
        String[] selectionArgs = {AccountNumber};

        Cursor curs = database.query("AccountTable",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if(curs.getCount()==0){
            String msg = "Account doesn't exists.";
            curs.close();
            throw new InvalidAccountException(msg);
        }
        else{
            curs.moveToFirst();
            String Bank=curs.getString(curs.getColumnIndex("Bank"));
            String AccountHolder=curs.getString(curs.getColumnIndex("AccountHolder"));
            double Balance=curs.getDouble(curs.getColumnIndex("Balance"));
            curs.close();
            return  new Account(AccountNumber,Bank,AccountHolder,Balance);
        }


    }

    @Override
    public void addAccount(Account account) {
        String sql="INSERT INTO AccountTable "+"("+ "AccountNumber"+","+ "Bank"+","+
                "AccountHolder"+","+ "Balance"+") VALUES(?,?,?,?);";
        SQLiteStatement statement=database.compileStatement(sql); //avoid sql injection
        statement.bindString(1,account.getAccountNo());
        statement.bindString(2,account.getBankName());
        statement.bindString(3,account.getAccountHolderName());
        statement.bindDouble(4,account.getBalance());
        try{
            statement.executeInsert();
        }
        catch (SQLiteConstraintException ex){
            Log.e("Error Here - ","Some Error Occured");
        }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql="DELETE FROM "+"AccountTable"+" WHERE "+ "AccountNumber"+" = ?;";
        SQLiteStatement statement=database.compileStatement(sql); //avoid sql injection
        statement.bindString(1,accountNo);
        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account=getAccount(accountNo);
        //Check the type of the transaction and do the relevant operation
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }

        String sql="UPDATE "+ "AccountTable"+" SET "+ "Balance"+" = ? WHERE "+
                "AccountNumber"+" = ? ;"  ;
        SQLiteStatement statement=database.compileStatement(sql); //avoid sql injection
        statement.bindDouble(1,account.getBalance());
        statement.bindString(2,accountNo);
        statement.executeUpdateDelete();
    }
}

