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
package org.ops4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

/** The ReaderInputStream wraps a Reader and and provides its characters as InputStream data
 * of a given encoding.
 * <p>
 * The class is effectively the reverse of the java.io.InputStreamReader.
 * </p>
 */
public class ReaderInputStream extends InputStream
{

    private Reader m_reader;
    private OutputStreamWriter m_converter;
    private PipedInputStream m_pipeIn;

    public ReaderInputStream( Reader reader, String encoding )
        throws IOException
    {
        m_reader = reader;
        PipedOutputStream pipeOut = new PipedOutputStream();
        m_converter = new OutputStreamWriter( pipeOut, encoding );
        m_pipeIn = new PipedInputStream( pipeOut );
    }

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     *
     * <p> A subclass must provide an implementation of this method.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     *         stream is reached.
     *
     * @throws java.io.IOException if an I/O error occurs.
     */
    public int read()
        throws IOException
    {
        int b = m_reader.read();
        if( b >= 0 )
        {
            m_converter.write( b );
            m_converter.flush();
        }
        if( m_pipeIn.available() <= 0 )
        {
            return -1;
        }
        return m_pipeIn.read();
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     *
     * <p> The <code>close</code> method of <code>InputStream</code> does
     * nothing.
     *
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void close()
        throws IOException
    {
        IOException throwable = null;
        try
        {
            super.close();
        } catch( IOException e )
        {
            throwable = e;
        }
        try
        {
            m_pipeIn.close();
        } catch( IOException e )
        {
            throwable = e;
        }
        try
        {
            m_converter.close();
        } catch( IOException e )
        {
            throwable = e;
        }
        if( throwable != null )
        {
            throw throwable;
        }
    }
}
