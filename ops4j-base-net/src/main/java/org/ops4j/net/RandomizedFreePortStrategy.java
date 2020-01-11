package org.ops4j.net;

import java.util.Objects;
import java.util.Random;

public final class RandomizedFreePortStrategy implements FreePortStrategy {

	private final PortTester m_portTester;
	private final int m_attempts;
    private final Random m_random;

	public RandomizedFreePortStrategy(PortTester portTester)
	{
		this(portTester, 50);
	}

	public RandomizedFreePortStrategy( PortTester portTester, int attempts)
	{
		this(portTester, attempts, new Random());
	}

	public RandomizedFreePortStrategy( PortTester portTester, int attempts, Random random)
	{
		Objects.requireNonNull(portTester);
		if (attempts <= 0) {
			throw new IllegalArgumentException("attempts must be greater than 0");
		}
		this.m_portTester = portTester;
		this.m_attempts = attempts;
		this.m_random = random;
	}

    public int findFree(int from, int to)
    {
    	if (from > to) {
    		throw new IllegalArgumentException();
		}
    	int range = to - from;
    	if (range == 0)
    	{
			if (m_portTester.isFree(from))
			{
				return from;
			}
		} else
			{
			for (int attempt = 0; attempt < m_attempts; ++attempt)
			{
				int candidate = from + (m_random.nextInt(range + 1));
				if (m_portTester.isFree(candidate))
				{
					return candidate;
				}
			}
		}
        throw new RuntimeException( "No free port found in range " + from + ":" + to + " after " + m_attempts + " attempts");
    }
}
