/*
 * Copyright 2008 Stuart McCulloch.
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
package org.ops4j.io;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.WeakHashMap;

public class Pipe
{
    private static final int READ_BUF_SIZE = 8192;

    private static final Map<InputStream, Pump> PUMPS = new WeakHashMap<InputStream, Pump>();

    private final InputStream m_in;
    private final OutputStream m_out;

    private Pump m_pump;

    public Pipe( final InputStream processStream, final OutputStream systemStream )
    {
        m_in = processStream;
        m_out = systemStream;
    }

    public Pipe( final OutputStream processStream, final InputStream systemStream )
    {
        m_in = systemStream;
        m_out = processStream;
    }

    public synchronized Pipe start( final String name )
    {
        if( null == m_pump && null != m_in && null != m_out )
        {
            m_pump = startPump( m_in );
            m_pump.setName( name );
            m_pump.connect( m_out );
        }
        return this;
    }

    public synchronized void stop()
    {
        if( null != m_pump )
        {
            m_pump.connect( null );
            m_pump.setName( m_pump.getName() + " (disconnected)" );
            m_pump = null;
        }
    }

    private static class Pump extends Thread
    {
        private final InputStream m_source;
        private OutputStream m_sink;

        public Pump( final InputStream source )
        {
            m_source = source;
        }

        public void connect( final OutputStream sink )
        {
            synchronized( this )
            {
                m_sink = sink;
                notify();
            }
        }

        @Override
        public void run()
        {
            try
            {
                final byte[] buf = new byte[READ_BUF_SIZE];

                while( null != validate( m_source ) )
                {
                    final int n = m_source.read( buf );
                    if( n == -1 )
                    {
                        break;
                    }

                    synchronized( this )
                    {
                        while( null == validate( m_sink ) )
                        {
                            wait();
                        }

                        m_sink.write( buf, 0, n );
                        m_sink.flush();
                    }
                }
            }
            catch( final IOException e )
            {
                e.printStackTrace();
            }
            catch( final InterruptedException e )
            {
                // do nothing
            }
        }
    }

    static InputStream validate( final InputStream is )
    {
        try
        {
            is.available();
            return is;
        }
        catch( final IOException e )
        {
            return null;
        }
    }

    static OutputStream validate( final OutputStream os )
    {
        try
        {
            os.flush();
            return os;
        }
        catch( final IOException e )
        {
            return null;
        }
    }

    static Pump startPump( final InputStream is )
    {
        synchronized( PUMPS )
        {
            Pump pump = PUMPS.get( is );
            if( null == pump )
            {
                pump = new Pump( is );
                pump.setDaemon( true );
                pump.start();

                PUMPS.put( is, pump );
            }
            return pump;
        }
    }
}
