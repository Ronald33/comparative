package generator.test;

import org.junit.Assert;
import org.junit.Test;

import generator.Generator;
import generator.Range;

public class GeneratorTest
{
	public String writeFile()
	{
		return Generator.generateRange(10, new Range(1, 10), new Range(10,  20));
	}
	@Test
	public void test()
	{
		try
		{
			String content = this.writeFile();
			helper.Helper.writeFile("points", true, content);
			Assert.assertFalse(content.isEmpty());
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
