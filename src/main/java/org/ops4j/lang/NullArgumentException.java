/*
 * Copyright 2005 Niclas Hedhman.
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

package org.ops4j.lang;

import java.util.Properties;

/** Exception thrown when the argument to a method or constructor is
 *  <i>null</i> and not handled by the method/constructor/class.
 *
 * The argument in the only constructor of this exception should only
 * take the name of the declared argument that is null, for instance;
 * <code><pre>
 *     public Person( String name, int age )
 *     {
 *         NullArgumentException.validateNotEmpty( name, "name" )
 *         if( age > 120 )
 *             throw new IllegalArgumentException( "age > 120" );
 *         if( age < 0 )
 *             throw new IllegalArgumentException( "age < 0" );
 *     }
 * </pre></code>
 *
 * @author <a href="http://www.ops4j.org">Open Particpation Software for Java</a>
 * @version $Id$
 */
public class NullArgumentException extends IllegalArgumentException
{
    private static final long serialVersionUID = 1L;

    private static final String IS_NULL = " is null.";

    private static final String IS_EMPTY = " is empty string.";

    public NullArgumentException( String msg )
    {
        super( msg );
    }

    public static void validateNotNull( Object obj, String objectName )
    {
        if( obj == null )
        {
            throw new NullArgumentException( objectName + IS_NULL );
        }
    }

    public static void validateNotEmpty( String stringToCheck, String argumentName )
        throws NullArgumentException
    {
        validateNotNull( stringToCheck, argumentName );
        if( stringToCheck.length() == 0 )
        {
            throw new NullArgumentException( argumentName + IS_EMPTY );
        }
    }

    public static void validateNotEmpty( Properties propertiesToCheck, String argumentName )
        throws NullArgumentException
    {
        validateNotNull( propertiesToCheck, argumentName );
        if( propertiesToCheck.isEmpty() )
        {
            throw new NullArgumentException( argumentName + IS_EMPTY );
        }
    }
}