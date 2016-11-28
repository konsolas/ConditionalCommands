package me.konsolas.conditionalcommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

class Expression {
    private final BooleanExpression expression;

    Expression(String exp) {
        BooleanLexer lex = new BooleanLexer(exp);
        BooleanParser parser = new BooleanParser(lex);
        this.expression = parser.build();
    }

    boolean evaluate() {
        return (boolean) expression.interpret();
    }

    @Override
    public String toString() {
        return expression.toString();
    }

    private interface BooleanExpression {
        Object interpret();
    }

    private static class BooleanLexer {
        private final StreamTokenizer input;

        BooleanLexer(String in) {
            input = new StreamTokenizer(new BufferedReader(new StringReader(in)));

            // Syntax rules
            input.resetSyntax();
            input.wordChars('a', 'z');
            input.wordChars('A', 'Z');
            input.whitespaceChars('\u0000', ' ');
            input.whitespaceChars('\n', '\t');

            input.ordinaryChar('(');
            input.ordinaryChar(')');
            input.ordinaryChar('&');
            input.ordinaryChar('+');
            input.ordinaryChar('|');
            input.ordinaryChar('!');

            // Comparison
            input.ordinaryChar('>');
            input.ordinaryChar('=');
            input.ordinaryChar('<');

            input.parseNumbers();
        }

        SymbolObject nextToken() {
            Symbol symbol;
            Double data = null;

            try {
                int token = input.nextToken();
                switch (token) {
                    case StreamTokenizer.TT_EOL:
                        symbol = Symbol.EOL;
                        break;
                    case StreamTokenizer.TT_EOF:
                        symbol = Symbol.EOF;
                        break;
                    case StreamTokenizer.TT_NUMBER:
                        symbol = Symbol.NUMBER;
                        data = input.nval;
                        break;
                    case '(':
                        symbol = Symbol.LEFT;
                        break;
                    case ')':
                        symbol = Symbol.RIGHT;
                        break;
                    case '&':
                    case '+':
                        symbol = Symbol.AND;
                        break;
                    case '|':
                        symbol = Symbol.OR;
                        break;
                    case '!':
                        symbol = Symbol.NOT;
                        break;
                    case '>':
                        symbol = Symbol.CMP_GREATER_THAN;
                        break;
                    case '=':
                        symbol = Symbol.CMP_EQUALS;
                        break;
                    case '<':
                        symbol = Symbol.CMP_LESS_THAN;
                        break;
                    default:
                        throw new RuntimeException("Lexer error: Got an invalid token");
                }
            } catch (IOException ex) {
                symbol = Symbol.EOF;
            }
            return new SymbolObject(symbol, data);
        }

        // Types of symbol
        enum Symbol {
            EOF, EOL, OR, AND, NOT, LEFT, RIGHT, CMP_EQUALS, CMP_LESS_THAN, CMP_GREATER_THAN, NUMBER
        }

        static class SymbolObject {
            private Symbol symbol;
            private Double data;

            SymbolObject(Symbol symbol, Double data) {
                this.symbol = symbol;
                this.data = data == null ? 0 : data;
            }

            Symbol getSymbol() {
                return symbol;
            }

            double getData() {
                return data != null ? data : 0;
            }
        }
    }

    private static abstract class Terminal implements BooleanExpression {
        final Object value;

        Terminal(Object value) {
            this.value = value;
        }

