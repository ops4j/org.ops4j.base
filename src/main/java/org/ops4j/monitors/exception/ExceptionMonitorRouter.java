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

package org.ops4j.monitors.exception;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A monitor of a Exceptions occuring, and capability to route/delegate these
 * events to 0..n registered ExceptionMonitor implementations.
 *
 * The purpose of this class is to support that many monitors are registered to the
 * same Exception source.
 *
 * @author <a href="http://www.ops4j.org">Open Particpation Software for Java</a>
 * @version $Id$
 */
public class ExceptionMonitorRouter
    implements ExceptionMonitor, ExceptionSource
{
    /**
     * List of attached monitors.
     */
     private ArrayList m_Monitors;

   /**
    * Creation of a exception monitor router.
    */
    public ExceptionMonitorRouter()
    {
        m_Monitors = new ArrayList();
    }

    /** This method is called when an Exception or Throwable occurs.
     *
     */
    public void exception( ExceptionSource source, Throwable exception )
    {
        synchronized( m_Monitors )
        {
            Iterator list = m_Monitors.iterator();
            while( list.hasNext() )
            {
                ExceptionMonitor monitor = (ExceptionMonitor) list.next();
                monitor.exception( source, exception );
            }
        }
    }

   /**
    * Add a monitor to the list of monitors managed by this router.
    * @param monitor the monitor to add
    */
    public void registerExceptionMonitor( ExceptionMonitor monitor )
    {
        synchronized( this )
        {
            m_Monitors.add( monitor );
        }
    }

    /**
     * Remove a monitor to the list of monitors managed by this router.
     * @param monitor the monitor to add
     */
     public void unregisterExceptionMonitor( ExceptionMonitor monitor )
     {
         synchronized( this )
         {
             m_Monitors.remove( monitor );
         }
     }

    /** Returns the number of registered ExceptionMonitors.
     *
     */
    public int size()
    {
        synchronized( this )
        {
            return m_Monitors.size();
        }
    }

    /** Returns the ExceptionMonitor at a particular index.
     *
     */
    public ExceptionMonitor getMonitor( int index )
    {
        synchronized( this )
        {
            return (ExceptionMonitor) m_Monitors.get( index );
        }
    }
}


