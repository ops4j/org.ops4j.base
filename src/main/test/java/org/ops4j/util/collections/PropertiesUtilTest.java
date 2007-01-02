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
package org.ops4j.util.collections;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import junit.framework.TestCase;
import org.ops4j.util.collections.PropertiesUtils;

public class PropertiesUtilTest extends TestCase
{
    static private final String DATA1 = "#Test Data\n"
                                        + "mything1=${myplace}/mything1\n"
                                        + "mything2=${myplace}/mything2\n"
                                        + "mything3=${myplace}/mything3\n"
                                        + "mything4=${myplace}/mything4\n"
                                        + "mything5=${myplace}/mything5\n"
                                        + "myplace=${user.home}/myplace\n"
                                        + "mything6=${myplace}/mything6\n";

    static private final String DATA2 = "#Test Data\n"
                                        + "mything1=${${place}place}/mything1\n"
                                        + "mything2=${${place}place}/mything2\n"
                                        + "mything3=${${place}place}/mything3\n"
                                        + "mything4=${${place}place}/mything4\n"
                                        + "mything5=${${place}place}/mything5\n"
                                        + "myplace=${user.home}/${place}place\n"
                                        + "mything6=${${place}place}/mything6\n";

    public void testReadPropertiesFromStream()
        throws IOException
    {
        byte[] bytes = DATA1.getBytes( "iso-8859-1" );
        ByteArrayInputStream data = new ByteArrayInputStream( bytes );
        Properties props = PropertiesUtils.readProperties( data, System.getProperties(), true );
        Iterator list = props.entrySet().iterator();
        while( list.hasNext() )
        {
            Map.Entry entry = (Map.Entry) list.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if( "myplace".equals( key ) )
            {
                String userhome = System.getProperty( "user.home" );
                String myplace = userhome + "/" + "myplace";
                assertEquals( myplace, value );
            }
            else
            {
                String userhome = System.getProperty( "user.home" );
                String myplace = userhome + "/myplace";
                String mything = myplace + "/" + key;
                assertEquals( mything, value );
            }
        }
    }

    public void testNestedNaming()
        throws IOException
    {
        byte[] bytes = DATA2.getBytes( "iso-8859-1" );
        ByteArrayInputStream data = new ByteArrayInputStream( bytes );
        Properties sysProps = System.getProperties();
        Properties mappings = new Properties();
        mappings.putAll( sysProps );
        mappings.put( "place", "my");
        String place = "my";
        validateProps( data, mappings, place );

        mappings.put( "place", "your");
        place = "your";
        validateProps( data, mappings, place );
    }

    private void validateProps( ByteArrayInputStream data, Properties sysProps, String place )
        throws IOException
    {
        Properties props = PropertiesUtils.readProperties( data, sysProps, true );
        Iterator list = props.entrySet().iterator();
        while( list.hasNext() )
        {
            Map.Entry entry = (Map.Entry) list.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if( "myplace".equals( key ) )
            {
                String userhome = System.getProperty( "user.home" );
                String myplace = userhome + "/" + place + "place";
                assertEquals( myplace, value );
            }
            else
            {
                String userhome = System.getProperty( "user.home" );
                String myplace = userhome + "/" + place + "place";
                String mything = myplace + "/" + key;
                assertEquals( mything, value );
            }
        }
    }
}
