package me.konsolas.conditionalcommands;

import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testEquality() {
        Expression expression = new Expression("10 = 10");
        Assert.assertTrue(expression.evaluate());
    }

    @Test
    public void testGreaterThan() {
        Expression expression = new Expression("10 > 9");
        Assert.assertTrue(expression.evaluate());
    }

    @Test
    public void testLessThan() {
        Expression expression = new Expression("9 < 11");
        Assert.assertTrue(expression.evaluate());
    }

    @Test
    public void testOr() {
        Expression expression = new Expression("9 < 11 | 9 = 9");
        Assert.assertTrue(expression.evaluate());
    }

    @Test
    public void testAnd() {
        Expression expression = new Expression("9 < 11 | 9 = 9");
        Assert.assertTrue(expression.evaluate());
    }

    @Test
    public void testNot() {
        Expression expression = new Expression("!(1 > 10)");
        Assert.assertTrue(expression.evaluate());
    }

    @Test
    public void testBrackets() {
        Expression expression = new Expression("!(1 < 1 | 2 < 1) & 4 > 1");
        Assert.assertTrue(expression.evaluate());
    }

    @Test(expected = Expression.ParseException.class)
    public void testMissedPlaceholder() {
        Expression expression = new Expression("(1 = 1 | 2 < 1) & -ping- > 1");
        Assert.assertTrue(expression.evaluate());
    }
}
