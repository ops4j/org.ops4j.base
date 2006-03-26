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

/** This Exception is thrown when the MonitorSource can not register any
 * more Monitors.
 */
public class TooManyMonitorsException extends MonitorException
{
    private int m_Max;
    private Object m_Source;

    public TooManyMonitorsException( Object source, int max )
    {
        super( "Too many Monitors being registered. Only " + max + " monitors are allowed.", null );
        m_Max = max;
        m_Source = source;
    }

    public int getMaxMonitorsAllowed()
    {
        return m_Max;
    }

    public Object getSource()
    {
        return m_Source;
    }
}
