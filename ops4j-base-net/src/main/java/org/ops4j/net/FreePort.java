/*
 * Copyright 2008 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.net;

/**
 * Find a not-taken port on localhost.
 * First call to getPort will try to find a free port in range given by contructor.
 * Each subsequent call will return the same value.
 * Exception: If no port has been found (you'll get a RuntimeException on getPort) a subsequent call to getPort()
 * will search again.
 *
 * @author Toni Menzel (tonit)
 * @since Jul 25, 2008
 */
public class FreePort
{

    private final int m_from;
    private final int m_to;
    private final FreePortStrategy m_strategy;
    private int m_found = -1;

    /**
     * @param from Begin of range to search for free port (including)
     * @param to   End of range to search for free port (including)
     */
    public FreePort( int from, int to )
    {
        this(from, to, new LinearFreePortStrategy(new DefaultPortTester()));
    }

    /**
     * @param from Begin of range to search for free port (including)
     * @param to   End of range to search for free port (including)
     */
    public FreePort( int from, int to, FreePortStrategy strategy)
    {
        m_from = from;
        m_to = to;
        m_strategy = strategy;
    }


    /**
     * Finds a free socket upon first calll and returns the same for every next call.
     *
     * @return a free port (from first call)
     *
     * @throws RuntimeException if no port has been found. (TODO change this to an apropriate checked exception)
     */
    public int getPort()
    {
        if( m_found == -1 )
        {
            m_found = m_strategy.findFree(m_from, m_to);
        }
        return m_found;
    }

    /**
     * @return Human readably String representation
     */
    @Override
    public String toString()
    {
        return "FreePort[" + m_from + ", " + m_to + "] " + ( ( m_found != -1 )
                                                             ? "m_found: " + m_found
                                                             : "non m_found" );
    }

}
