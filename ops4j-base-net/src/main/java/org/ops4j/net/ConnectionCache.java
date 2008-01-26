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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import org.ops4j.monitors.TooManyMonitorsException;
import org.ops4j.monitors.exception.ExceptionMonitor;
import org.ops4j.monitors.exception.ExceptionSource;

/**
 * This class is to be used to cache URLConnections.
 */
public final class ConnectionCache
    implements Runnable, ExceptionSource
{

    /**
     * Dummy Object.
     */
    private static final Object DUMMY = new Object();

    /**
     * Instance
     */
    private static ConnectionCache m_instance;

    private HashMap<Object, Entry> m_hardStore;
    private WeakHashMap<URLConnection, Object> m_weakStore;

    private long m_timeToLive;
    private Thread m_thread;
    private ExceptionMonitor m_exceptionMonitor;

    static
    {
        m_instance = new ConnectionCache();
    }

    /**
     * Private constructor to ensure singleton.
     */
    private ConnectionCache()
    {
        m_hardStore = new HashMap<Object, Entry>();
        m_weakStore = new WeakHashMap<URLConnection, Object>();
        m_timeToLive = 30000;
    }

    /**
     * Returns the singleton instance.
     *
     * @return the singleton instance.
     */
    public static ConnectionCache getInstance()
    {
        return m_instance;
    }

    /**
     * Returns the URLConnection associated with the given key.
     *
     * @param key The key that is associated to the URL.
     *
     * @return the URLConnection associated with the given key.
     */
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

    /**
     * Stores a URLConnection in association with a key.
     *
     * @param key  The key that is associated to the URLConnection.
     * @param conn The URLConnection that should be stored in association with the key.
     */
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

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run()
    {
        while( true )
        {
            try
            {
                synchronized( this )
                {
                    if( mainLoop() )
                    {
                        break;              // Exit the thread
                    }
                }
            }
            catch( RuntimeException e )
            {
                if( m_exceptionMonitor != null )
                {
                    m_exceptionMonitor.exception( this, e );
                }
            }
            catch( InterruptedException e )
            {
                if( m_exceptionMonitor != null )
                {
                    m_exceptionMonitor.exception( this, e );
                }
            }
        }
    }

    /**
     * The main loop of the cache, which checks for expirations.
     *
     * @return true if the thread should be stopped.
     *
     * @throws InterruptedException if the thread was interrupted while blocking.
     */
    private boolean mainLoop()
        throws InterruptedException
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
            return true;
        }
        wait( 10000 );
        return false;
    }

    /**
     * Register a ExceptionMonitor with the source.
     *
     * @param monitor The ExceptionMonitor to be notified.
     *
     * @throws TooManyMonitorsException If more than one monitor is registered.
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
     * @param monitor The ExceptionMonitor to be unregistered.
     *
     * @requires HansaPermission "api.exceptionmonitor", "remove"
     */
    public void unregisterExceptionMonitor( ExceptionMonitor monitor )
    {
        synchronized( this )
        {
            if( monitor == null || !monitor.equals( m_exceptionMonitor ) )
            {
                return;
            }
            m_exceptionMonitor = null;
        }
    }

    /**
     * Returns all ExceptionMonitors that are registered.
     *
     * @return all ExceptionMonitors that are registered.
     *
     * @requires HansaPermission "api.exceptionmonitor", "get"
     */
    public List<ExceptionMonitor> getExceptionMonitors()
    {
        synchronized( this )
        {
            ArrayList<ExceptionMonitor> result = new ArrayList<ExceptionMonitor>();
            result.add( m_exceptionMonitor );
            return result;
        }
    }

    /**
     * The Entry class is used to tag the URLConenction with a "collection time", when it can
     * be removed from the cache.
     */
    private class Entry
    {

        private URLConnection m_connection;
        private long m_collectTime;

        /**
         * Constructor for an entry.
         *
         * @param conn The URLConnection to be placed in the Entry.
         */
        Entry( URLConnection conn )
        {
            m_connection = conn;
            m_collectTime = System.currentTimeMillis() + m_timeToLive;
        }

        /**
         * Checks for equality.
         *
         * @param obj The object to compare with.
         *
         * @return true if the compared object is semantically identical to this one.
         */
        @Override
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

        /**
         * Computes the hashCode for the Entry.
         *
         * @return the hashCode of the Entry.
         */
        @Override
        public int hashCode()
        {
            return m_connection.hashCode();
        }

        /**
         * Converts the Entry to a String.
         *
         * @return The string representation of this Entry.
         */
        @Override
        public String toString()
        {
            return "Entry[" + m_connection + ", " + m_collectTime + "]";
        }
    }
}