        @Override
        public Object interpret() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("%s", value);
        }
    }

    private static abstract class NonTerminal implements BooleanExpression {
        BooleanExpression left;
        BooleanExpression right;

        void setLeft(BooleanExpression left) {
            this.left = left;
        }

        public void setRight(BooleanExpression right) {
            this.right = right;
        }
    }

    private static final class Number extends Terminal {
        Number(double number) {
            super(number);
        }
    }

    private static final class And extends NonTerminal {
        @Override
        public Object interpret() {
            boolean left = (Boolean) this.left.interpret();
            boolean right = (Boolean) this.right.interpret();

            return left && right;
        }

        @Override
        public String toString() {
            return String.format("(%s & %s)", left, right);
        }
    }

    private static final class Or extends NonTerminal {
        @Override
        public Object interpret() {
            boolean left = (Boolean) this.left.interpret();
            boolean right = (Boolean) this.right.interpret();

            return left || right;
        }

        @Override
        public String toString() {
            return String.format("(%s | %s)", left, right);
        }
    }

    private static final class Not extends NonTerminal {
        void setChild(BooleanExpression child) {
            setLeft(child);
        }

        @Override
        public void setRight(BooleanExpression right) {
            throw new UnsupportedOperationException();
        }

        public Object interpret() {
            return !((Boolean) this.left.interpret());
        }

        public String toString() {
            return String.format("!%s", left);
        }
    }

    private static final class CmpEquals extends NonTerminal {
        @Override
        public Object interpret() {
            double left = (Double) this.left.interpret();
            double right = (Double) this.right.interpret();

            return left == right;
        }

        @Override
        public String toString() {
            return String.format("(%s = %s)", left, right);
        }
    }

    private static final class CmpGreaterThan extends NonTerminal {
        @Override
        public Object interpret() {
            double left = (Double) this.left.interpret();
            double right = (Double) this.right.interpret();

            return left > right;
        }

        @Override
        public String toString() {
            return String.format("(%s > %s)", left, right);
        }
    }

    private static final class CmpLessThan extends NonTerminal {
        @Override
        public Object interpret() {
            double left = (Double) this.left.interpret();
            double right = (Double) this.right.interpret();

            return left < right;
        }

        @Override
        public String toString() {
            return String.format("(%s < %s)", left, right);
        }
    }

    // Errors
    static class ParseException extends RuntimeException {
        ParseException(String desc) {
            super(desc);
        }
    }

    /*
     * Recursive Descent Parser
     *
     * <expression>::=<term>{<or><term>}
     * <term>::=<factor>{<and><factor>}
     * <factor>::=<comparison>|<not><factor>|(<expression>)
     * <comparison>::=<constant><comparator><constant>
     * <constant>::=floating point number or integer
     *
     * <and>::='&'
     * <or>::='|'
     * <not>::='!'
     * <comparator>::='>'|'='|'<'
     */
    private static class BooleanParser {
        private BooleanLexer lexer;
        private BooleanLexer.SymbolObject symbol;
        private BooleanExpression root;

        BooleanParser(BooleanLexer lexer) {
            this.lexer = lexer;
        }

        BooleanExpression build() {
            try {
                expression();
                root.interpret();
            } catch (NullPointerException ex) {
                throw new ParseException("Failed to parse: root expression is null.");
            } catch (Exception ex) {
                throw new ParseException(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
            return root;
        }

        // Begin the descent....
        private void expression() {
            term();
            while (symbol.getSymbol() == BooleanLexer.Symbol.OR) {
                Or or = new Or();
                or.setLeft(root);
                term();
                or.setRight(root);
                root = or;
            }
        }

        private void term() {
            factor();
            while (symbol.getSymbol() == BooleanLexer.Symbol.AND) {
                And and = new And();
                and.setLeft(root);
                factor();
                and.setRight(root);
                root = and;
            }
        }

        private void factor() {
            comparison();

            if (symbol.getSymbol() == BooleanLexer.Symbol.NOT) {
                Not not = new Not();
                factor();
                not.setChild(root);
                root = not;
            } else if (symbol.getSymbol() == BooleanLexer.Symbol.LEFT) {
                expression();
                symbol = lexer.nextToken();
            }
        }

        private void comparison() {
            constant();

            if (symbol.getSymbol() == BooleanLexer.Symbol.CMP_EQUALS) {
                CmpEquals equals = new CmpEquals();
                equals.setLeft(root);
                constant();
                equals.setRight(root);
                root = equals;
            } else if (symbol.getSymbol() == BooleanLexer.Symbol.CMP_GREATER_THAN) {
                CmpGreaterThan greater = new CmpGreaterThan();
                greater.setLeft(root);
                constant();
                greater.setRight(root);
                root = greater;
            } else if (symbol.getSymbol() == BooleanLexer.Symbol.CMP_LESS_THAN) {
                CmpLessThan less = new CmpLessThan();
                less.setLeft(root);
                constant();
                less.setRight(root);
                root = less;
            }
        }

        private void constant() {
            symbol = lexer.nextToken();
            if (symbol.getSymbol() == BooleanLexer.Symbol.NUMBER) {
                root = new Number(symbol.getData());
                symbol = lexer.nextToken();
            }
        }
    }
}