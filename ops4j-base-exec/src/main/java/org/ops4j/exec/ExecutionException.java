/*
 * Copyright 2007 Alin Dreghiciu.
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
package org.ops4j.exec;

/**
 * Thrown to indicate an exception during running of the platform.
 *
 * @author Alin Dreghiciu
 * @author Harald Wellmann
 * @since August 19, 2007
 */
public class ExecutionException
    extends RuntimeException
{
    private static final long serialVersionUID = 4601658364559556917L;

    public ExecutionException()
    {
        // empty
    }
    
    /**
     * @param message The exception message.
     *
     * @see Exception#Exception(String)
     */
    public ExecutionException( String message )
    {
        super( message );
    }

    /**
     * @param message The exception message.
     * @param cause   The original cause of this exception.
     *
     * @see Exception#Exception(String,Throwable)
     */
    public ExecutionException( String message, Throwable cause )
    {
        super( message, cause );
    }
    /**
     * @param cause   The original cause of this exception.
     *
     * @see Exception#Exception(String,Throwable)
     */
    public ExecutionException( Throwable cause )
    {
        super( cause );
    }
}
