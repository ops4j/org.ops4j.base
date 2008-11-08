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

/**
 * Pipes asynchronously transfer data between process and system streams.
 */
public class Pipe
{
    private static final int READ_BUF_SIZE = 8192;

    /*
     * Background: we need to call read() on the System.in stream otherwise the
     * system console doesn't echo any characters back when the user is typing.
     * Unfortunately read() is a blocking call, which makes it really hard to
     * shutdown the pipe thread so that a new pipe thread can take its place.
     * 
     * The solution here is to cache pipe threads based on their input stream.
     * Each thread can then be switched to pump data to the appropriate output
     * stream, by using the old start() and stop() methods.
     * 
     * This means we don't have to worry about the blocking read() because the
     * thread will be re-used when new data appears. When streams are closed,
     * the relevant thread will break out of the read() and safely shutdown.
     */
    static final Map<InputStream, Pump> PUMPS = new WeakHashMap<InputStream, Pump>();

    private final InputStream m_in;
    private final OutputStream m_out;

    private Pump m_pump;

    /**
     * Create an incoming pipe, from the external process to us.
     * 
     * @param processStream process source
     * @param systemStream system sink
     */
    public Pipe( final InputStream processStream, final OutputStream systemStream )
    {
        m_in = processStream;
        m_out = systemStream;
    }

    /**
     * Create an outgoing pipe, from us to the external process.
     * 
     * @param processStream process sink
     * @param systemStream system source
     */
    public Pipe( final OutputStream processStream, final InputStream systemStream )
    {
        m_in = systemStream;
        m_out = processStream;
    }

    /**
     * Start piping data from input to output.
     * 
     * @param name pipe name
     * @return pipe instance
     */
    public synchronized Pipe start( final String name )
    {
        if( null == m_pump && null != m_in && null != m_out )
        {
            // might re-use a pump
            m_pump = startPump( m_in );
            m_pump.setName( name );
            m_pump.connect( m_out );
        }
        return this;
    }

    /**
     * Stop piping data from input to output.
     */
    public synchronized void stop()
    {
        if( null != m_pump )
        {
            // disconnect pump
            m_pump.connect( null );
            m_pump.setName( m_pump.getName() + " (disconnected)" );
            m_pump = null;
        }
    }

    private static class Pump extends Thread
    {
        // fixed input, but variable output
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
                notify(); // wake-up pump
            }
        }

        @Override
        public void run()
        {
            try
            {
                final byte[] buf = new byte[READ_BUF_SIZE];

                // check the input is still OK to read
                while( null != validate( m_source ) )
                {
                    // this call might block...
                    final int n = m_source.read( buf );
                    if( n == -1 )
                    {
                        break; // end-of-file
                    }

                    synchronized( this )
                    {
                        // check output is still OK to write
                        while( null == validate( m_sink ) )
                        {
                            wait(); // disconnected, so save data and wait
                        }

                        // pump out saved data
                        m_sink.write( buf, 0, n );
                        m_sink.flush();
                    }
                }
            }
            catch( final IOException e )
            {
                if( null != m_sink )
                {
                    // only report if connected
                    e.printStackTrace();
                }
            }
            catch( final Exception e )
            {
                // ignore spurious exceptions
            }
            finally
            {
                synchronized( PUMPS )
                {
                    // stopping, so remove ourselves
                    if( PUMPS.get( m_source ) == this )
                    {
                        PUMPS.remove( m_source );
                    }
                }
            }
        }
    }

    static InputStream validate( final InputStream is )
    {
        try
        {
            is.available(); // test input stream without disturbing content
            return is;
        }
        catch( final Exception e )
        {
            return null;
        }
    }

    static OutputStream validate( final OutputStream os )
    {
        try
        {
            os.flush(); // test output stream without disturbing content
            return os;
        }
        catch( final Exception e )
        {
            return null;
        }
    }

    static Pump startPump( final InputStream is )
    {
        synchronized( PUMPS )
        {
            // find if stream already has a pump
            Pump pump = PUMPS.get( is );
            if( null == pump )
            {
                // new stream needs pump thread
                pump = new Pump( is );
                pump.setDaemon( true );
                pump.start();

                PUMPS.put( is, pump );
            }
            return pump;
        }
    }
}
