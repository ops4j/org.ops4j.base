/*
 * Copyright 2004 Stephen J. McConnell.
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

/**
 * A monitor of a download activity or activities.
 *
 * @author <a href="http://www.ops4j.org">Open Particpation Software for Java</a>
 * @version $Id$
 */
public interface StreamMonitor
{

    /**
     * Notify the monitor of the update in the download status.
     *
     * @param resource the name of the remote resource being downloaded.
     * @param expected the expected number of bytes to be downloaded.
     * @param count    the number of bytes downloaded.
     */
    void notifyUpdate( URL resource, int expected, int count );

    /**
     * Notify the monitor of the successful completion of a download
     * process.
     *
     * @param resource the name of the remote resource.
     */
    void notifyCompletion( URL resource );

    /**
     * Notify the monitor of the an error during the download
     * process.
     *
     * @param resource the name of the remote resource.
     * @param message  a non-localized message describing the problem in english.
     */
    void notifyError( URL resource, String message );
}

