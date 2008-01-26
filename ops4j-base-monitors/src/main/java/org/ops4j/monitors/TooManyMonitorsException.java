/*
 * Copyright 2006 Niclas Hedhman.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.monitors;

import org.ops4j.monitors.exception.ExceptionSource;

/**
 * This Exception is thrown when the MonitorSource can not register any
 * more Monitors.
 */
public class TooManyMonitorsException extends MonitorException
{

    private final int m_max;
    private final ExceptionSource m_source;

    /**
     * Constructor
     *
     * @param source The ExceptionSource,
     * @param max    The maximum number of monitors that can be registered at the ExceptionSource.
     */
    public TooManyMonitorsException( ExceptionSource source, int max )
    {
        super( "Too many Monitors being registered. Only " + max + " monitors are allowed.", null );
        m_max = max;
        m_source = source;
    }

    /**
     * Returns he maximum number of monitors that can be registered at the ExceptionSource.
     *
     * @return The maximum number of monitors that can be registered at the ExceptionSource.
     */
    public int getMaxMonitorsAllowed()
    {
        return m_max;
    }

    /**
     * Returns the ExceptionSource that can't deal with too many ExceptionMonitors.
     *
     * @return The ExceptionSource unable to cope.
     */
    public ExceptionSource getExceptionSource()
    {
        return m_source;
    }
}
