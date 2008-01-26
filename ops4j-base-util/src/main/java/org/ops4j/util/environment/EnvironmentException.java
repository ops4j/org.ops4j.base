/*
 * Copyright 2004 Stephen J. McConnell.
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

package org.ops4j.util.environment;

/**
 * A simple wrapper exception around exceptions that could occur while accessing
 * environment parameters.
 *
 * @author <a href="http://www.ops4j.org">Open Particpation Software for Java</a>
 * @version $Id$
 */
public class EnvironmentException extends RuntimeException
{

    /**
     * the environment variable name if available
     */
    private final String m_variable;

    /**
     * root cause
     */
    private final Throwable m_cause;

    /**
     * Creates an exception denoting a failure while attempting to access an
     * environment variable within an operating system and shell specific
     * environment that is caused by another exception.
     *
     * @param cause the underlying exception that caused the failure
     */
    EnvironmentException( final Throwable cause )
    {
        super();

        m_variable = null;
        m_cause = cause;
    }

    /**
     * Creates an exception denoting a failure while attempting to access an
     * environment variable within an operating system and shell specific
     * environment.
     *
     * @param message the reason for the access failure
     */
    EnvironmentException( final String message )
    {
        super( message );

        m_variable = null;
        m_cause = null;
    }

    /**
     * Creates an exception denoting a failure while attempting to access an
     * environment variable within an operating system and shell specific
     * environment that is caused by another exception.
     *
     * @param variable the variable whose value was to be accessed
     * @param cause    the underlying exception that caused the failure
     */
    EnvironmentException( final String variable, final Throwable cause )
    {
        super();

        m_variable = variable;
        m_cause = cause;
    }

    /**
     * Creates an exception denoting a failure while attempting to access an
     * environment variable within an operating system and shell specific
     * environment.
     *
     * @param variable the variable whose value was to be accessed
     * @param message  the reason for the access failure
     */
    EnvironmentException( final String variable, final String message )
    {
        super( message );

        m_variable = variable;
        m_cause = null;
    }

    /**
     * Gets the variable that was to be accessed.
     *
     * @return the value of the variable
     */
    public String getVariable()
    {
        return m_variable;
    }

    /**
     * Return the causal exception.
     *
     * @return the exception that caused this exception (possibly null)
     */
    public Throwable getCause()
    {
        return m_cause;
    }

    /**
     * Prepends variable name to the base message.
     *
     * @return the message of the Exception.
     *
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage()
    {
        String base = super.getMessage();

        if( null == base )
        {
            return "Failed to access " + m_variable + " environment variable";
        }

        return "Failed to access " + m_variable + " environment variable - " + base;
    }
}



