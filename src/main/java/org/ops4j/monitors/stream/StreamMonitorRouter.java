/*
 * Copyright 2004 Niclas Hedhman.
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

package org.ops4j.monitors.stream;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A router that handles multicasr distribution of monitor events to registered monitors.
 * @author <a href="http://www.ops4j.org">Open Particpation Software for Java</a>
 * @version $Id$
 */
public class StreamMonitorRouter
    implements StreamMonitor
{
    /**
     * List of attached monitors.
     */
     private ArrayList m_Monitors;

    //--------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------

   /**
    * Creation of a new network monitor router.
    */
    public StreamMonitorRouter()
    {
        m_Monitors = new ArrayList();
    }

    //--------------------------------------------------------------------
    // StreamMonitor
    //--------------------------------------------------------------------

   /**
    * Notify all subscribing monitors of a updated event.
    * @param resource the url of the updated resource
    * @param expected the size in bytes of the download
    * @param count the progress in bytes
    */
    public void notifyUpdate( URL resource, int expected, int count )
    {
        synchronized( m_Monitors )
        {
            Iterator list = m_Monitors.iterator();
            while( list.hasNext() )
            {
                StreamMonitor monitor = (StreamMonitor) list.next();
                monitor.notifyUpdate( resource, expected, count );
            }
        }
    }

   /**
    * Notify all subscribing monitors of a download completion event.
    * @param resource the url of the downloaded resource
    */
    public void notifyCompletion( URL resource )
    {
        synchronized( m_Monitors )
        {
            Iterator list = m_Monitors.iterator();
            while( list.hasNext() )
            {
                StreamMonitor monitor = (StreamMonitor) list.next();
                monitor.notifyCompletion( resource );
            }
        }
    }

    /**
     * Notify the monitor of the an error during the download
     * process.
     * @param resource the name of the remote resource.
     * @param message a non-localized message describing the problem in english.
     */
    public void notifyError( URL resource, String message )
    {
        synchronized( m_Monitors )
        {
            Iterator list = m_Monitors.iterator();
            while( list.hasNext() )
            {
                StreamMonitor monitor = (StreamMonitor) list.next();
                monitor.notifyError( resource, message );
            }
        }
    }

   /**
    * Add a monitor to the list of monitors managed by this router.
    * @param monitor the monitor to add
    */
    public void addStreamMonitor( StreamMonitor monitor )
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
     public void removeStreamMonitor( StreamMonitor monitor )
     {
         synchronized( this )
         {
             m_Monitors.remove( monitor );
         }
     }

    public int size()
    {
        synchronized( this )
        {
            return m_Monitors.size();
        }
    }

    public StreamMonitor getMonitor( int index )
    {
        synchronized( this )
        {
            return (StreamMonitor) m_Monitors.get( index );
        }
    }
}

