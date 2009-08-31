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
package org.ops4j.io;

import java.util.regex.Pattern;

/**
 * Utility methods related to {@link Lister}.
 */
public final class ListerUtils
{

    /**
     * Private Constructor to ensure no instances are created.
     */
    private ListerUtils()
    {

    }

    /**
     * Parses a usual filter into a regex pattern.
     *
     * @param spec the filter to be parsed
     *
     * @return a regexp pattern corresponding to the filter
     *
     * @throws IllegalArgumentException - If the filter could not be compiled to a pattern
     */
    public static Pattern parseFilter( final String spec )
    {
        StringBuffer sb = new StringBuffer();
        for( int j = 0; j < spec.length(); j++ )
        {
            char c = spec.charAt( j );
            switch( c )
            {
                case '.':
                    sb.append( "\\." );
                    break;

                case '*':
                    // test for ** (all directories)
                    if( j < spec.length() - 1 && spec.charAt( j + 1 ) == '*' )
                    {
                        sb.append( ".*" );
                        j++;
                    }
                    else
                    {
                        sb.append( "[^/]*" );
                    }
                    break;
                default:
                    sb.append( c );
                    break;
            }
        }
        String s = sb.toString();
        try
        {
            return Pattern.compile( s );
        }
        catch( Exception e )
        {
            throw new IllegalArgumentException( "Invalid character used in the filter name", e );
        }
    }

}