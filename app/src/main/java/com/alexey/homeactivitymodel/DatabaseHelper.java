package com.alexey.homeactivitymodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Имя базы данных
    private static final String DATABASE_NAME = "expenses_db";

    // Версия базы данных
    private static final int DATABASE_VERSION = 1;

    // Имена таблиц и столбцов
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_INCOME = "income";
    private static final String COLUMN_EXPENSE_NAME = "expense_name";
    private static final String COLUMN_EXPENSE = "expense";

    // SQL-запросы для создания и удаления таблицы
    private static final String CREATE_TABLE_EXPENSES =
            "CREATE TABLE " + TABLE_EXPENSES + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_INCOME + " REAL,"
                    + COLUMN_EXPENSE_NAME + " TEXT,"
                    + COLUMN_EXPENSE + " REAL"
                    + ")";

    private static final String DROP_TABLE_EXPENSES = "DROP TABLE IF EXISTS " + TABLE_EXPENSES;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старую таблицу и создаем новую при обновлении базы данных
        db.execSQL(DROP_TABLE_EXPENSES);
        onCreate(db);
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, expense.getDate());
        values.put(COLUMN_INCOME, expense.getIncome());
        values.put(COLUMN_EXPENSE_NAME, expense.getExpenseName());
        values.put(COLUMN_EXPENSE, expense.getExpense());

        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_ID,
                COLUMN_DATE,
                COLUMN_INCOME,
                COLUMN_EXPENSE_NAME,
                COLUMN_EXPENSE
        };

        Cursor cursor = db.query(
                TABLE_EXPENSES,
                columns,
                null,
                null,
                null,
                null,
                COLUMN_DATE + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                expense.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                expense.setIncome(cursor.getDouble(cursor.getColumnIndex(COLUMN_INCOME)));
                expense.setExpenseName(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_NAME)));
                expense.setExpense(cursor.getDouble(cursor.getColumnIndex(COLUMN_EXPENSE)));

                expenses.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return expenses;
    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, expense.getDate());
        values.put(COLUMN_INCOME, expense.getIncome());
        values.put(COLUMN_EXPENSE_NAME, expense.getExpenseName());
        values.put(COLUMN_EXPENSE, expense.getExpense());

        db.update(TABLE_EXPENSES, values, COLUMN_ID + "=?", new String[]{String.valueOf(expense.getId())});

        db.close();
    }
}

