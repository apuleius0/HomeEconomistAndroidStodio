//package com.alexey.homeactivitymodel;
//
//package com.alexey.homeactivitymodel;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.GridLayout;
//import android.widget.LinearLayout;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//
//public class MainActivity extends AppCompatActivity {
//
//    private EditText incomeInput;
//    private EditText expenseNameInput;
//    private EditText expenseInput;
//    private TextView balanceButton;
//    private TableLayout tableLayout;
//    private double balance;
//    private JSONObject data;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        LinearLayout mainLayout = new LinearLayout(this);
//        mainLayout.setOrientation(LinearLayout.VERTICAL);
//
//        balance = 0;
//
//        // Поля для ввода данных
//        LinearLayout incomeLayout = new LinearLayout(this);
//        incomeLayout.setOrientation(LinearLayout.HORIZONTAL);
//        TextView incomeLabel = new TextView(this);
//        incomeLabel.setText("Доход: ");
//        incomeLayout.addView(incomeLabel);
//        incomeInput = new EditText(this);
//        incomeLayout.addView(incomeInput);
//        mainLayout.addView(incomeLayout);
//
//        LinearLayout expenseNameLayout = new LinearLayout(this);
//        expenseNameLayout.setOrientation(LinearLayout.HORIZONTAL);
//        TextView expenseNameLabel = new TextView(this);
//        expenseNameLabel.setText("Статья расхода: ");
//        expenseNameLayout.addView(expenseNameLabel);
//        expenseNameInput = new EditText(this);
//        expenseNameLayout.addView(expenseNameInput);
//        mainLayout.addView(expenseNameLayout);
//
//        LinearLayout expenseLayout = new LinearLayout(this);
//        expenseLayout.setOrientation(LinearLayout.HORIZONTAL);
//        TextView expenseLabel = new TextView(this);
//        expenseLabel.setText("Расход: ");
//        expenseLayout.addView(expenseLabel);
//        expenseInput = new EditText(this);
//        expenseLayout.addView(expenseInput);
//        mainLayout.addView(expenseLayout);
//
//        // Кнопка для расчета баланса
//        balanceButton = new Button(this);
//        balanceButton.setText("Баланс: " + balance);
//        balanceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calculateBalance();
//            }
//        });
//        Button exitButton = new Button(this);
//        exitButton.setText("Выход");
//        exitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        mainLayout.addView(exitButton);
//
//
//        // Таблица для отображения данных
//        tableLayout = new TableLayout(this);
//        tableLayout.setColumnStretchable(0, true);
//        tableLayout.setColumnStretchable(1, true);
//        tableLayout.setColumnStretchable(2, true);
//        tableLayout.setColumnStretchable(3, true);
//        TableRow headerRow = new TableRow(this);
//        TextView dateHeader = new TextView(this);
//        dateHeader.setText("Дата");
//        headerRow.addView(dateHeader);
//        TextView incomeHeader = new TextView(this);
//        incomeHeader.setText("Доход");
//        headerRow.addView(incomeHeader);
//        TextView expenseNameHeader = new TextView(this);
//        expenseNameHeader.setText("Статья расхода");
//        headerRow.addView(expenseNameHeader);
//        TextView expenseHeader = new TextView(this);
//        expenseHeader.setText("Расход");
//        headerRow.addView(expenseHeader);
//        tableLayout.addView(headerRow);
//
//        // Загрузка сохраненных данных в таблицу
//        data = loadData();
//        Iterator<String> keys = data.keys();
//        while (keys.hasNext()) {
//            String key = keys.next();
//            try {
//                JSONObject entry = data.getJSONObject(key);
//                TableRow row = new TableRow(this);
//                TextView date = new TextView(this);
//                date.setText(entry.getString("date"));
//                row.addView(date);
//                TextView income =
//                        new TextView(this);
//                income.setText(entry.getString("income"));
//                row.addView(income);
//                TextView expenseName = new TextView(this);
//                expenseName.setText(entry.getString("expenseName"));
//                row.addView(expenseName);
//                TextView expense = new TextView(this);
//                expense.setText(entry.getString("expense"));
//                row.addView(expense);
//                tableLayout.addView(row);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        mainLayout.addView(balanceButton);
//        mainLayout.addView(tableLayout);
//        setContentView(mainLayout);
//    }
//
//    private JSONObject loadData() {
//        String jsonString = getSharedPreferences("data", MODE_PRIVATE).getString("data", "{}");
//        try {
//            return new JSONObject(jsonString);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return new JSONObject();
//        }
//    }
//
//    private void saveData(JSONObject data) {
//        getSharedPreferences("data", MODE_PRIVATE).edit().putString("data", data.toString()).apply();
//    }
//
//    private void calculateBalance() {
//        String incomeString = incomeInput.getText().toString();
//        double income = incomeString.isEmpty() ? 0 : Double.parseDouble(incomeString);
//
//        String expenseName = expenseNameInput.getText().toString();
//
//        String expenseString = expenseInput.getText().toString();
//        double expense = expenseString.isEmpty() ? 0 : Double.parseDouble(expenseString);
//
//        balance += income - expense;
//
//        // Обновляем баланс на кнопке
////        balanceButton.setText("Баланс: " + balance);
//        balanceButton.setText("Баланс: " + String.valueOf(balance));
//
//        // Сохраняем данные о транзакции
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String currentDateandTime = sdf.format(new Date());
//
//        try {
//            JSONObject entry = new JSONObject();
//            entry.put("date", currentDateandTime);
//            entry.put("income", income);
//            entry.put("expenseName", expenseName);
//            entry.put("expense", expense);
//            data.put(currentDateandTime, entry);
//            saveData(data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Добавляем запись в таблицу
//        TableRow row = new TableRow(this);
//        TextView date = new TextView(this);
//        date.setText(currentDateandTime);
//        row.addView(date);
//        TextView incomeView = new TextView(this);
//        incomeView.setText(String.valueOf(income));
//        row.addView(incomeView);
//        TextView expenseNameView = new TextView(this);
//        expenseNameView.setText(expenseName);
//        row.addView(expenseNameView);
//        TextView expenseView = new TextView(this);
//        expenseView.setText(String.valueOf(expense));
//        row.addView(expenseView);
//        tableLayout.addView(row);
//
//        // Очищаем поля ввода
//        incomeInput.setText("");
//        expenseNameInput.setText("");
//        expenseInput.setText("");
//    }
//
//}
