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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;

/** The WriterOutputStream wraps a java.io.Writer and sends the stream bytes to that Writer using
 * a given encoding.
 * <p>
 * This class is effectively the reverse of the java.io.OutputStreamWriter.
 * </p>
 */
public class WriterOutputStream extends OutputStream
{

    private InputStreamReader m_converter;
    private PipedOutputStream m_pipeOut;
    private Writer m_writer;

    public WriterOutputStream( Writer writer, String encoding )
        throws IOException
    {
        m_writer = writer;
        PipedInputStream pipeIn = new PipedInputStream();
        m_converter = new InputStreamReader( pipeIn, encoding );
        m_pipeOut = new PipedOutputStream( pipeIn );
    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param b the <code>byte</code>.
     *
     * @throws java.io.IOException if an I/O error occurs. In particular,
     *                             an <code>IOException</code> may be thrown if the
     *                             output stream has been closed.
     */
    public void write( int b )
        throws IOException
    {
        m_pipeOut.write( b );
        m_pipeOut.flush();
        if( m_converter.ready() )
        {
            int character = m_converter.read();
            m_writer.write( character );
        }
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with this stream. The general contract of <code>close</code>
     * is that it closes the output stream. A closed stream cannot perform
     * output operations and cannot be reopened.
     * <p>
     * The <code>close</code> method of <code>OutputStream</code> does nothing.
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
            m_pipeOut.close();
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
