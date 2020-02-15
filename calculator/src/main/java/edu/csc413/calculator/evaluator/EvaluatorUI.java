package edu.csc413.calculator.evaluator;

import edu.csc413.calculator.exceptions.InvalidTokenException;
import edu.csc413.calculator.operators.Operator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.StringTokenizer;

public class EvaluatorUI extends JFrame implements ActionListener {

    private TextField expressionTextField = new TextField();
    private Panel buttonPanel = new Panel();

    // total of 20 buttons on the calculator,
    // numbered from left to right, top to bottom
    // bText[] array contains the text for corresponding buttons
    private static String express = "";
    private static final String[] buttonText = {
        "7", "8", "9", "+", "4", "5", "6", "- ", "1", "2", "3",
        "*", "0", "^", "=", "/", "(", ")", "C", "CE"
    };
    /**
     * C  is for clear, clears entire expression
     * CE is for clear expression, clears last entry up until the last operator.
     */
    private Button[] buttons = new Button[buttonText.length];
    private Stack<Operand> operandStack;
    private Stack<Operator> operatorStack;
    private StringTokenizer expressionTokenizer;
    private final String delimiters = " +/*-^()";

    public static void main(String argv[]) {
        new EvaluatorUI();
    }

    public EvaluatorUI(){

        operandStack = new Stack<>();
        operatorStack = new Stack<>();
        setLayout(new BorderLayout());
        this.expressionTextField.setPreferredSize(new Dimension(600, 50));
        this.expressionTextField.setFont(new Font("Courier", Font.BOLD, 28));

        add(expressionTextField, BorderLayout.NORTH);
        expressionTextField.setEditable(false);

        add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.setLayout(new GridLayout(5, 4));

        //create 20 buttons with corresponding text in bText[] array
        Button tempButtonReference;
        for (int i = 0; i < EvaluatorUI.buttonText.length; i++) {
            tempButtonReference = new Button(buttonText[i]);
            tempButtonReference.setFont(new Font("Courier", Font.BOLD, 28));
            buttons[i] = tempButtonReference;
        }

        //add buttons to button panel
        for (int i = 0; i < EvaluatorUI.buttonText.length; i++) {
            buttonPanel.add(buttons[i]);
        }

        //set up buttons to listen for mouse input
        for (int i = 0; i < EvaluatorUI.buttonText.length; i++) {
            buttons[i].addActionListener(this);
        }

        setTitle("Calculator");
        setSize(400, 400);
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private String getExpress()
    {
        return express;
    }

    private void addtoExpress(String append)
    {
        express = express+append;
    }

    private void clearExpress()
    {
        express = "";
    }

    private void clearLastOperand()
    {
        int lastIndex = express.length()-1;
        while(express.charAt(lastIndex)>= '0' && express.charAt(lastIndex)<= '9')
            lastIndex--;
        express = express.substring(0,lastIndex+1);
    }

    public int evaluateExpression(String expression ) throws InvalidTokenException {
        String expressionToken;

        // The 3rd argument is true to indicate that the delimiters should be used
        // as tokens, too. But, we'll need to remember to filter out spaces.
        this.expressionTokenizer = new StringTokenizer( expression, this.delimiters, true );

        // initialize operator stack - necessary with operator priority schema
        // the priority of any operator in the operator stack other than
        // the usual mathematical operators - "+-*/" - should be less than the priority
        // of the usual operators


        Operator newOperator = null;
        while ( this.expressionTokenizer.hasMoreTokens() ) {
            // filter out spaces
            if ( !( expressionToken = this.expressionTokenizer.nextToken() ).equals( " " )) {
                // check if token is an operand
                if ( Operand.check( expressionToken )) {
                    operandStack.push( new Operand( expressionToken ));
                }
                else if(")".equals(expressionToken))
                {
                    while(operatorStack.peek().priority()!=0)
                    {
                        Operator operatorFromStack = operatorStack.pop();
                        Operand operandTwo = operandStack.pop();
                        Operand operandOne = operandStack.pop();
                        Operand result = operatorFromStack.execute(operandOne, operandTwo);
                        operandStack.push(result);
                    }
                    operatorStack.pop();
                }
                else if ( ! Operator.check( expressionToken )) {
                    throw new InvalidTokenException(expressionToken);
                }
                else if(operatorStack.isEmpty() || Operator.getOperator(expressionToken).priority() > operatorStack.peek().priority() || Operator.getOperator(expressionToken).priority() == 0)
                {
                    newOperator = Operator.getOperator(expressionToken);
                    operatorStack.push(newOperator);
                }
                else
                {
                    // TODO Operator is abstract - these two lines will need to be fixed:
                    // The Operator class should contain an instance of a HashMap,
                    // and values will be instances of the Operators.  See Operator class
                    // skeleton for an example.
                    //Operator newOperator = new Operator();
                    //Operator newOperator = Operator.getOperator(" ");
                    newOperator = Operator.getOperator(expressionToken);
                    while (!operatorStack.isEmpty() && !operandStack.isEmpty() && operatorStack.peek().priority() >= newOperator.priority()) {
                        // note that when we eval the expression 1 - 2 we will
                        // push the 1 then the 2 and then do the subtraction operation
                        // This means that the first number to be popped is the
                        // second operand, not the first operand - see the following code
                        Operator operatorFromStack = operatorStack.pop();
                        Operand operandTwo = operandStack.pop();
                        Operand operandOne = operandStack.pop();
                        Operand result = operatorFromStack.execute(operandOne, operandTwo);
                        operandStack.push(result);
                    }
                    operatorStack.push(newOperator);
                }
            }
        }
        while(!operatorStack.isEmpty() && !operandStack.isEmpty()) {
            Operator operatorFromStack = operatorStack.pop();
            Operand operandTwo = operandStack.pop();
            Operand operandOne = operandStack.pop();
            Operand result = operatorFromStack.execute(operandOne, operandTwo);
            operandStack.push(result);
        }
        return operandStack.peek().getValue();

        // Control gets here when we've picked up all of the tokens; you must add
        // code to complete the evaluation - consider how the code given here
        // will evaluate the expression 1+2*3
        // When we have no more tokens to scan, the operand stack will contain 1 2
        // and the operator stack will have + * with 2 and * on the top;
        // In order to complete the evaluation we must empty the stacks,
        // that is, we should keep evaluating the operator stack until it is empty;
        // Suggestion: create a method that processes the operator stack until empty.
//comment out the return 0 when done
        //return 0;
    }
    /**
     * This function is triggered anytime a button is pressed
     * on our Calculator GUI.
     * @param actionEventObject Event object generated when a
     *                    button is pressed.
     */
    public void actionPerformed(ActionEvent actionEventObject) {
        //reference evaluator, don't revise it
        //switch(keyTyped){
        //case "9":
        //System.out.println(actionEventObject.getActionCommand());
        if(actionEventObject.getActionCommand().equals("C"))
            clearExpress();
        else if(actionEventObject.getActionCommand().equals("CE"))
            clearLastOperand();
        else if(actionEventObject.getActionCommand().equals("="))
        {
            try {
                express = Integer.toString(evaluateExpression(express));
            } catch (InvalidTokenException e) {
                e.printStackTrace();
            }
        }
        else
            addtoExpress(actionEventObject.getActionCommand());
        //System.out.println(express);
        expressionTextField.setText(express);



    }
}
