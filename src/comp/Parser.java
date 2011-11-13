package comp;

import ast.*;
import ast.exp.*;
import ast.stm.*;
import common.*;
import ctx.GlobalContext;

import java.io.*;
import java.util.*;

import static util.StringUtils.implode;

@SuppressWarnings({"serial"})
public final class Parser {
    private final static List<String> operators;

    static {
        operators = new ArrayList<String>();
        for (InfixOp op : InfixOp.values())
            operators.add(op.toString());
        for (PrefixOp op : PrefixOp.values())
            operators.add(op.toString());
        Collections.sort(operators, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s2.length() - s1.length();
            }
        });
    }

    private final CharSequence source;

    public Parser(CharSequence source) {
        this.source = preprocess(source);
    }

    private CharSequence preprocess(CharSequence source) {
        // TODO: strip comments (but be careful with quotes)
        // (or, just treat comments like WS as other languages do)
        return source;
    }

    private char next(int p) {
        try {
            return source.charAt(p);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("unexpected EOF");
        }
    }

    private void parseChar(int p, char c) {
        if (next(p) != c)
            throw new ParseException("expecting '%c', found '%c'", c, next(p));
    }

    private int parseString(int p, String s) {
        for (int i = 0; i < s.length(); ++i)
            if (next(p + i) != s.charAt(i))
                throw new ParseException("expecting \"%s\", found '%c' at %d", s, next(p + i), i);
        return p + s.length();
    }

    private int optWS(int p) {
        while (p < source.length()) {
            char c = source.charAt(p);
            if (Character.isWhitespace(c))
                ++p;
            else if (c == '#')
                while (p < source.length() && source.charAt(p) != '\n')
                    ++p;
            else break;
        }
        return p;
    }

    private int someWS(int p) {
        if (!Character.isWhitespace(next(p++)))
            throw new ParseException("expecting whitespace");
        return optWS(p);
    }

    private ParseResult<String> parseIdentifier(int p) {
        char c = next(p++);
        if (!Character.isJavaIdentifierStart(c))
            throw new ParseException("expecting identifier");
        StringBuilder sb = new StringBuilder().append(c);
        try {
            while (Character.isJavaIdentifierPart(c = next(p))) {
                sb.append(c);
                ++p;
            }
        } catch (ParseException e) {}
        return new ParseResult<String>(sb.toString(), p);
    }

    private ParseResult<String> parseIdentifierOrOp(int p) {
        try {
            return parseIdentifier(p);
        } catch (ParseException e) {}

        for (String op : operators)
            try {
                p = parseString(p, op);
                return new ParseResult<String>(op, p);
            } catch (ParseException e) {}

        throw new ParseException("expecting identifier or operator");
    }

    private ParseResult<Type> parseType(int p) {
        ParseResult<String> name = parseIdentifier(p);
        try {
            p = name.rem;
            p = optWS(p);
            ParseResult<Type[]> genericArgs = parseGenericArgList(p);
            p = genericArgs.rem;
            return new ParseResult<Type>(new Type(name.val, genericArgs.val), p);
        } catch (ParseException e) {
            return new ParseResult<Type>(new Type(name.val, new Type[0]), name.rem);
        }
    }

    private ParseResult<TypedVar> parseTypedVar(int p) {
        ParseResult<Type> resType = parseType(p);
        p = resType.rem;
        p = optWS(p);
        ParseResult<String> resName = parseIdentifier(p);
        p = resName.rem;
        return new ParseResult<TypedVar>(new TypedVar(resType.val, resName.val), p);
    }

    private ParseResult<TypedVar> parseTypedVarAllowOps(int p) {
        ParseResult<Type> resType = parseType(p);
        p = resType.rem;
        p = optWS(p);
        ParseResult<String> resName = parseIdentifierOrOp(p);
        p = resName.rem;
        return new ParseResult<TypedVar>(new TypedVar(resType.val, resName.val), p);
    }

    private ParseResult<Expression> parseLitInt(int p) {
        char c = next(p++);
        if (!Character.isDigit(c))
            throw new ParseException("expecting integer literal");
        StringBuilder sb = new StringBuilder().append(c);
        try {
            while (Character.isDigit(next(p)))
                sb.append(next(p++));
        } catch (ParseException e) {}
        return new ParseResult<Expression>(new LitInt(Integer.parseInt(sb.toString())), p);
    }

    private ParseResult<Expression> parseLitBool(int p) {
        try {
            p = parseString(p, "true");
            return new ParseResult<Expression>(LitBool.TRUE, p);
        } catch (ParseException e) {
            p = parseString(p, "false");
            return new ParseResult<Expression>(LitBool.FALSE, p);
        }
    }

    private ParseResult<Expression> parseLitChar(int p) {
        parseChar(p++, '\'');
        char c = next(p++);
        if (c == '\'')
            throw new ParseException("bad character literal");
        if (c == '\\') {
            c = next(p++);
            switch (c) {
                case 'n': c = '\n'; break;
                case 'r': c = '\r'; break;
                case '\\': c = '\\'; break;
                case 't': c = '\t'; break;
                case '0': c = '\0'; break;
                default: throw new ParseException("bad character literal");
            }
        }
        parseChar(p++, '\'');
        return new ParseResult<Expression>(new LitChar(c), p);
    }

    private ParseResult<Expression> parseParenExp(int p) {
        parseChar(p++, '(');
        p = optWS(p);
        ParseResult<Expression> resExp = parseExpression(p);
        p = resExp.rem;
        p = optWS(p);
        parseChar(p++, ')');
        return new ParseResult<Expression>(resExp.val, p);
    }

    private ParseResult<Expression> parseConstructor(int p) {
        p = parseString(p, "new");
        p = someWS(p);
        ParseResult<Type> resType = parseType(p);
        p = resType.rem;
        p = optWS(p);
        ParseResult<Expression[]> resArgs = parseArgumentList(p);
        p = resArgs.rem;
        return new ParseResult<Expression>(new NewExp(resType.val, resArgs.val), p);
    }

    private ParseResult<Expression> parseAtom(int p) {
        try {
            return parseParenExp(p);
        } catch (ParseException e1) {
        try {
            return parseLitInt(p);
        } catch (ParseException e2) {
        try {
            return parseLitBool(p);
        } catch (ParseException e3) {
        try {
            return parseLitChar(p);
        } catch (ParseException e4) {
        try {
            return parseConstructor(p);
        } catch (ParseException e5) {
            ParseResult<String> resId = parseIdentifier(p);
            return new ParseResult<Expression>(new VarExp(resId.val), resId.rem);
        }}}}}
    }

    private ParseResult<Expression[]> parseArgumentList(int p) {
        parseChar(p++, '(');
        p = optWS(p);
        List<Expression> parts = new ArrayList<Expression>();
        try {
            for (;;) {
                int p2 = p;
                if (!parts.isEmpty()) {
                    parseChar(p2++, ',');
                    p2 = optWS(p2);
                }
                ParseResult<Expression> res = parseExpression(p2);
                parts.add(res.val);
                p = res.rem;
                p = optWS(p);
            }
        } catch (ParseException e) {
            parseChar(p++, ')');
            return new ParseResult<Expression[]>(parts.toArray(new Expression[parts.size()]), p);
        }
    }

    private ParseResult<Expression> parseChain(int p) {
        ParseResult<Expression> resBase = parseAtom(p);
        Expression val = resBase.val;
        p = resBase.rem;
        try {
            int p2 = optWS(p);
            Type[] genericArgs;
            try {
                ParseResult<Type[]> resGenerics = parseGenericArgList(p2);
                genericArgs = resGenerics.val;
                p2 = resGenerics.rem;
                p2 = optWS(p2);
            } catch (ParseException e) {
                genericArgs = new Type[0];
            }
            ParseResult<Expression[]> resArgs = parseArgumentList(p2);
            val = new Invocation(val, genericArgs, resArgs.val);
            p = resArgs.rem;
        } catch (ParseException e) {}

        for (;;)
            try {
                int p2 = optWS(p);
                parseChar(p2++, '.');
                p2 = optWS(p2);
                ParseResult<String> resMember = parseIdentifierOrOp(p2);
                val = new MemberAccess(val, resMember.val);
                p = resMember.rem;

                try {
                    p2 = optWS(p);
                    Type[] genericArgs;
                    try {
                        ParseResult<Type[]> resGenerics = parseGenericArgList(p2);
                        genericArgs = resGenerics.val;
                        p2 = resGenerics.rem;
                        p2 = optWS(p2);
                    } catch (ParseException e) {
                        genericArgs = new Type[0];
                    }
                    ParseResult<Expression[]> resArgs = parseArgumentList(p2);
                    val = new Invocation(val, genericArgs, resArgs.val);
                    p = resArgs.rem;
                } catch (ParseException e) {}
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private ParseResult<Expression> parsePrefixExp(int p) {
        PrefixOp chosenOp = null;
        for (PrefixOp op : PrefixOp.values()) {
            try {
                p = parseString(p, op.toString());
                chosenOp = op;
                break;
            } catch (ParseException e) {}
        }
        ParseResult<Expression> resArg = parseChain(p);
        if (chosenOp == null)
            return resArg;
        return new ParseResult<Expression>(new PrefixExp(chosenOp, resArg.val), resArg.rem);
    }

    private static final InfixOp[] multiplicativeOps = {InfixOp.MUL, InfixOp.DIV, InfixOp.MOD};
    private ParseResult<Expression> parseMultiplicativeExp(int p) {
        ParseResult<Expression> resFirst = parsePrefixExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                InfixOp chosenOp = null;
                for (InfixOp op : multiplicativeOps)
                    try {
                        p2 = parseString(p2, op.toString());
                        chosenOp = op;
                        break;
                    } catch (ParseException e) {}
                if (chosenOp == null)
                    return new ParseResult<Expression>(val, p);
                p2 = optWS(p2);
                ParseResult<Expression> res = parsePrefixExp(p2);
                val = new InfixExp(val, chosenOp, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private static final InfixOp[] additiveOps = {InfixOp.ADD, InfixOp.SUB};
    private ParseResult<Expression> parseAdditiveExp(int p) {
        ParseResult<Expression> resFirst = parseMultiplicativeExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                InfixOp chosenOp = null;
                for (InfixOp op : additiveOps)
                    try {
                        p2 = parseString(p2, op.toString());
                        chosenOp = op;
                        break;
                    } catch (ParseException e) {}
                if (chosenOp == null)
                    return new ParseResult<Expression>(val, p);
                p2 = optWS(p2);
                ParseResult<Expression> res = parseMultiplicativeExp(p2);
                val = new InfixExp(val, chosenOp, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private static final InfixOp[] shiftOps = {InfixOp.RSH, InfixOp.LSH};
    private ParseResult<Expression> parseShiftExp(int p) {
        ParseResult<Expression> resFirst = parseAdditiveExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                InfixOp chosenOp = null;
                for (InfixOp op : shiftOps)
                    try {
                        p2 = parseString(p2, op.toString());
                        chosenOp = op;
                        break;
                    } catch (ParseException e) {}
                if (chosenOp == null)
                    return new ParseResult<Expression>(val, p);
                p2 = optWS(p2);
                ParseResult<Expression> res = parseAdditiveExp(p2);
                val = new InfixExp(val, chosenOp, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private static final InfixOp[] relationalOps = {InfixOp.LE, InfixOp.GE, InfixOp.LT, InfixOp.GT};
    private ParseResult<Expression> parseRelationalExp(int p) {
        ParseResult<Expression> resFirst = parseShiftExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                InfixOp chosenOp = null;
                for (InfixOp op : relationalOps)
                    try {
                        p2 = parseString(p2, op.toString());
                        chosenOp = op;
                        break;
                    } catch (ParseException e) {}
                if (chosenOp == null)
                    return new ParseResult<Expression>(val, p);
                p2 = optWS(p2);
                ParseResult<Expression> res = parseShiftExp(p2);
                val = new InfixExp(val, chosenOp, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private static final InfixOp[] equalityOps = {InfixOp.EQ, InfixOp.NEQ};
    private ParseResult<Expression> parseEqualityExp(int p) {
        ParseResult<Expression> resFirst = parseRelationalExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                InfixOp chosenOp = null;
                for (InfixOp op : equalityOps)
                    try {
                        p2 = parseString(p2, op.toString());
                        chosenOp = op;
                        break;
                    } catch (ParseException e) {}
                if (chosenOp == null)
                    return new ParseResult<Expression>(val, p);
                p2 = optWS(p2);
                ParseResult<Expression> res = parseRelationalExp(p2);
                val = new InfixExp(val, chosenOp, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private ParseResult<Expression> parseAndExp(int p) {
        ParseResult<Expression> resFirst = parseEqualityExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                p2 = parseString(p2, InfixOp.AND.toString());
                p2 = optWS(p2);
                ParseResult<Expression> res = parseEqualityExp(p2);
                val = new InfixExp(val, InfixOp.AND, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private ParseResult<Expression> parseXorExp(int p) {
        ParseResult<Expression> resFirst = parseAndExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                p2 = parseString(p2, InfixOp.XOR.toString());
                p2 = optWS(p2);
                ParseResult<Expression> res = parseAndExp(p2);
                val = new InfixExp(val, InfixOp.XOR, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private ParseResult<Expression> parseIorExp(int p) {
        ParseResult<Expression> resFirst = parseXorExp(p);
        Expression val = resFirst.val;
        p = resFirst.rem;
        for (;;)
            try {
                int p2 = optWS(p);
                p2 = parseString(p2, InfixOp.IOR.toString());
                p2 = optWS(p2);
                ParseResult<Expression> res = parseXorExp(p2);
                val = new InfixExp(val, InfixOp.IOR, res.val);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Expression>(val, p);
            }
    }

    private static final InfixOp[] assignmentOps = {
            InfixOp.IORASS, InfixOp.XORASS, InfixOp.ANDASS, InfixOp.LSHASS, InfixOp.RSHASS,
            InfixOp.ADDASS, InfixOp.SUBASS, InfixOp.MULASS, InfixOp.DIVASS, InfixOp.MODASS};
    private ParseResult<Expression> parseExpression(int p) {
        ParseResult<Expression> left = parseIorExp(p);
        try {
            p = left.rem;
            p = optWS(p);
            parseChar(p++, '=');
            p = optWS(p);
            ParseResult<Expression> right = parseExpression(p);
            p = right.rem;
            return new ParseResult<Expression>(new AssignmentExp(left.val, right.val), p);
        } catch (ParseException e) {
            try {
                p = left.rem;
                p = optWS(p);
                InfixOp chosenOp = null;
                for (InfixOp op : assignmentOps)
                    try {
                        p = parseString(p, op.toString());
                        chosenOp = op;
                        break;
                    } catch (ParseException e2) {}
                if (chosenOp == null)
                    throw new ParseException();
                p = optWS(p);
                ParseResult<Expression> right = parseExpression(p);
                p = right.rem;
                return new ParseResult<Expression>(new InfixExp(left.val, chosenOp, right.val), p);
            } catch (ParseException e2) {
                return left;
            }
        }
    }

    private ParseResult<BlockStm> parseBlockStm(int p) {
        parseChar(p++, '{');
        List<Statement> parts = new ArrayList<Statement>();
        for (;;)
            try {
                p = optWS(p);
                ParseResult<Statement> res = parseStatement(p);
                parts.add(res.val); p = res.rem;
            } catch (ParseException e) {
                parseChar(p++, '}');
                return new ParseResult<BlockStm>(new BlockStm(parts.toArray(new Statement[parts.size()])), p);
            }
    }

    private ParseResult<IfElseStm> parseIfElseStm(int p) {
        p = parseString(p, "if");
        p = optWS(p);
        parseChar(p++, '(');
        p = optWS(p);
        ParseResult<Expression> resCond = parseExpression(p);
        p = resCond.rem;
        p = optWS(p);
        parseChar(p++, ')');
        p = optWS(p);
        ParseResult<Statement> resTrue = parseStatement(p);
        p = resTrue.rem;
        p = optWS(p);
        p = parseString(p, "else");
        p = optWS(p);
        ParseResult<Statement> resFalse = parseStatement(p);
        p = resFalse.rem;
        return new ParseResult<IfElseStm>(new IfElseStm(resCond.val, resTrue.val, resFalse.val), p);
    }

    private ParseResult<IfStm> parseIfStm(int p) {
        p = parseString(p, "if");
        p = optWS(p);
        parseChar(p++, '(');
        p = optWS(p);
        ParseResult<Expression> resCond = parseExpression(p);
        p = resCond.rem;
        p = optWS(p);
        parseChar(p++, ')');
        p = optWS(p);
        ParseResult<Statement> resTrue = parseStatement(p);
        p = resTrue.rem;
        return new ParseResult<IfStm>(new IfStm(resCond.val, resTrue.val), p);
    }

    private ParseResult<WhileStm> parseWhileStm(int p) {
        p = parseString(p, "while");
        p = optWS(p);
        parseChar(p++, '(');
        p = optWS(p);
        ParseResult<Expression> resCond = parseExpression(p);
        p = resCond.rem;
        p = optWS(p);
        parseChar(p++, ')');
        p = optWS(p);
        ParseResult<Statement> resTrue = parseStatement(p);
        p = resTrue.rem;
        return new ParseResult<WhileStm>(new WhileStm(resCond.val, resTrue.val), p);
    }

    private ParseResult<ForStm> parseForStm(int p) {
        p = parseString(p, "for");
        p = optWS(p);
        parseChar(p++, '(');
        p = optWS(p);
        ParseResult<Statement> resA = parseStatement(p);
        p = resA.rem;
        p = optWS(p);

        Expression b;
        try {
            ParseResult<Expression> resB = parseExpression(p);
            b = resB.val;
            p = resB.rem;
            p = optWS(p);
        } catch (ParseException e) {
            b = null;
        }
        parseChar(p++, ';');
        p = optWS(p);

        Expression c;
        try {
            ParseResult<Expression> resC = parseExpression(p);
            c = resC.val;
            p = resC.rem;
            p = optWS(p);
        } catch (ParseException e) {
            c = null;
        }

        parseChar(p++, ')');
        p = optWS(p);
        ParseResult<Statement> resBody = parseStatement(p);
        p = resBody.rem;
        return new ParseResult<ForStm>(new ForStm(resA.val, b, c, resBody.val), p);
    }

    private ParseResult<IterationStm> parseIterationStm(int p) {
        p = parseString(p, "for");
        p = optWS(p);
        parseChar(p++, '(');
        p = optWS(p);
        ParseResult<TypedVar> resVar = parseTypedVar(p);
        p = resVar.rem;
        p = optWS(p);
        parseChar(p++, ':');
        p = optWS(p);
        ParseResult<Expression> resIterable = parseExpression(p);
        p = resIterable.rem;
        p = optWS(p);
        parseChar(p++, ')');
        p = optWS(p);
        ParseResult<Statement> resBody = parseStatement(p);
        p = resBody.rem;
        return new ParseResult<IterationStm>(new IterationStm(resVar.val, resIterable.val, resBody.val), p);
    }

    private ParseResult<ReturnStm> parseReturnStm(int p) {
        p = parseString(p, "return");
        p = optWS(p);
        ParseResult<Expression> resExp = parseExpression(p);
        p = resExp.rem;
        p = optWS(p);
        parseChar(p++, ';');
        return new ParseResult<ReturnStm>(new ReturnStm(resExp.val), p);
    }

    private ParseResult<LocalDefStm> parseLocalDefStm(int p) {
        ParseResult<TypedVar> resSelf = parseTypedVar(p);
        p = resSelf.rem;
        Expression initVal = null;
        try {
            int p2 = optWS(p);
            parseChar(p2++, '=');
            p2 = optWS(p2);
            ParseResult<Expression> resInitVal = parseExpression(p2);
            initVal = resInitVal.val;
            p = resInitVal.rem;
        } catch (ParseException e) {}
        p = optWS(p);
        parseChar(p++, ';');
        return new ParseResult<LocalDefStm>(new LocalDefStm(resSelf.val, initVal), p);
    }

    private ParseResult<ExpStm> parseExpStm(int p) {
        ParseResult<Expression> resExp = parseExpression(p);
        p = resExp.rem;
        p = optWS(p);
        parseChar(p++, ';');
        return new ParseResult<ExpStm>(new ExpStm(resExp.val), p);
    }

    private ParseResult<Statement> parseStatement(int p) {
        // Yuuuuuuck!!
        try {
            return (ParseResult) parseIfElseStm(p);
        } catch (ParseException e1) {
        try {
            return (ParseResult) parseIfStm(p);
        } catch (ParseException e2) {
        try {
            return (ParseResult) parseWhileStm(p);
        } catch (ParseException e3) {
        try {
            return (ParseResult) parseIterationStm(p);
        } catch (ParseException e4) {
        try {
            return (ParseResult) parseForStm(p);
        } catch (ParseException e5) {
        try {
            return (ParseResult) parseReturnStm(p);
        } catch (ParseException e6) {
        try {
            return (ParseResult) parseLocalDefStm(p);
        } catch (ParseException e7) {
        try {
            return (ParseResult) parseExpStm(p);
        } catch (ParseException e8) {
        try {
            return (ParseResult) parseBlockStm(p);
        } catch (ParseException e9) {
        try {
            // Try the empty statement
            parseChar(p++, ';');
            return new ParseResult<Statement>(EmptyStm.INST, p);
        } catch (ParseException e10) {
            throw new ParseException("expecting statement");
        }}}}}}}}}} // :-\
    }

    private ParseResult<MemberDef[]> parseFieldDef(int p) {
        ParseResult<String[]> resQuals = parseFieldQualifiers(p);
        p = resQuals.rem; p = optWS(p);
        ParseResult<Type> resType = parseType(p);
        p = resType.rem; p = optWS(p);

        ParseResult<String> resFirst = parseIdentifier(p);
        p = resFirst.rem; p = optWS(p);
        List<FieldDef> parts = new ArrayList<FieldDef>();
        parts.add(new FieldDef(resQuals.val, new TypedVar(resType.val, resFirst.val)));

        for (;;)
            try {
                parseChar(p, ',');
                int p2 = p + 1;
                p2 = optWS(p2);
                ParseResult<String> resNext = parseIdentifier(p2);
                parts.add(new FieldDef(resQuals.val, new TypedVar(resType.val, resNext.val)));
                p = optWS(resNext.rem);
            } catch (ParseException e) {
                parseChar(p++, ';');
                return new ParseResult<MemberDef[]>(parts.toArray(new MemberDef[parts.size()]), p);
            }
    }

    private ParseResult<TypedVar[]> parseParamList(int p) {
        parseChar(p++, '(');
        p = optWS(p);
        List<TypedVar> parts = new ArrayList<TypedVar>();
        for (;;)
            try {
                if (!parts.isEmpty()) {
                    p = optWS(p);
                    parseChar(p, ','); ++p;
                    p = optWS(p);
                }
                ParseResult<TypedVar> res = parseTypedVar(p);
                parts.add(res.val); p = res.rem;
            } catch (ParseException e) {
                parseChar(p++, ')');
                return new ParseResult<TypedVar[]>(parts.toArray(new TypedVar[parts.size()]), p);
            }
    }

    private static final Set<String> typeQuals = new HashSet<String>() {{
        for (String s : new String[] {"public", "abstract", "sealed"})
            add(s);
    }};

    private static final Set<String> fieldQuals = new HashSet<String>() {{
        for (String s : new String[] {"public", "private", "static", "readonly"})
            add(s);
    }};

    private static final Set<String> methodQuals = new HashSet<String>() {{
        for (String s : new String[] {"public", "private", "static", "sealed"})
            add(s);
    }};

    private ParseResult<String> parseTypeQualifier(int p) {
        ParseResult<String> resQual = parseIdentifier(p);
        if (!typeQuals.contains(resQual.val))
            throw new ParseException("bad type qualifier: \"%s\"", resQual.val);
        return resQual;
    }

    private ParseResult<String> parseFieldQualifier(int p) {
        ParseResult<String> resQual = parseIdentifier(p);
        if (!fieldQuals.contains(resQual.val))
            throw new ParseException("bad field qualifier: \"%s\"", resQual.val);
        return resQual;
    }

    private ParseResult<String> parseMethodQualifier(int p) {
        ParseResult<String> resQual = parseIdentifier(p);
        if (!methodQuals.contains(resQual.val))
            throw new ParseException("bad method qualifier: \"%s\"", resQual.val);
        return resQual;
    }

    private ParseResult<String[]> parseTypeQualifiers(int p) {
        List<String> quals = new ArrayList<String>();
        for (;;)
            try {
                ParseResult<String> q = parseTypeQualifier(quals.isEmpty()? p : optWS(p));
                quals.add(q.val); p = q.rem;
            } catch (ParseException e) {
                return new ParseResult<String[]>(quals.toArray(new String[quals.size()]), p);
            }
    }

    private ParseResult<String[]> parseFieldQualifiers(int p) {
        List<String> quals = new ArrayList<String>();
        for (;;)
            try {
                ParseResult<String> q = parseFieldQualifier(quals.isEmpty()? p : optWS(p));
                quals.add(q.val); p = q.rem;
            } catch (ParseException e) {
                return new ParseResult<String[]>(quals.toArray(new String[quals.size()]), p);
            }
    }

    private ParseResult<String[]> parseMethodQualifiers(int p) {
        List<String> quals = new ArrayList<String>();
        for (;;)
            try {
                ParseResult<String> q = parseMethodQualifier(quals.isEmpty()? p : optWS(p));
                quals.add(q.val); p = q.rem;
            } catch (ParseException e) {
                return new ParseResult<String[]>(quals.toArray(new String[quals.size()]), p);
            }
    }

    private ParseResult<MemberDef[]> parseMethodDef(int p) {
        ParseResult<String[]> resQuals = parseMethodQualifiers(p);
        p = resQuals.rem;
        p = optWS(p);

        ParseResult<TypedVar> resSelf = parseTypedVarAllowOps(p);
        p = resSelf.rem;
        p = optWS(p);

        ParseResult<String[]> resGenerics = parseNonvariantGenericParamDecList(p);
        p = resGenerics.rem;
        p = optWS(p);

        ParseResult<TypedVar[]> resParams = parseParamList(p);
        p = resParams.rem;
        p = optWS(p);

        BlockStm body;
        if (next(p) == ';') {
            ++p;
            body = null;
        } else {
            ParseResult<BlockStm> resBody = parseBlockStm(p);
            p = resBody.rem;
            body = resBody.val;
        }
        // TODO: parse generic constraints
        MethodDef def = new MethodDef(new GenericConstraint[0], resQuals.val, resSelf.val,
                resGenerics.val, resParams.val, body);
        return new ParseResult<MemberDef[]>(new MemberDef[] {def}, p);
    }

    private ParseResult<MemberDef[]> parseMemberDef(int p) {
        try {
            return parseFieldDef(p);
        } catch (ParseException e) {
            return parseMethodDef(p);
        }
    }

    private ParseResult<MemberDef[]> parseMembers(int p) {
        List<MemberDef> parts = new ArrayList<MemberDef>();
        for (;;)
            try {
                ParseResult<MemberDef[]> res = parseMemberDef(parts.isEmpty() ? p : optWS(p));
                for (MemberDef mem : res.val)
                    parts.add(mem);
                p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<MemberDef[]>(parts.toArray(new MemberDef[parts.size()]), p);
            }
    }

    private ParseResult<Import> parseImport(int p) {
        p = someWS(parseString(p, "import"));
        ParseResult<String> resModule = parseIdentifier(p);
        p = optWS(resModule.rem);
        parseChar(p++, '.');
        p = optWS(p);
        ParseResult<String> resType = parseIdentifier(p);
        p = optWS(resType.rem);
        parseChar(p++, ';');
        Import imp = new Import(resModule.val, resType.val);
        return new ParseResult<Import>(imp, p);
    }

    private ParseResult<Import[]> parseImports(int p) {
        List<Import> parts = new ArrayList<Import>();
        for (;;)
            try {
                ParseResult<Import> res = parseImport(parts.isEmpty()? p : optWS(p));
                parts.add(res.val); p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Import[]>(parts.toArray(new Import[parts.size()]), p);
            }
    }

    private ParseResult<Variance> parseVariance(int p) {
        char c;
        try {
            c = next(p);
        } catch (ParseException e) {
            return new ParseResult<Variance>(Variance.NONVARIANT, p);
        }

        if (c == '+')
            return new ParseResult<Variance>(Variance.COVARIANT, p + 1);
        if (c == '-')
            return new ParseResult<Variance>(Variance.CONTRAVARIANT, p + 1);
        return new ParseResult<Variance>(Variance.NONVARIANT, p);
    }

    private ParseResult<GenericParamDec> parseGenericParamDec(int p) {
        ParseResult<Variance> resVariance = parseVariance(p);
        p = resVariance.rem;
        p = optWS(p);
        ParseResult<String> resName = parseIdentifier(p);
        p = resName.rem;
        return new ParseResult<GenericParamDec>(new GenericParamDec(resVariance.val, resName.val), p);
    }

    private ParseResult<GenericParamDec[]> parseGenericParamDecList(int p) {
        try {
            parseChar(p, '['); ++p;
        } catch (ParseException e) {
            return new ParseResult<GenericParamDec[]>(new GenericParamDec[0], p);
        }
        p = optWS(p);
        List<GenericParamDec> parts = new ArrayList<GenericParamDec>();
        for (;;)
            try {
                if (!parts.isEmpty()) {
                    p = optWS(p);
                    parseChar(p, ','); ++p;
                    p = optWS(p);
                }
                ParseResult<GenericParamDec> res = parseGenericParamDec(p);
                parts.add(res.val); p = res.rem;
            } catch (ParseException e) {
                parseChar(p++, ']');
                return new ParseResult<GenericParamDec[]>(parts.toArray(new GenericParamDec[parts.size()]), p);
            }
    }

    private ParseResult<String[]> parseNonvariantGenericParamDecList(int p) {
        try {
            parseChar(p, '['); ++p;
        } catch (ParseException e) {
            return new ParseResult<String[]>(new String[0], p);
        }
        p = optWS(p);
        List<String> parts = new ArrayList<String>();
        for (;;)
            try {
                if (!parts.isEmpty()) {
                    p = optWS(p);
                    parseChar(p, ','); ++p;
                    p = optWS(p);
                }
                ParseResult<String> res = parseIdentifier(p);
                parts.add(res.val); p = res.rem;
            } catch (ParseException e) {
                parseChar(p++, ']');
                return new ParseResult<String[]>(parts.toArray(new String[parts.size()]), p);
            }
    }

    private ParseResult<Type[]> parseTypeList(int p) {
        List<Type> parts = new ArrayList<Type>();
        for (;;)
            try {
                int p2 = p;
                if (!parts.isEmpty()) {
                    p2 = optWS(p2);
                    parseChar(p2++, ',');
                    p2 = optWS(p2);
                }
                ParseResult<Type> res = parseType(p2);
                parts.add(res.val); p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<Type[]>(parts.toArray(new Type[parts.size()]), p);
            }
    }

    private ParseResult<Type[]> parseGenericArgList(int p) {
        parseChar(p++, '[');
        p = optWS(p);
        List<Type> parts = new ArrayList<Type>();
        try {
            for (;;) {
                int p2 = p;
                if (!parts.isEmpty()) {
                    parseChar(p2++, ',');
                    p2 = optWS(p2);
                }
                ParseResult<Type> res = parseType(p2);
                parts.add(res.val);
                p = res.rem;
                p = optWS(p);
            }
        } catch (ParseException e) {
            parseChar(p++, ']');
            return new ParseResult<Type[]>(parts.toArray(new Type[parts.size()]), p);
        }
    }

    private ParseResult<TypeDef> parseTypeDef(int p) {
        ParseResult<String[]> resQuals = parseTypeQualifiers(p);
        p = resQuals.rem;
        p = optWS(p);

        p = parseString(p, "type");
        p = someWS(p);
        ParseResult<String> resName = parseIdentifier(p);
        p = resName.rem;
        p = optWS(p);

        // Generic params
        ParseResult<GenericParamDec[]> resGenerics = parseGenericParamDecList(p);
        p = resGenerics.rem;
        p = optWS(p);

        // Super types
        Type[] supers;
        try {
            p = parseString(p, "extends");
            p = someWS(p);
            ParseResult<Type[]> resSupers = parseTypeList(p);
            supers = resSupers.val;
            p = resSupers.rem;
            p = optWS(p);
        } catch (ParseException e) {
            supers = new Type[0];
        }

        // Members
        parseChar(p++, '{');
        p = optWS(p);
        ParseResult<MemberDef[]> resMembers = parseMembers(p);
        p = resMembers.rem;
        p = optWS(p);
        parseChar(p++, '}');

        // TODO: parse generic constraints
        TypeDef def = new TypeDef(new GenericConstraint[0], resQuals.val, resName.val, resGenerics.val, supers, resMembers.val);
        return new ParseResult<TypeDef>(def, p);
    }

    private ParseResult<TypeDef[]> parseTypeDefs(int p) {
        List<TypeDef> parts = new ArrayList<TypeDef>();
        for (;;)
            try {
                ParseResult<TypeDef> res = parseTypeDef(parts.isEmpty() ? p : optWS(p));
                parts.add(res.val); p = res.rem;
            } catch (ParseException e) {
                return new ParseResult<TypeDef[]>(parts.toArray(new TypeDef[parts.size()]), p);
            }
    }

    public SourceFile parseSourceFile() {
        int p = optWS(0);
        p = parseString(p, "module");
        p = someWS(p);
        ParseResult<String> resModule = parseIdentifier(p);
        p = resModule.rem;
        p = optWS(p);
        parseChar(p++, ';');
        p = optWS(p);
        ParseResult<Import[]> resImports = parseImports(p);
        p = optWS(resImports.rem);
        ParseResult<TypeDef[]> resTypeDefs = parseTypeDefs(p);
        p = optWS(resTypeDefs.rem);
        if (p != source.length())
            // FIXME: uncomment?
            throw new ParseException("expecting EOF (did not parse whole input)");
        return new SourceFile(resModule.val, resImports.val, resTypeDefs.val);
    }
}

class ParseResult<T> {
    public final T val;
    public final int rem;

    public ParseResult(T val, int rem) {
        this.val = val;
        this.rem = rem;
    }

    public String toString() {
        return val.toString();
    }
}

class ParseException extends RuntimeException {
    public ParseException() {
        super();
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String format, Object... args) {
        super(String.format(format, args));
    }
}
