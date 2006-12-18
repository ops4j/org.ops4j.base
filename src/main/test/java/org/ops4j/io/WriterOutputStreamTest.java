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
import java.io.StringWriter;

public class WriterOutputStreamTest extends TestCase
{
    public void testWriterOutputStream()
        throws Exception
    {
        String compare = "abcdåäö\u4312\u1289";
        int[] data = new int[] { 97, 98, 99, 100, 195, 165, 195, 164, 195, 182, 228, 140, 146, 225, 138, 137 };
        StringWriter writer = new StringWriter();
        WriterOutputStream wos = new WriterOutputStream( writer, "UTF-8" );
        for( int i=0; i < data.length; i++ )
        {
            wos.write( data[i] );
        }
        wos.flush();
        assertEquals( compare, writer.toString() );
        wos.close();
    }
}
