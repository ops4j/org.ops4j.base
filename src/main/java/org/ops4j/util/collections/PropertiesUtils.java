/*
 * Copyright 2004 Niclas Hedhman
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

package org.ops4j.util.collections;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/** Utility class supporting operations related to property retrival. */
public final class PropertiesUtils
{
    /** Private Constructor.
      */
    private PropertiesUtils()
    {
    }

    /**
     * Read a set of properties from a property file specificed by a url.
     *
     * @param propsUrl the url of the property file to read
     * @param mappings Properties that will be available for translations initially.
     *
     * @return the resolved properties
     *
     * @throws IOException if an io error occurs
     * @see #readProperties(java.io.InputStream,java.util.Properties,boolean)
     */
    public static Properties readProps( URL propsUrl, Properties mappings )
        throws IOException
    {
        InputStream stream = propsUrl.openStream();
        return readProperties( stream, mappings, true );
    }

    /**
     * Read a set of properties from a property file provided as an Inputstream.
     * <p/>
     * Property files may reference symbolic properties in the form ${name}. Translations occur from both the
     * <code>mappings</code> properties as well as all values within the Properties that are being read.
     * </p>
     * <p/>
     * Example;
     * </p>
     * <code><pre>
     *   Properties mappings = System.getProperties();
     *   Properties myProps = PropertiesUtils.readProperties( "mine.properties", mappings );
     * </pre></code>
     * <p/>
     * and the "mine.properties" file contains;
     * </p>
     * <code><pre>
     * mything=${myplace}/mything
     * myplace=${user.home}/myplace
     * </pre></code>
     * <p/>
     * then the Properties object <i>myProps</i> will contain;
     * </p>
     * <code><pre>
     * mything=/home/niclas/myplace/mything
     * myplace=/home/niclas/myplace
     * </pre></code>
     *
     * @param stream      the InputStream of the property file to read.
     * @param mappings    Properties that will be available for translations initially.
     * @param closeStream true if the method should close the stream before returning, false otherwise.
     *
     * @return the resolved properties.
     *
     * @throws IOException if an io error occurs
     */
    public static Properties readProperties( InputStream stream, Properties mappings, boolean closeStream )
        throws IOException
    {
        try
        {
            Properties p = new Properties();
            p.load( stream );
            mappings.putAll( p );
            boolean doAgain = true;
            while( doAgain )
            {
                doAgain = false;
                for( Map.Entry<Object, Object> entry : p.entrySet() )
                {
                    String value = (String) entry.getValue();
                    if( value.indexOf( "${" ) >= 0 )
                    {
                        value = resolveProperty( mappings, value );
                        entry.setValue( value );
                        doAgain = true;
                    }
                }
            }
            return p;
        }
        finally
        {
            if( closeStream )
            {
                stream.close();
            }
        }
    }

    /**
     * Resolve symbols in a supplied value against supplied known properties.
     *
     * @param props a set of know properties
     * @param value the string to parse for tokens
     *
     * @return the resolved string
     */
    public static String resolveProperty( Properties props, String value )
    {
        return PropertyResolver.resolve( props, value );
    }

    /**
     * Return the value of a property.
     *
     * @param props the property file
     * @param key   the property key to lookup
     * @param def   the default value
     *
     * @return the resolve value
     */
    public static String getProperty( Properties props, String key, String def )
    {
        String value = props.getProperty( key, def );
        if( value == null )
        {
            return null;
        }
        if( "".equals( value ) )
        {
            return value;
        }
        value = PropertyResolver.resolve( props, value );
        return value;
    }

    /**
     * Read a file and return the list of lines in an array of strings.
     *
     * @param listFile the url to read from
     *
     * @return the lines
     *
     * @throws IOException if a read error occurs
     */
    public static String[] readListFile( URL listFile )
        throws IOException
    {
        ArrayList<String> list = new ArrayList<String>();
        InputStream stream = listFile.openStream();
        try
        {
            InputStreamReader isr = new InputStreamReader( stream, "UTF-8" );
            BufferedReader reader = new BufferedReader( isr );
            String line = reader.readLine();
            while( line != null )
            {
                list.add( line );
                line = reader.readLine();
            }
            String[] items = new String[list.size()];
            list.toArray( items );
            return items;
        }
        finally
        {
            stream.close();
        }
    }
}
