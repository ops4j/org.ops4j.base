package org.ops4j.net;

public final class LinearFreePortStrategy implements FreePortStrategy {

    private final PortTester m_portTester;

    public LinearFreePortStrategy(PortTester portTester)
    {
        this.m_portTester = portTester;
    }

    public int findFree(int from, int to)
    {
        for(int i = from; i <= to; i++ )
        {
            if( m_portTester.isFree( i ) )
            {
                return i;
            }
        }
        throw new RuntimeException( "No free port in range " + from + ":" + to);
    }

}
