package de.skuzzle.enforcer.restrictimports.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.skuzzle.enforcer.restrictimports.model.PackagePattern;

public class PackagePatternImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() throws Exception {
        PackagePattern.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull2() throws Exception {
        PackagePattern.parseAll(null);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("de.skuzzle.**", PackagePattern.parse("de.skuzzle.**").toString());
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(PackagePattern.parse("de.skuzzle.**"),
                PackagePattern.parse("de.skuzzle.**"));
        assertEquals(PackagePattern.parse("de.skuzzle.**").hashCode(),
                PackagePattern.parse("de.skuzzle.**").hashCode());
    }

    @Test
    public void testNotEquals() throws Exception {
        assertNotEquals(PackagePattern.parse("de.skuzzle.**"),
                PackagePattern.parse("de.skuzzle.*"));
    }

    @Test
    public void testMatchDefaultPackage() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("**");
        assertTrue(pattern.matches(""));
    }

    @Test
    public void testMatchesDefaultPackage2() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("*");
        assertTrue(pattern.matches(""));
    }

    @Test
    public void testMatchExactly() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("de.skuzzle.SomeClass");
        assertTrue(pattern.matches("de.skuzzle.SomeClass"));
        assertFalse(pattern.matches("de.skuzzle.SomeClass2"));
    }

    @Test
    public void testMatchWildCardSuffix() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("de.skuzzle.*");
        assertTrue(pattern.matches("de.skuzzle.TestClass"));
        assertTrue(pattern.matches("de.skuzzle.TestClass2"));
    }

    @Test
    public void testWildCardMatchesSingle() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("de.skuzzle.*");
        assertFalse(pattern.matches("de.skuzzle.sub.TestClass"));
    }

    @Test
    public void testMatchInfix() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("de.skuzzle.*.test");
        assertTrue(pattern.matches("de.skuzzle.foo.test"));
        assertFalse(pattern.matches("de.skuzzle.test"));
    }

    @Test
    public void testWildCatMatchMultiple() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("de.skuzzle.**");
        assertTrue(pattern.matches("de.skuzzle.sub.TestClass"));
        assertTrue(pattern.matches("de.skuzzle.TestClass2"));
    }

    @Test
    public void testLogger() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("java.util.**");
        assertTrue(pattern.matches("java.util.logging.Logger"));
    }

    @Test
    public void testWildcardInStringToTest() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("java.util.ArrayList");
        assertFalse(pattern.matches("java.util.*"));
    }

    @Test
    public void testDoubleWildcardInBetween() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("com.**.bar.ClassName");
        assertTrue(pattern.matches("com.foo.bar.ClassName"));
        assertFalse(pattern.matches("com.xyz.foo.bar"));
        assertFalse(pattern.matches("com.xyz.foo.ClassName"));
        assertFalse(pattern.matches("com.bar.ClassName"));
    }

    @Test
    public void testDoubleWildcardInBetweenSkipMultiple() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("com.**.bar.ClassName");
        assertTrue(pattern.matches("com.xyz.foo.bar.ClassName"));
        assertFalse(pattern.matches("com.xyz.foo.bar"));
    }

    @Test
    public void testConsecutiveDoubleWildcard() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("com.**.**.ClassName");
        assertTrue(pattern.matches("com.xyz.foo.bar.ClassName"));
        assertFalse(pattern.matches("com.xyz.foo.bar"));
    }

    @Test
    public void testDoubleWildCartBeginning() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("**.ClassName");
        assertTrue(pattern.matches("com.xyz.foo.bar.ClassName"));
    }

    @Test
    public void test() throws Exception {
        final PackagePattern pattern = PackagePattern.parse("com.foo.**");
        assertFalse(pattern.matches("java.util.ArrayList"));
    }
}
