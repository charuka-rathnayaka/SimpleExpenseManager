package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class NewTransactionDAO implements TransactionDAO {

    private transient SQLiteDatabase database;
    public NewTransactionDAO(SQLiteDatabase database) {
        this.database=database;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql="INSERT INTO "+ "TransactionTable"+"("+ "AccountNumber"+","+ "TransactionType"+","+
                "TransactionDate"+","+ "TransactionAmount"+") VALUES(?,?,?,?);";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.bindString(1,accountNo);
        statement.bindString(2,expenseType.name());
        statement.bindString(3,date.toString());
        statement.bindDouble(4,amount);

        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor curs = database.query(
                "TransactionTable",
                null,
                null,
                null,
                null,
                null,
                null);

        List<Transaction> transactionList=new ArrayList<>();
        while(curs.moveToNext()){
            String AccountNumber=curs.getString(curs.getColumnIndex("AccountNumber"));
            double Amount=curs.getDouble(curs.getColumnIndex("TransactionAmount"));
            String Type=curs.getString(curs.getColumnIndex("TransactionType"));

            String Date=curs.getString(curs.getColumnIndex("TransactionDate"));
            Transaction transaction=new Transaction(new Date(Date),AccountNumber, ExpenseType.valueOf(Type),Amount);
            transactionList.add(transaction);
        }
        curs.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        String sql="SELECT * FROM "+ "TransactionTable"+" LIMIT "+limit;
        Cursor curs=database.rawQuery(sql,null);
        List<Transaction> transactionList=new ArrayList<>();
        while(curs.moveToNext()){
            String AccountNumber=curs.getString(curs.getColumnIndex("AccountNumber"));
            double Amount=curs.getDouble(curs.getColumnIndex("TransactionAmount"));
            String Type=curs.getString(curs.getColumnIndex("TransactionType"));

            String Date=curs.getString(curs.getColumnIndex("TransactionDate"));
            Transaction transaction=new Transaction(new Date(Date),AccountNumber, ExpenseType.valueOf(Type),Amount);
            transactionList.add(transaction);
        }
        curs.close();
        return  transactionList;
    }
}

