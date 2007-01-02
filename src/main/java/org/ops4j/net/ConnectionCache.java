/*
 * Copyright 2004 Stephen J. McConnell.
 * Copyright 2004 Niclas Hedhman
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

package org.ops4j.net;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.WeakHashMap;
import org.ops4j.monitors.TooManyMonitorsException;
import org.ops4j.monitors.exception.ExceptionMonitor;
import org.ops4j.monitors.exception.ExceptionSource;

public class ConnectionCache
    implements Runnable, ExceptionSource
{

    static private Object DUMMY = new Object();
    static private ConnectionCache m_instance;

    private HashMap<Object, Entry> m_hardStore;
    private WeakHashMap<URLConnection, Object> m_weakStore;

    private long m_timeToLive;
    private Thread m_thread;
    private ExceptionMonitor m_exceptionMonitor;

    static
    {
        m_instance = new ConnectionCache();
    }

    static public ConnectionCache getInstance()
    {
        return m_instance;
    }

    private ConnectionCache()
    {
        m_hardStore = new HashMap<Object, Entry>();
        m_weakStore = new WeakHashMap<URLConnection, Object>();
        m_timeToLive = 30000;
    }

    public URLConnection get( Object key )
    {
        synchronized( this ) // ensure no ConcurrentModificationException can occur.
        {
            Entry entry = m_hardStore.get( key );
            if( entry == null )
            {
                return null;
            }
            return entry.m_connection;
        }
    }

    public void put( Object key, URLConnection conn )
    {
        synchronized( this ) // ensure no ConcurrentModificationException can occur.
        {
            Entry entry = new Entry( conn );
            m_hardStore.put( key, entry );
            if( m_thread == null )
            {
                m_thread = new Thread( this, "ConnectionCache-cleaner" );
                m_thread.setDaemon( true );
                m_thread.start();
            }
        }
    }

    public void run()
    {
        while( true )
        {
            try
            {
                synchronized( this )
                {
                    long now = System.currentTimeMillis();
                    Iterator list = m_hardStore.values().iterator();
                    while( list.hasNext() )
                    {
                        Entry entry = (Entry) list.next();
                        if( entry.m_collectTime < now )
                        {
                            m_weakStore.put( entry.m_connection, DUMMY );
                            list.remove();
                        }
                    }
                    if( m_hardStore.size() == 0 )
                    {
                        m_thread = null;    // mark to start a new thread next time.
                        break;              // Exit the thread
                    }
                    wait( 10000 );
                }
            } catch( RuntimeException e )
            {
                if( m_exceptionMonitor != null )
                {
                    m_exceptionMonitor.exception( this, e );
                }
            } catch( InterruptedException e )
            {
                if( m_exceptionMonitor != null )
                {
                    m_exceptionMonitor.exception( this, e );
                }
            }
        }
    }

    /**
     * Register a ExceptionMonitor with the source.
     *
     * @requires HansaPermission "api.exceptionmonitor", "add"
     */
    public void registerExceptionMonitor( ExceptionMonitor monitor )
        throws TooManyMonitorsException
    {
        synchronized( this )
        {
            if( m_exceptionMonitor != null )
            {
                throw new TooManyMonitorsException( this, 1 );
            }
            m_exceptionMonitor = monitor;
        }
    }

    /**
     * Unregister a ExceptionMonitor with the source.
     *
     * @requires HansaPermission "api.exceptionmonitor", "remove"
     */
    public void unregisterExceptionMonitor( ExceptionMonitor monitor )
    {
        synchronized( this )
        {
            if( monitor == null || ! monitor.equals( m_exceptionMonitor ) )
            {
                return;
            }
            m_exceptionMonitor = null;
        }
    }

    private class Entry
    {

        private URLConnection m_connection;
        private long m_collectTime;

        Entry( URLConnection conn )
        {
            m_connection = conn;
            m_collectTime = System.currentTimeMillis() + m_timeToLive;
        }

        public boolean equals( Object obj )
        {
            if( obj == null )
            {
                return false;
            }
            if( obj.getClass().equals( Entry.class ) == false )
            {
                return false;
            }
            Entry other = (Entry) obj;

            if( m_connection.equals( other.m_connection ) == false )
            {
                return false;
            }

            return true;
        }

        public int hashCode()
        {
            return m_connection.hashCode();
        }

        public String toString()
        {
            return "Entry[" + m_connection + ", " + m_collectTime + "]";
        }
    }
}
