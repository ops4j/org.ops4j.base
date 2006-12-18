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

import junit.framework.TestCase;
import java.io.StringReader;
import java.io.InputStream;

public class ReaderInputStreamTest extends TestCase
{

    public void testReading()
        throws Exception
    {
        StringReader reader = new StringReader( "abcdåäö\u4312\u1289");
        InputStream in = new ReaderInputStream( reader, "UTF-8" );
        int[] compare = new int[] { 97, 98, 99, 100, 195, 165, 195, 164, 195, 182, 228, 140, 146, 225, 138, 137 };
        int b = in.read();
        int count = 0;
        for( int i=0 ; b != -1 ; i++ )
        {
            assertEquals( compare[i], b );
            b = in.read();
            count++;
        }
        assertEquals( compare.length, count );
        in.close();
        reader.close();
    }
}
