/*
 * Copyright 2004 Stephen J. McConnell.
 * Copyright 1999-2004 The Apache Software Foundation
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
package org.ops4j.util.i18n;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * A class to simplify extracting localized strings, icons
 * and other common resources from a ResourceBundle.
 * <p/>
 * Reworked to mirror behaviour of StringManager from Tomcat (format() to getString()).
 *
 * @author <a href="http://www.ops4j.org">Open Participation Software for Java</a>
 */
public final class Resources
{

    /**
     * Span width constant.
     */
    private static final int SPAN = 100;

    /**
     * Random seed.
     */
    private static final Random RANDOM = new Random();

    /**
     * Locale of Resources
     */
    private final Locale m_locale;

    /**
     * Resource bundle referenced by manager
     */
    private ResourceBundle m_bundle;

    /**
     * Base name of resource bundle
     */
    private String m_baseName;

    /**
     * ClassLoader from which to load resources
     */
    private ClassLoader m_classLoader;

    /**
     * Constructor that builds a manager in default locale.
     *
     * @param baseName the base name of ResourceBundle
     */
    public Resources( String baseName )
    {
        this( baseName, Locale.getDefault(), null );
    }

    /**
     * Constructor that builds a manager in default locale
     * using specified ClassLoader.
     *
     * @param baseName    the base name of ResourceBundle
     * @param classLoader the classLoader to load ResourceBundle from
     */
    public Resources( String baseName, ClassLoader classLoader )
    {
        this( baseName, Locale.getDefault(), classLoader );
    }

    /**
     * Constructor that builds a manager in specified locale.
     *
     * @param baseName the base name of ResourceBundle
     * @param locale   the Locale for resource bundle
     */
    public Resources( String baseName, Locale locale )
    {
        this( baseName, locale, null );
    }

    /**
     * Constructor that builds a manager in specified locale.
     *
     * @param baseName    the base name of ResourceBundle
     * @param locale      the Locale for resource bundle
     * @param classLoader the classLoader to load ResourceBundle from
     */
    public Resources( String baseName, Locale locale, ClassLoader classLoader )
    {
        if( null == baseName )
        {
            throw new NullPointerException( "baseName property is null" );
        }
        if( null == locale )
        {
            throw new NullPointerException( "locale property is null" );
        }
        m_baseName = baseName;
        m_locale = locale;
        m_classLoader = classLoader;
    }

