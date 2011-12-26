/*
 * Copyright 2011 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.spi;

/**
 * An exception thrown when a unique service provider is expected, but more than one provider is
 * found.
 * 
 * @author Harald Wellmann
 * 
 */
public class NonUniqueServiceProviderException extends RuntimeException
{
    private static final long serialVersionUID = -2500275828102837801L;

    public NonUniqueServiceProviderException()
    {
        // empty
    }

    public NonUniqueServiceProviderException( String message )
    {
        super( message );
    }

    public NonUniqueServiceProviderException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public NonUniqueServiceProviderException( Throwable cause )
    {
        super( cause );
    }
}
