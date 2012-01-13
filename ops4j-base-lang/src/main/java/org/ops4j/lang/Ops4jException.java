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
package org.ops4j.lang;

/**
 * A base class for unchecked exceptions, to be used as a wrapper for third-party checked exceptions
 * or to be subclassed in other OPS4j projects.
 * 
 * @author Harald Wellmann
 * 
 */
public class Ops4jException extends RuntimeException
{

    private static final long serialVersionUID = 7318488956280074308L;

    public Ops4jException()
    {
        super();
    }

    public Ops4jException( String message )
    {
        super( message );
    }

    public Ops4jException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public Ops4jException( Throwable cause )
    {
        super( cause );
    }
}
