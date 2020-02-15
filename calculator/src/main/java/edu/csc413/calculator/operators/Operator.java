package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;
import javafx.scene.Parent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Operator {
    // The Operator class should contain an instance of a HashMap
    // This map will use keys as the tokens we're interested in,
    // and values will be instances of the Operators.
    // ALL subclasses of operator MUST be in their own file.
    //Make parentheses under operators class
    //if front parenthesis is found, push to stack, then iterate regularly until end parenthesis is found, then evaluate the operands, and pop the front parenthesis out of the stack
    private static HashMap<String, Operator> operators;
    static{
        operators = new HashMap<String, Operator>();
        operators.put("+", new AddOperator());
        operators.put("-", new SubtractOperator());
        operators.put("*", new MultiplyOperator());
        operators.put("/", new DivideOperator());
        operators.put("^", new PowerOperator());
        operators.put("(", new ParenthesisOperator());
    }
    // Example
    // Where does this declaration go? What should its access level be?
    // Class or instance variable? Is this the right declaration?
    // HashMap operators = new HashMap();
    // operators.put( "+", new AdditionOperator() );
    // operators.put( "-", new SubtractionOperator() );
    //token = "+"
    //if("+".equals(token))
    //return new AddOperator();
    //else if("-".equals(token))
    //return new
    //check to make sure there is only one of an operator (singleton opperator), using hashmap
    //mapping string tokens to already created operator objects
    //cannot use init method for hashmap, must fill hashmap automatically
    //must use static initializer for hashmap
    //look at static block in EvaluatorDriver
    /**
     * retrieve the priority of an Operator
     * @return priority of an Operator as an int
     */
    public abstract int priority();

    /**
     * Abstract method to execute an operator given two operands.
     * @param operandOne first operand of operator
     * @param operandTwo second operand of operator
     * @return an operand of the result of the operation.
     */
    public abstract Operand execute(Operand operandOne, Operand operandTwo);

    /**
     * used to retrieve an operator from our HashMap.
     * This will act as our publicly facing function,
     * granting access to the Operator HashMap.
     *
     * @param token key of the operator we want to retrieve
     * @return reference to a Operator instance.
     */
    public static Operator getOperator(String token)
    {
        Operator resOp;
        resOp = "+".equals(token)?(Operator)operators.get("+")
                :"-".equals(token)?(Operator)operators.get("-")
                :"*".equals(token)?(Operator) operators.get("*")
                :"/".equals(token)?(Operator) operators.get("/")
                :"^".equals(token)?(Operator) operators.get("^")
                :"(".equals(token)?(Operator) operators.get("(")
                :null;
        return resOp;
    }

    /**
     * determines if a given token is a valid operator.
     * please do your best to avoid static checks
     * for example token.equals("+") and so on.
     * Think about what happens if we add more operators.
     */
    public static boolean check(String token) {
        return operators.containsKey(token);
    }

}