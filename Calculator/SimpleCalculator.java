import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class SimpleCalculator {
    private static final String DOUBLE_OR_NUMBER_REGEX = "([-]?\\d+[.]\\d*)|(\\d+)|(-\\d+)";
    
    private JFrame window;
    private JTextField inputScreen;
    private char selectedOperator = ' ';
    private boolean go = true;
    private boolean addToDisplay = true;
    private double typedValue = 0;

    public SimpleCalculator() {
        window = new JFrame("Calculator");
        window.setSize(410, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);

        initComponents();
        window.setVisible(true);
    }

    private void initComponents() {
        // Input screen
        inputScreen = new JTextField("0");
        inputScreen.setBounds(20, 60, 350, 70);
        inputScreen.setEditable(false);
        inputScreen.setBackground(Color.WHITE);
        inputScreen.setFont(new Font("Arial", Font.PLAIN, 33));
        window.add(inputScreen);

        // Button positions
        int[] columns = {20, 110, 200, 290};
        int[] rows = {160, 240, 320, 400, 480};

        // Row 1
        createButton("C", columns[0], rows[0], e -> {
            inputScreen.setText("0");
            selectedOperator = ' ';
            typedValue = 0;
        });
        
        createButton("←", columns[1], rows[0], e -> {
            String str = inputScreen.getText();
            if (str.length() > 1) {
                inputScreen.setText(str.substring(0, str.length() - 1));
            } else {
                inputScreen.setText("0");
            }
        });
        
        createButton("%", columns[2], rows[0], e -> performOperation('%'));
        createButton("/", columns[3], rows[0], e -> performOperation('/'));

        // Row 2
        createButton("7", columns[0], rows[1], e -> addNumber("7"));
        createButton("8", columns[1], rows[1], e -> addNumber("8"));
        createButton("9", columns[2], rows[1], e -> addNumber("9"));
        createButton("*", columns[3], rows[1], e -> performOperation('*'));

        // Row 3
        createButton("4", columns[0], rows[2], e -> addNumber("4"));
        createButton("5", columns[1], rows[2], e -> addNumber("5"));
        createButton("6", columns[2], rows[2], e -> addNumber("6"));
        createButton("-", columns[3], rows[2], e -> performOperation('-'));

        // Row 4
        createButton("1", columns[0], rows[3], e -> addNumber("1"));
        createButton("2", columns[1], rows[3], e -> addNumber("2"));
        createButton("3", columns[2], rows[3], e -> addNumber("3"));
        createButton("+", columns[3], rows[3], e -> performOperation('+'));

        // Row 5
        createButton(".", columns[0], rows[4], e -> {
            if (addToDisplay && !inputScreen.getText().contains(".")) {
                inputScreen.setText(inputScreen.getText() + ".");
            } else if (!addToDisplay) {
                inputScreen.setText("0.");
                addToDisplay = true;
            }
            go = true;
        });
        
        createButton("0", columns[1], rows[4], e -> addNumber("0"));
        
        JButton btnEqual = createButton("=", columns[2], rows[4], e -> {
            if (!Pattern.matches(DOUBLE_OR_NUMBER_REGEX, inputScreen.getText()) || !go)
                return;
            
            typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
            displayResult();
            selectedOperator = '=';
            addToDisplay = false;
        });
        btnEqual.setSize(170, 70); // Make = button wider
    }

    private JButton createButton(String text, int x, int y, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 80, 70);
        btn.setFont(new Font("Arial", Font.PLAIN, 28));
        btn.setFocusable(false);
        btn.addActionListener(action);
        window.add(btn);
        return btn;
    }

    private void addNumber(String number) {
        if (addToDisplay) {
            if (inputScreen.getText().equals("0")) {
                inputScreen.setText(number);
            } else {
                inputScreen.setText(inputScreen.getText() + number);
            }
        } else {
            inputScreen.setText(number);
            addToDisplay = true;
        }
        go = true;
    }

    private void performOperation(char operator) {
        if (!Pattern.matches(DOUBLE_OR_NUMBER_REGEX, inputScreen.getText()))
            return;

        if (go) {
            typedValue = calculate(typedValue, Double.parseDouble(inputScreen.getText()), selectedOperator);
            displayResult();
            selectedOperator = operator;
            go = false;
            addToDisplay = false;
        } else {
            selectedOperator = operator;
        }
    }

    private double calculate(double firstNumber, double secondNumber, char operator) {
        switch (operator) {
            case '+': return firstNumber + secondNumber;
            case '-': return firstNumber - secondNumber;
            case '*': return firstNumber * secondNumber;
            case '/': return firstNumber / secondNumber;
            case '%': return firstNumber % secondNumber;
            default: return secondNumber;
        }
    }

    private void displayResult() {
        if (typedValue == (int) typedValue) {
            inputScreen.setText(String.valueOf((int) typedValue));
        } else {
            inputScreen.setText(String.valueOf(typedValue));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleCalculator());
    }
}