package com.alexey.homeactivitymodel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthActivity extends AppCompatActivity {
    private TextView incomeTextView;
    private TextView expenseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        incomeTextView = findViewById(R.id.income_text_view);
        expenseTextView = findViewById(R.id.expense_text_view);

        Intent intent = getIntent();
        String year = intent.getStringExtra("year");
        String month = intent.getStringExtra("month");

        List<Transaction> transactions = loadTransactions();

        double totalIncome = calculateTotalIncome(transactions, year, month);
        double totalExpense = calculateTotalExpense(transactions, year, month);

        incomeTextView.setText(String.format(Locale.US, "Income: %.2f", totalIncome));
        expenseTextView.setText(String.format(Locale.US, "Expense: %.2f", totalExpense));
    }

    private List<Transaction> loadTransactions() {
        // Загрузите список транзакций из файла или базы данных
        // Здесь просто возвращаем пустой список
        return new ArrayList<>();
    }

    private double calculateTotalIncome(List<Transaction> transactions, String year, String month) {
        // Вычислите общий доход для выбранного года и месяца
        double totalIncome = 0;
        for (Transaction transaction : transactions) {
            if (isTransactionInMonth(transaction, year, month)) {
                totalIncome += transaction.getIncome();
            }
        }
        return totalIncome;
    }

    private double calculateTotalExpense(List<Transaction> transactions, String year, String month) {
        // Вычислите общие расходы для выбранного года и месяца
        double totalExpense = 0;
        for (Transaction transaction : transactions) {
            if (isTransactionInMonth(transaction, year, month)) {
                totalExpense += transaction.getExpense();
            }
        }
        return totalExpense;
    }

    private boolean isTransactionInMonth(Transaction transaction, String year, String month) {
        // Верните true, если транзакция была совершена в выбранный год и месяц
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getDate());
        int transactionYear = calendar.get(Calendar.YEAR);
        int transactionMonth = calendar.get(Calendar.MONTH) + 1;  // months are 0-indexed in Calendar

        return transactionYear == Integer.parseInt(year) && transactionMonth == Integer.parseInt(month);
    }
}
