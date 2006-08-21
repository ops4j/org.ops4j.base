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
import java.io.ByteArrayInputStream;

public class StreamUtilTestCase extends TestCase
{

    public void testCompare1()
        throws Exception
    {
        byte[] data1 = "1234567890".getBytes();
        ByteArrayInputStream in1 = new ByteArrayInputStream( data1 );
        ByteArrayInputStream in2 = new ByteArrayInputStream( data1 );
        assertTrue( StreamUtils.compareStreams( in1, in2 ) );
        in1.close();
        in2.close();
    }

    public void testCompare2()
        throws Exception
    {
        byte[] data1 = "1234567890".getBytes();
        byte[] data2 = "123456789".getBytes();
        ByteArrayInputStream in1 = new ByteArrayInputStream( data1 );
        ByteArrayInputStream in2 = new ByteArrayInputStream( data2 );
        assertFalse( StreamUtils.compareStreams( in1, in2 ) );
        in1.close();
        in2.close();
    }

    public void testCompare3()
        throws Exception
    {
        byte[] data1 = "1234567890".getBytes();
        byte[] data2 = "123456789 ".getBytes();
        ByteArrayInputStream in1 = new ByteArrayInputStream( data1 );
        ByteArrayInputStream in2 = new ByteArrayInputStream( data2 );
        assertFalse( StreamUtils.compareStreams( in1, in2 ) );
        in1.close();
        in2.close();
    }

    public void testCompare4()
        throws Exception
    {
        byte[] data1 = "1234567890".getBytes();
        byte[] data2 = "123456789".getBytes();
        ByteArrayInputStream in1 = new ByteArrayInputStream( data2 );
        ByteArrayInputStream in2 = new ByteArrayInputStream( data1 );
        assertFalse( StreamUtils.compareStreams( in1, in2 ) );
        in1.close();
        in2.close();
    }

    public void testCompare5()
        throws Exception
    {
        byte[] data1 = "123456789 ".getBytes();
        byte[] data2 = "1234567890".getBytes();
        ByteArrayInputStream in1 = new ByteArrayInputStream( data1 );
        ByteArrayInputStream in2 = new ByteArrayInputStream( data2 );
        assertFalse( StreamUtils.compareStreams( in1, in2 ) );
        in1.close();
        in2.close();
    }

    public void testCompare6()
        throws Exception
    {
        byte[] data1 = new byte[0];
        byte[] data2 = "123456789".getBytes();
        ByteArrayInputStream in1 = new ByteArrayInputStream( data1 );
        ByteArrayInputStream in2 = new ByteArrayInputStream( data2 );
        assertFalse( StreamUtils.compareStreams( in1, in2 ) );
        in1.close();
        in2.close();
    }

    public void testCompare7()
        throws Exception
    {
        byte[] data1 = new byte[0];
        byte[] data2 = new byte[0];
        ByteArrayInputStream in1 = new ByteArrayInputStream( data1 );
        ByteArrayInputStream in2 = new ByteArrayInputStream( data2 );
        assertTrue( StreamUtils.compareStreams( in1, in2 ) );
        in1.close();
        in2.close();
    }
}