    /**
     * Retrieve a boolean from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource boolean
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public boolean getBoolean( String key, boolean defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getBoolean( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a boolean from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource boolean
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public boolean getBoolean( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        return "true".equalsIgnoreCase( value );
    }

    /**
     * Retrieve a byte from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource byte
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public byte getByte( String key, byte defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getByte( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a byte from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource byte
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public byte getByte( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            return Byte.parseByte( value );
        }
        catch( NumberFormatException nfe )
        {
            throw new MissingResourceException( "Expecting a byte value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a char from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource char
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public char getChar( String key, char defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getChar( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a char from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource char
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public char getChar( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );

        if( 1 == value.length() )
        {
            return value.charAt( 0 );
        }
        else
        {
            throw new MissingResourceException( "Expecting a char value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a short from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource short
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public short getShort( String key, short defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getShort( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a short from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource short
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public short getShort( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            return Short.parseShort( value );
        }
        catch( NumberFormatException nfe )
        {
            throw new MissingResourceException( "Expecting a short value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a integer from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource integer
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public int getInteger( String key, int defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getInteger( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a integer from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource integer
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public int getInteger( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            return Integer.parseInt( value );
        }
        catch( NumberFormatException nfe )
        {
            throw new MissingResourceException( "Expecting a integer value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a long from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource long
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public long getLong( String key, long defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getLong( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a long from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource long
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public long getLong( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            return Long.parseLong( value );
        }
        catch( NumberFormatException nfe )
        {
            throw new MissingResourceException( "Expecting a long value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a float from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource float
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public float getFloat( String key, float defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getFloat( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a float from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource float
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public float getFloat( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            return Float.parseFloat( value );
        }
        catch( NumberFormatException nfe )
        {
            throw new MissingResourceException( "Expecting a float value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a double from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource double
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public double getDouble( String key, double defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getDouble( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a double from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource double
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public double getDouble( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            return Double.parseDouble( value );
        }
        catch( NumberFormatException nfe )
        {
            throw new MissingResourceException( "Expecting a double value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a date from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource date
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public Date getDate( String key, Date defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getDate( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a date from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource date
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public Date getDate( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            DateFormat format =
                DateFormat.getDateInstance( DateFormat.DEFAULT, m_locale );
            return format.parse( value );
        }
        catch( ParseException pe )
        {
            throw new MissingResourceException( "Expecting a date value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource time
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public Date getTime( String key, Date defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getTime( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource time
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public Date getTime( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            DateFormat format =
                DateFormat.getTimeInstance( DateFormat.DEFAULT, m_locale );
            return format.parse( value );
        }
        catch( ParseException pe )
        {
            throw new MissingResourceException( "Expecting a time value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a time from bundle.
     *
     * @param key          the key of resource
     * @param defaultValue the default value if key is missing
     *
     * @return the resource time
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public Date getDateTime( String key, Date defaultValue )
        throws MissingResourceException
    {
        try
        {
            return getDateTime( key );
        }
        catch( MissingResourceException mre )
        {
            return defaultValue;
        }
    }

    /**
     * Retrieve a date + time from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource date + time
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public Date getDateTime( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        String value = bundle.getString( key );
        try
        {
            DateFormat format =
                DateFormat.getDateTimeInstance( DateFormat.DEFAULT, DateFormat.DEFAULT, m_locale );
            return format.parse( value );
        }
        catch( ParseException pe )
        {
            throw new MissingResourceException( "Expecting a time value but got " + value,
                                                "java.lang.String",
                                                key
            );
        }
    }

    /**
     * Retrieve a raw string from bundle.
     *
     * @param key the key of resource
     *
     * @return the resource string
     *
     * @throws MissingResourceException if the requested key is unknown
     */
    public String getString( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        return bundle.getString( key );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param arg1 an arg
     *
     * @return the formatted string
     */
    public String getString( String key, Object arg1 )
    {
        Object[] args = new Object[]{ arg1 };
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     *
     * @return the formatted string
     */
    public String getString( String key, Object arg1, Object arg2 )
    {
        Object[] args = new Object[]{ arg1, arg2 };
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     *
     * @return the formatted string
     */
    public String getString( String key,
                             Object arg1,
                             Object arg2,
                             Object arg3
    )
    {
        Object[] args = new Object[]{ arg1, arg2, arg3 };
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     * @param arg4 an arg
     *
     * @return the formatted string
     */
    public String getString( String key,
                             Object arg1,
                             Object arg2,
                             Object arg3,
                             Object arg4
    )
    {
        Object[] args = new Object[]{ arg1, arg2, arg3, arg4 };
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     * @param arg4 an arg
     * @param arg5 an arg
     *
     * @return the formatted string
     */
    public String getString( String key,
                             Object arg1,
                             Object arg2,
                             Object arg3,
                             Object arg4,
                             Object arg5
    )
    {
        Object[] args = new Object[]{ arg1, arg2, arg3, arg4, arg5 };
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     * @param arg4 an arg
     * @param arg5 an arg
     * @param arg6 an arg
     *
     * @return the formatted string
     */
    public String getString( String key,
                             Object arg1,
                             Object arg2,
                             Object arg3,
                             Object arg4,
                             Object arg5,
                             Object arg6
    )
    {
        Object[] args = new Object[]{ arg1, arg2, arg3, arg4, arg5, arg6 };
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param arg1 an arg
     * @param arg2 an arg
     * @param arg3 an arg
     * @param arg4 an arg
     * @param arg5 an arg
     * @param arg6 an arg
     * @param arg7 an arg
     *
     * @return the formatted string
     */
    public String getString( String key,
                             Object arg1,
                             Object arg2,
                             Object arg3,
                             Object arg4,
                             Object arg5,
                             Object arg6,
                             Object arg7
    )
    {
        Object[] args = new Object[]{ arg1, arg2, arg3, arg4, arg5, arg6, arg7 };
        return format( key, args );
    }

    /**
     * Retrieve a string from resource bundle and format it with specified args.
     *
     * @param key  the key for resource
     * @param args an array of args
     *
     * @return the formatted string
     */
    public String format( String key, Object[] args )
    {
        try
        {
            String pattern = getPatternString( key );
            StringBuffer buff = new StringBuffer( key.length() + SPAN );
            MessageFormat messFormat = new MessageFormat( pattern, m_locale );
            messFormat.format( args, buff, null );
            String result = buff.toString();
            buff.setLength( 0 );
            return result;
        }
        catch( MissingResourceException mre )
        {
            StringBuffer sb = new StringBuffer();
            sb.append( "Unknown resource. Bundle: '" );
            sb.append( m_baseName );
            sb.append( "' Key: '" );
            sb.append( key );
            sb.append( "' Args: '" );

            for( int i = 0; i < args.length; i++ )
            {
                if( 0 != i )
                {
                    sb.append( "', '" );
                }
                sb.append( args[ i ] );
            }

            sb.append( "' Reason: " );
            sb.append( mre );

            return sb.toString();
        }
    }

    /**
     * Retrieve underlying ResourceBundle.
     * If bundle has not been loaded it will be loaded by this method.
     * Access is given in case other resources need to be extracted
     * that this Manager does not provide simplified access to.
     *
     * @return the ResourceBundle
     *
     * @throws MissingResourceException if an error occurs
     */
    public ResourceBundle getBundle()
        throws MissingResourceException
    {
        if( null == m_bundle )
        {
            // bundle wasn't cached, so load it, cache it, and return it.
            ClassLoader classLoader = m_classLoader;
            if( null == classLoader )
            {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
            if( null != classLoader )
            {
                m_bundle = ResourceBundle.getBundle( m_baseName, m_locale, classLoader );
            }
            else
            {
                m_bundle = ResourceBundle.getBundle( m_baseName, m_locale );
            }
        }
        return m_bundle;
    }

    /**
     * Utility method to retrieve a string from ResourceBundle.
     * If the key is a single string then that will be returned.
     * If key refers to string array then a random string will be chosen.
     * Other types cause an exception.
     *
     * @param key the key to resource
     *
     * @return the string resource
     *
     * @throws MissingResourceException if an error occurs
     */
    private String getPatternString( String key )
        throws MissingResourceException
    {
        ResourceBundle bundle = getBundle();
        Object object = bundle.getObject( key );

        // is the resource a single string
        if( object instanceof String )
        {
            return (String) object;
        }
        else if( object instanceof String[] )
        {
            //if string array then randomly pick one
            String[] strings = (String[]) object;
            return strings[ RANDOM.nextInt( strings.length ) ];
        }
        else
        {
            throw new MissingResourceException( "Unable to find resource of appropriate type.",
                                                "java.lang.String",
                                                key
            );
        }
    }
}
