package com.example.calculatorr;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorr.R;

public class MainActivity extends AppCompatActivity {

    TextView resultTextView;
    TextView historyTextView;
    Boolean isEqualPressed = false;

    Button clearButton;

    String savedNumber = "";

    String savedOperator = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.results_tv);
        historyTextView = findViewById(R.id.history_tv);
        clearButton = findViewById(R.id.btn_clear);

    }


    public void onNumberClick(View view) {
        historyTextView.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);

        String numberClickedText = ((Button) view).getText().toString();

        //after pressing equal, don't add to history and start with this clicked number
        if (isEqualPressed && savedOperator.equals("")) {
            historyTextView.setText("");
            resultTextView.setText("");
            isEqualPressed = false;
        }

        //don't add multiple .
        String history = historyTextView.getText().toString();
        if (!history.isEmpty()) {
            Character lastChar = history.charAt(history.length() - 1);
            if (lastChar.toString().equals(".") && ".".equals(numberClickedText))
                return;
            if (lastChar.toString().equals("√"))
                savedNumber = numberClickedText;
        }


        resultTextView.append(numberClickedText);
        historyTextView.append(numberClickedText);


    }

    public void onOperatorClick(View view) {
        historyTextView.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);
        //don't start with an operator
        String history = historyTextView.getText().toString();
        if (history.isEmpty()) {
            Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show();
            return;
        }

        //don't allow multiple clicks on same operator
        Character operatorClickedText = ((Button) view).getText().toString().charAt(0);
        Character lastChar = history.charAt(history.length() - 1);
        if (lastChar == operatorClickedText) {
            return;
        }

        //replace the last clicked operator with this one
        if (lastChar != operatorClickedText && isOperator(lastChar)) {
            String replacedHistory = replaceLastCharacter(history, operatorClickedText.toString());
            historyTextView.setText(replacedHistory);
            savedOperator = operatorClickedText.toString();
            return;
        }


        if (savedOperator.isEmpty()) {
            savedNumber = resultTextView.getText().toString();
            savedOperator = operatorClickedText.toString();
            resultTextView.setText("");
            historyTextView.append(savedOperator);

        } else {
            String rhs = resultTextView.getText().toString();
            //don't allow divide by zero
            if (savedOperator.equals("÷") && rhs.equals("0")) {
                Toast.makeText(this, "Can't divide by zero.", Toast.LENGTH_SHORT).show();
                return;
            }
            //don't allow divide by .
            if (savedOperator.equals("÷") && rhs.equals(".")) {
                Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show();
                return;
            }

            savedNumber = calculate(savedNumber, savedOperator, rhs);
            savedOperator = operatorClickedText.toString();
            resultTextView.setText("");
            historyTextView.append(savedOperator);

        }


    }


    private boolean isOperator(char ch) {
        return (ch == '+' || ch == '-' || ch == '÷' || ch == '×');
    }

    public void onEqualClick(View view) {

        if (savedNumber.equals(""))
            return;

        String rhs = resultTextView.getText().toString();

        if (rhs.isEmpty() || rhs.equals("^2") || rhs.equals("√")) {
            Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show();
            return;
        }

        //don't allow divide by zero
        if (savedOperator.equals("÷") && rhs.equals("0")) {
            Toast.makeText(this, "Can't divide by zero.", Toast.LENGTH_SHORT).show();
            return;
        }
        //don't allow divide by .
        if (savedOperator.equals("÷") && rhs.equals(".")) {
            Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (savedOperator.equals("") && rhs.endsWith("^2")) {
            Double res = Math.pow(Double.parseDouble(savedNumber), 2);
            savedNumber = res.toString();
        } else if (savedOperator.equals("") && rhs.matches("√" + "[0-9]$")) {
            Double res = Math.sqrt(Double.parseDouble(savedNumber));
            savedNumber = res.toString();
        } else if (!savedOperator.isEmpty() && !savedNumber.isEmpty()) {
            savedNumber = calculate(savedNumber, savedOperator, rhs);
        } else {
            return;
        }

        resultTextView.setText(savedNumber);
        savedNumber = "";
        savedOperator = "";
        isEqualPressed = true;
        historyTextView.setVisibility(View.GONE);
        resultTextView.setVisibility(View.VISIBLE);
        resultTextView.setTextSize(32);
    }

    private String calculate(String lhs, String operator, String rhs) {
        Double result = 0.0;
        Double num1 = Double.parseDouble(lhs);
        Double num2 = Double.parseDouble(rhs);

        switch (operator) {
            case "+": {
                result = num1 + num2;
                break;
            }
            case "-": {
                result = num1 - num2;
                break;

            }
            case "×": {
                result = num1 * num2;
                break;
            }
            case "÷": {
                result = num1 / num2;
                break;
            }

            default:


        }
        return result.toString();
    }

    public void onClearClick(View view) {
        savedNumber = "";
        savedOperator = "";
        historyTextView.setText("");
        resultTextView.setText("");
    }

    private String replaceLastCharacter(String string, String replacement) {
        Character lastChar = string.charAt(string.length() - 1);
        int start = string.lastIndexOf(lastChar);
        String builder = string.substring(0, start) +
                replacement;
        return builder;
    }

    public void onBackspaceClicked(View view) {
        String history = historyTextView.getText().toString();
        String result = resultTextView.getText().toString();

        if (!history.isEmpty()) {
            String replacedHistory = replaceLastCharacter(history, "");
            String replacedResult = replaceLastCharacter(result, "");
            historyTextView.setText(replacedHistory);
            resultTextView.setText(replacedResult);


        }
    }

    public void onSquaredPowerClick(View view) {
        savedNumber = resultTextView.getText().toString();
        historyTextView.append("^2");
        resultTextView.append("^2");

    }

    public void onSquaredRootClick(View view) {
        historyTextView.append("√");
        resultTextView.append("√");
        }
    }


