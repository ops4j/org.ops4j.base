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
package org.ops4j.monitors.stream;

import org.ops4j.monitors.TooManyMonitorsException;

/**
 * An interface for classes that supports StreamMonitors.
 */
public interface StreamSource
{

    /**
     * Register a ExceptionMonitor with the source.
     *
     * @param monitor The monitor to register.
     *
     * @throws TooManyMonitorsException if the StreamSource can not handle another registration.
     */
    void registerStreamMonitor( StreamMonitor monitor )
        throws TooManyMonitorsException;

    /**
     * Unregister a StreamMonitor with the source.
     *
     * @param monitor The monitor to register.
     */
    void unregisterStreamMonitor( StreamMonitor monitor );
}
