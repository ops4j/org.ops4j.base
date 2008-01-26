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

/**
 * PreConditionException is used to validate incoming arguments to methods and if not within the
 * supported ranges, throw an appropriate exception describing the problem.
 * <p/>
 * This Exception is used to make the code more robust, and ensure that clients use it as expected.
 */
public class PreConditionException extends RuntimeException
{

    /**
     * PreCondition constructor.
     *
     * @param message The composed error message.
     */
    public PreConditionException( String message )
    {
        super( message );
    }

    /**
     * Validates that the object is not null.
     *
     * @param object     The object to be validated.
     * @param identifier The name of the object.
     *
     * @throws PreConditionException if the object is null.
     */
    public static void validateNotNull( Object object, String identifier )
        throws PreConditionException
    {
        if( object == null )
        {
            throw new PreConditionException( identifier + " was NULL." );
        }
    }

    /**
     * Validates that the object is null.
     *
     * @param object     The object to be validated.
     * @param identifier The name of the object.
     *
     * @throws PreConditionException if the object is not null.
     */
    public static void validateNull( Object object, String identifier )
        throws PreConditionException
    {
        if( object == null )
        {
            return;
        }
        throw new PreConditionException( identifier + " was NOT NULL." );
    }

    /**
     * Validates that the value is greater than a limit.
     * <p/>
     * This method ensures that <code>value > limit</code>.
     *
     * @param identifier The name of the object.
     * @param limit      The limit that the value must exceed.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateGreaterThan( long value, long limit, String identifier )
        throws PreConditionException
    {
        if( value > limit )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not greater than " + limit + ". Was: " + value );
    }

    /**
     * Validates that the value is greater than a limit.
     * <p/>
     * This method ensures that <code>value > limit</code>.
     *
     * @param identifier The name of the object.
     * @param limit      The limit that the value must exceed.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateGreaterThan( double value, double limit, String identifier )
        throws PreConditionException
    {
        if( value > limit )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not greater than " + limit + ". Was: " + value );
    }

    /**
     * Validates that the value is greater than a limit.
     * <p/>
     * This method ensures that <code>value > limit</code>.
     *
     * @param identifier The name of the object.
     * @param limit      The limit that the value must exceed.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateGreaterThan( Number value, Number limit, String identifier )
        throws PreConditionException
    {
        if( value.doubleValue() > limit.doubleValue() )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not greater than " + limit + ". Was: " + value );
    }

    /**
     * Validates that the value is lesser than a limit.
     * <p/>
     * This method ensures that <code>value < limit</code>.
     *
     * @param identifier The name of the object.
     * @param limit      The limit that the value must be smaller than.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateLesserThan( long value, long limit, String identifier )
        throws PreConditionException
    {
        if( value < limit )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not lesser than " + limit + ". Was: " + value );
    }

    /**
     * Validates that the value is lesser than a limit.
     * <p/>
     * This method ensures that <code>value < limit</code>.
     *
     * @param identifier The name of the object.
     * @param limit      The limit that the value must be smaller than.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateLesserThan( double value, double limit, String identifier )
        throws PreConditionException
    {
        if( value < limit )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not lesser than " + limit + ". Was: " + value );
    }

    /**
     * Validates that the value is lesser than a limit.
     * <p/>
     * This method ensures that <code>value < limit</code>.
     *
     * @param identifier The name of the object.
     * @param limit      The limit that the value must be smaller than.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateLesserThan( Number value, Number limit, String identifier )
        throws PreConditionException
    {
        if( value.doubleValue() < limit.doubleValue() )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not lesser than " + limit + ". Was: " + value );
    }

    /**
     * Validates that the value under test is a particular value.
     * <p/>
     * This method ensures that <code>value == condition</code>.
     *
     * @param identifier The name of the object.
     * @param condition  The condition value.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateEqualTo( long value, long condition, String identifier )
        throws PreConditionException
    {
        if( value == condition )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not equal to " + condition + ". Was: " + value );
    }

    /**
     * Validates that the value under test is a particular value.
     * <p/>
     * This method ensures that <code>value == condition</code>.
     *
     * @param identifier The name of the object.
     * @param condition  The condition value.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateEqualTo( double value, double condition, String identifier )
        throws PreConditionException
    {
        if( value == condition )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not equal to " + condition + ". Was: " + value );
    }

    /**
     * Validates that the value under test is a particular value.
     * <p/>
     * This method ensures that <code>value == condition</code>.
     *
     * @param identifier The name of the object.
     * @param condition  The condition value.
     * @param value      The value to be tested.
     *
     * @throws PreConditionException if the condition is not met.
     */
    public static void validateEqualTo( Number value, Number condition, String identifier )
        throws PreConditionException
    {
        if( value.doubleValue() == condition.doubleValue() )
        {
            return;
        }
        throw new PreConditionException( identifier + " was not equal to " + condition + ". Was: " + value );
    }
}
