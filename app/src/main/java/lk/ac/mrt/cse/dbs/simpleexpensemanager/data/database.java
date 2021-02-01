package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class database extends SQLiteOpenHelper {
    public static final String DatabaseName = "180526RExpenseManager.db";
    public static final int DatabaseVersion = 1;
    public static final String AccountTable = "AccountTable";
    public static final String AccountNumber = "AccountNumber";
    public static final String Bank = "Bank";
    public static final String AccountHolder = "AccountHolder";
    public static final String AccountBalance = "Balance";

    public static final String TransactionTable = "TransactionTable";
    public static final String TransactionID = "TransactionID";
    public static final String TransactionType = "TransactionType";
    public static final String TransactionAmount = "TransactionAmount";
    public static final String TransactionDate = "TransactionDate";


    public database(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+AccountTable + " (" + AccountNumber + " varchar(30)  PRIMARY KEY, "
                + Bank + " varchar(50), "
                + AccountHolder + " varchar(100), "
                + AccountBalance + " float(10,2)"+
                ")"
        );
        db.execSQL("create table "+TransactionTable + " ("
                + TransactionID + " Integer  PRIMARY KEY AUTOINCREMENT, "
                + AccountNumber + " varchar(30), "
                + TransactionType + " varchar(20), "
                + TransactionAmount + " float(10,2), "
                + TransactionDate + " Date"+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AccountTable);
        db.execSQL("DROP TABLE IF EXISTS " + TransactionTable);
        onCreate(db);

    }
}
