package logic;

import static org.junit.Assert.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

public class SplitTest {
	public static String hi = "Привет, как дела?";
    public static String h2ruen = "Hi, как дела*";
	@Test
	public void testRegex(){
		Pattern p = Pattern.compile(TextProcessor.ruRegex);
		Matcher m = p.matcher(hi);
		assertEquals(true, m.find());
	}
	
	@Test
	public void testRegexGroups(){
		Pattern p = Pattern.compile(TextProcessor.ruRegex);
		Matcher m = p.matcher(hi);
		
		assertEquals(true, m.find());
		System.out.println(m.group());
		
		assertEquals(true, m.find());
		System.out.println(m.group());
		
		assertEquals(true, m.find());
		System.out.println(m.group());
				
	}

	// @Ignore
	@Test
	public void testSplit() {
		List<String> a = TextProcessor.split2(hi);
		
		System.out.println("out array size: "+ a.size());
		
		for(String s: a)
			System.out.println(s);
		
		assertEquals(3, a.size());
		
		assertEquals("Привет", a.get(0));
		assertEquals("как", a.get(1));
		assertEquals("дела", a.get(2));
	}

    @Test
    public void testSplitRuEn() {
        List<String> a = TextProcessor.split2(h2ruen);

        System.out.println("out array size: "+ a.size());

        for(String s: a)
            System.out.println(s);

        assertEquals(3, a.size());

        assertEquals("Hi", a.get(0));
        assertEquals("как", a.get(1));
        assertEquals("дела", a.get(2));
    }

}
