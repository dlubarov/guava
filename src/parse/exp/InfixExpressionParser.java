package parse.exp;

import java.util.*;

import parse.*;
import util.StringUtils;
import a.exp.*;

public class InfixExpressionParser extends Parser<Expression> {
    public static final Parser<Expression>
            exponentiationParser = new InfixExpressionParser(
                    new String[] {"**"}, ChainParser.singleton, true),
            mulDivModParser = new InfixExpressionParser(
                    new String[] {"*", "/", "%"}, PrefixExpressionParser.singleton, false),
            plusMinusParser = new InfixExpressionParser(
                    new String[] {"+", "-"}, mulDivModParser, false),
            appendParser = new InfixExpressionParser(
                    new String[] {"+:"}, plusMinusParser, true),
            prependParser = new InfixExpressionParser(
                    new String[] {":+"}, appendParser, false),
            shiftParser = new InfixExpressionParser(
                    new String[] {"<<", ">>", ">>>"}, prependParser, false),
            relationParser = new InfixExpressionParser(
                    new String[] {"<", "<=", ">", ">="}, shiftParser, false),
            equalityParser = new InfixExpressionParser(
                    new String[] {"==", "!="}, relationParser, false),
            andParser = new InfixExpressionParser(
                    new String[] {"&"}, equalityParser, false),
            xorParser = new InfixExpressionParser(
                    new String[] {"^"}, andParser, false),
            iorParser = new InfixExpressionParser(
                    new String[] {"|"}, xorParser, false),
            rangeParser = new InfixExpressionParser(
                    new String[] {"..", "..."}, iorParser, false),
            assignmentParser = new InfixExpressionParser(
                    new String[] {"=", "|=", "^=", "&=", "<<=", ">>>=", ">>=",
                            "+=", "-=", "*=", "/=", "%=", "**="},
                    rangeParser, true);

    private final String[] operators;
    private final Parser<Expression> innerParser;
    private final boolean rightAssoc;

    private InfixExpressionParser(String[] operators, Parser<Expression> innerParser,
            boolean rightAssoc) {
        StringUtils.sortDescendingLength(operators);
        this.operators = operators;
        this.innerParser = innerParser;
        this.rightAssoc = rightAssoc;
    }

    private Success<String> parseOperator(String s, int p) {
        for (String op : operators)
            if (StringUtils.containsAt(s, op, p))
                return new Success<String>(op, p + op.length());
        return null;
    }

    private Expression getResult(Expression[] operands, String[] operators) {
        assert operands.length == operators.length + 1;
        Expression result;
        if (rightAssoc) {
            result = operands[operators.length];
            for (int i = operands.length - 2; i >= 0; --i) {
                String op = operators[i];
                result = new InfixOperation(operands[i], op, result);
            }
        } else {
            result = operands[0];
            for (int i = 1; i < operands.length; ++i) {
                String op = operators[i - 1];
                result = new InfixOperation(result, op, operands[i]);
            }
        }
        return result;
    }

    @Override
    public Success<Expression> parse(String s, int p) {
        List<Expression> operands = new ArrayList<Expression>();
        List<String> operators = new ArrayList<String>();

        // Parse the first operand.
        Success<Expression> resOperand = innerParser.parse(s, p);
        if (resOperand == null)
            return null;
        operands.add(resOperand.value);
        int pGood = p = resOperand.rem;

        // Parse any additional operands.
        for (;;) {
            // Parse the operator.
            p = optWS(s, p);
            Success<String> resOperator = parseOperator(s, p);
            if (resOperator == null)
                break;
            p = optWS(s, resOperator.rem);

            // Parse the next operand.
            resOperand = innerParser.parse(s, p);
            if (resOperand == null)
                break;
            p = resOperand.rem;

            // Update the result.
            operators.add(resOperator.value);
            operands.add(resOperand.value);
            pGood = p;
        }

        Expression result = getResult(
                operands.toArray(new Expression[operands.size()]),
                operators.toArray(new String[operators.size()]));
        return new Success<Expression>(result, pGood);
    }
}
