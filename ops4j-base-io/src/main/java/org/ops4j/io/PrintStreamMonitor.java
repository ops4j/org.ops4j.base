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

import java.io.PrintStream;
import java.net.URL;
import org.ops4j.monitors.stream.StreamMonitor;

/**
 * PrintStreamMonitor is a convenience implementation of the StreamMonitor for output to
 * a PrintStream, such as <code>System.out</code>.
 * <p/>
 * <p/>
 * Example of usage;
 * <pre><code>
 *      StreamMonitor monitor = new PrintStreamMonitor( System.out );
 *      StreamUtils.copyStream( monitor, sourceURL, length, srcStream, destStream, true );
 * </code></pre>
 * </p>
 */
public class PrintStreamMonitor
    implements StreamMonitor
{

    private boolean m_first = true;
    private int m_expected;
    private long m_start;
    private PrintStream m_out;

    /**
     * @param out The PrintStream that should receive the monitored output.
     */
    public PrintStreamMonitor( PrintStream out )
    {
        m_out = out;
    }

    /**
     * Notify the monitor of the update in the download status.
     *
     * @param resource the name of the remote resource being downloaded.
     * @param expected the expected number of bytes to be downloaded.
     * @param count    the number of bytes downloaded.
     */
    public void notifyUpdate( URL resource, int expected, int count )
    {
        if( m_first )
        {
            m_expected = expected;
            m_start = System.currentTimeMillis();
            m_first = false;
        }
        int completed = ( count * 100 ) / expected;
        m_out.print( resource.toExternalForm() + " : " + completed + "%    \r" );
    }

    /**
     * Notify the monitor of the successful completion of a download
     * process.
     *
     * @param resource the name of the remote resource.
     */
    public void notifyCompletion( URL resource )
    {
        long now = System.currentTimeMillis();
        long time = now - m_start;
        int kBps = (int) ( m_expected / time );
        m_out.println( resource.toExternalForm() + " : " + kBps + " kBps.          " );
    }

    /**
     * Notify the monitor of the an error during the download
     * process.
     *
     * @param resource the name of the remote resource.
     * @param message  a non-localized message describing the problem in english.
     */
    public void notifyError( URL resource, String message )
    {
        m_out.println( resource.toExternalForm() + " : " + message );
    }
}
