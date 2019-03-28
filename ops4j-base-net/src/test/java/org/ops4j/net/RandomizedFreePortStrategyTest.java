package org.ops4j.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class RandomizedFreePortStrategyTest {

	public static final PortTester ALWAYS_FALSE_PORT_TESTER = new PortTester()
	{
		@Override
		public boolean isFree(int port)
		{
			return false;
		}
	};

	public static final PortTester ALWAYS_TRUE_PORT_TESTER = new PortTester()
	{
		@Override
		public boolean isFree(int port)
		{
			return true;
		}
	};

	@Test(expected = NullPointerException.class)
	public void testPortTestNullGivesException()
	{
		new RandomizedFreePortStrategy(null, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAttemptsZeroGivesException()
	{
		new RandomizedFreePortStrategy(ALWAYS_FALSE_PORT_TESTER, 0);
	}

	@Test(expected = RuntimeException.class)
	public void testFindFreeFailsThenExceptionThrown()
	{
		FreePortStrategy strategy = new RandomizedFreePortStrategy(ALWAYS_FALSE_PORT_TESTER, 1);
		strategy.findFree(1, 1);
	}

	@Test
	public void testFindFreeWithAlwaysTruePortTester()
	{
		FreePortStrategy strategy = new RandomizedFreePortStrategy(ALWAYS_TRUE_PORT_TESTER, 1);
		int port = strategy.findFree(1, 1);
		assertEquals(port, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindFreeWithFromGreaterThanToGivesException()
	{
		FreePortStrategy strategy = new RandomizedFreePortStrategy(ALWAYS_FALSE_PORT_TESTER, 1);
		strategy.findFree(2, 1);
	}

	@Test
	public void testRandomPortsAreTested()
	{
		final int from = 1;
		final int to = 3;
		final int range = to - from;

		final LinkedList<Integer> integerSequence = new LinkedList<Integer>() {{
			add(0);
			add(1);
			add(2);
			add(0);
			add(1);
			add(2);
		}};

		Random random = new Random() {
			@Override
			public int nextInt(int bound) {
				assertEquals(range + 1, bound);
				assertFalse(integerSequence.isEmpty());
				return integerSequence.poll();
			}
		};

		final List<Integer> testedPorts = new ArrayList<>();
		PortTester portTester = new PortTester()
		{
			@Override
			public boolean isFree(int port)
			{
				testedPorts.add(port);
				return false;
			}
		};

		FreePortStrategy strategy = new RandomizedFreePortStrategy(portTester, integerSequence.size(), random);
		boolean caught = false;
		try
		{
			strategy.findFree(from, to);
		}
		catch (RuntimeException e)
		{
			caught = true;
		}
		assertTrue(caught);
		for (int i = 0; i < integerSequence.size(); ++i)
		{
			assertEquals(integerSequence.get(i) + from, (int) testedPorts.get(i));
		}
	}

}
