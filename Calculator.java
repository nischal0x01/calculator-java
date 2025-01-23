import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;
    private boolean errorState = false;
    private DecimalFormat df = new DecimalFormat("#.###########");

    public Calculator() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, use default look and feel
        }

        setTitle("Advanced Calculator");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(40, 40, 40));

        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(20, 20, 20));
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBackground(new Color(40, 40, 40));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttons = {
            "C", "±", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "DEL", "="
        };

        for (String text : buttons) {
            JButton button = createStyledButton(text);
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        setLayout(new BorderLayout(10, 10));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 24));
        button.setBackground(getButtonColor(text));
        button.setForeground(getButtonTextColor(text));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(getButtonColor(text));
            }
        });

        return button;
    }

    private Color getButtonColor(String text) {
        if ("/*-+".contains(text)) return new Color(255, 160, 0);
        if ("C".equals(text)) return new Color(220, 50, 50);
        if ("=".equals(text)) return new Color(50, 200, 50);
        if ("±%DEL".contains(text)) return new Color(100, 100, 100);
        return new Color(70, 70, 70);
    }

    private Color getButtonTextColor(String text) {
        return Color.WHITE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (errorState && !"C".equals(command)) return;

        try {
            switch (command) {
                case "0": case "1": case "2": case "3": case "4": 
                case "5": case "6": case "7": case "8": case "9":
                    handleNumberInput(command);
                    break;
                case "+": case "-": case "*": case "/":
                    handleOperator(command);
                    break;
                case "=":
                    calculateResult();
                    break;
                case "C":
                    clearCalculator();
                    break;
                case "DEL":
                    deleteLastCharacter();
                    break;
                case "±":
                    toggleSign();
                    break;
                case "%":
                    calculatePercentage();
                    break;
                case ".":
                    addDecimalPoint();
                    break;
            }
        } catch (Exception ex) {
            display.setText("ERROR");
            errorState = true;
        }
    }

    private void handleNumberInput(String number) {
        if (startNewNumber) {
            display.setText(number);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + number);
        }
    }

    private void handleOperator(String op) {
        firstNumber = Double.parseDouble(display.getText());
        operator = op;
        startNewNumber = true;
    }

    private void calculateResult() {
        secondNumber = Double.parseDouble(display.getText());
        double result = calculate(firstNumber, secondNumber, operator);
        display.setText(df.format(result));
        startNewNumber = true;
    }

    private void clearCalculator() {
        display.setText("0");
        firstNumber = 0;
        secondNumber = 0;
        operator = "";
        startNewNumber = true;
        errorState = false;
    }

    private void deleteLastCharacter() {
        String current = display.getText();
        if (current.length() > 1) {
            display.setText(current.substring(0, current.length() - 1));
        } else {
            display.setText("0");
        }
    }

    private void toggleSign() {
        double value = Double.parseDouble(display.getText());
        display.setText(df.format(-value));
    }

    private void calculatePercentage() {
        double value = Double.parseDouble(display.getText());
        display.setText(df.format(value / 100));
    }

    private void addDecimalPoint() {
        if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private double calculate(double num1, double num2, String op) {
        switch (op) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "*": return num1 * num2;
            case "/": 
                if (num2 == 0) throw new ArithmeticException("Divide by zero");
                return num1 / num2;
            default: return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}