package com.alexey.homeactivitymodel;


        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.ContextCompat;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText incomeInput;
    private EditText expenseNameInput;
    private EditText expenseInput;
    private Button balanceButton;
    private TableLayout tableLayout;
    private ListView listView;
    private double balance;
    private JSONObject data;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews() {
        incomeInput = findViewById(R.id.incomeInput);
        expenseNameInput = findViewById(R.id.expenseNameInput);
        expenseInput = findViewById(R.id.expenseInput);
        balanceButton = findViewById(R.id.balance_btn);
        tableLayout = findViewById(R.id.tableLayout);
        listView = findViewById(R.id.listView);
        balance = 0;
        data = new JSONObject();
        databaseHelper = new DatabaseHelper(this);
    }
    private void calculateBalance(double income, double expense) {
        balance += income - expense;
        balanceButton.setText("Баланс: " + balance);
    }
    private void calculateBalance() {
        String incomeString = incomeInput.getText().toString();
        double income = incomeString.isEmpty() ? 0 : Double.parseDouble(incomeString);
        String expenseName = expenseNameInput.getText().toString();
        String expenseString = expenseInput.getText().toString();
        double expense = expenseString.isEmpty() ? 0 : Double.parseDouble(expenseString);
        balance += income - expense;
        // Обновляем баланс на кнопке
        balanceButton.setText("Баланс: " + balance);
        // Сохраняем данные о транзакции
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        try {
            JSONObject entry = new JSONObject();
            entry.put("date", currentDateandTime);
            entry.put("income", income);
            entry.put("expenseName", expenseName);
            entry.put("expense", expense);
            data.put(currentDateandTime, entry);
            saveData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Очищаем поля ввода
        incomeInput.setText("");
        expenseNameInput.setText("");
        expenseInput.setText("");
    }
    private void saveData(JSONObject data) {
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("data", data.toString());
        editor.apply();
    }
}