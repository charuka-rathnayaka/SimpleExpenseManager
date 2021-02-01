package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.NewAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.NewTransactionDAO;

public class NewExpenseManager extends ExpenseManager{

    private transient Context mContext;

    public NewExpenseManager(Context context) {
        this.mContext=context;
        setup();
    }



    @Override
    public void setup(){
        SQLiteDatabase database=new database(mContext).getWritableDatabase();

        TransactionDAO inMemoryTransactionDAO = new NewTransactionDAO(database);
        setTransactionsDAO(inMemoryTransactionDAO);

        AccountDAO inMemoryAccountDAO = new NewAccountDAO(database);
        setAccountsDAO(inMemoryAccountDAO);
    }
}
