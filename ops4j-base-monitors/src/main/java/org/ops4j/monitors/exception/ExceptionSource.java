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

package org.ops4j.monitors.exception;

import java.util.List;
import org.ops4j.monitors.TooManyMonitorsException;

/**
 * An interface for classes that supports ExceptionMonitors.
 */
public interface ExceptionSource
{

    /**
     * Register a ExceptionMonitor with the source.
     *
     * @param monitor The ExceptionMonitor to register.
     *
     * @throws TooManyMonitorsException if the ExceptionSource is uncapable of handling any more registrations. If this
     *                                  Exception is thrown the client should unregister the existing monitor and
     *                                  replace it with a ExceptionMonitorRouter.
     */
    void registerExceptionMonitor( ExceptionMonitor monitor )
        throws TooManyMonitorsException;

    /**
     * Unregister a ExceptionMonitor with the source.
     *
     * @param monitor The ExceptionMonitor to unregister.
     */
    void unregisterExceptionMonitor( ExceptionMonitor monitor );

    /**
     * Returns all ExceptionMonitors that are registered.
     *
     * @return all ExceptionMonitors that are registered.
     */
    List<ExceptionMonitor> getExceptionMonitors();
}
