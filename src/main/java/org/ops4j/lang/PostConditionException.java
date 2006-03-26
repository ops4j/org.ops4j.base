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
package org.ops4j.lang;

public class PostConditionException extends RuntimeException
{

    /** PostCondition constructor.
     *
     * @param message The composed error message.
     */
    public PostConditionException( String message )
    {
        super( message );
    }

    public static void validateNotNull( Object object, String identifier )
    {
        if( object == null )
        {
            throw new PostConditionException( identifier + " was NULL." );
        }
    }

    public static void validateNull( Object object, String identifier )
    {
        if( object == null )
        {
            return;
        }
        throw new PostConditionException( identifier + " was NOT NULL." );
    }

    public static void validateGreaterThan( long value, long limit, String identifier )
    {
        if( value > limit )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not greater than " + limit + ". Was: " + value );
    }

    public static void validateGreaterThan( double value, double limit, String identifier )
    {
        if( value > limit )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not greater than " + limit + ". Was: " + value );
    }

    public static void validateGreaterThan( Number value, Number limit, String identifier )
    {
        if( value.doubleValue() > limit.doubleValue() )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not greater than " + limit + ". Was: " + value );
    }

    public static void validateLesserThan( long value, long limit, String identifier )
    {
        if( value < limit )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not lesser than " + limit + ". Was: " + value );
    }

    public static void validateLesserThan( double value, double limit, String identifier )
    {
        if( value < limit )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not lesser than " + limit + ". Was: " + value );
    }

    public static void validateLesserThan( Number value, Number limit, String identifier )
    {
        if( value.doubleValue() < limit.doubleValue() )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not lesser than " + limit + ". Was: " + value );
    }

    public static void validateEqualTo( long value, long limit, String identifier )
    {
        if( value == limit )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not equal to " + limit + ". Was: " + value );
    }

    public static void validateEqualTo( double value, double limit, String identifier )
    {
        if( value == limit )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not equal to " + limit + ". Was: " + value );
    }

    public static void validateEqualTo( Number value, Number limit, String identifier )
    {
        if( value.doubleValue() == limit.doubleValue() )
        {
            return;
        }
        throw new PostConditionException( identifier + " was not equal to " + limit + ". Was: " + value );
    }

}
