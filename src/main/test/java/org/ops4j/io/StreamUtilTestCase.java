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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

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

    public void testCopyStream1()
        throws Exception
    {
        byte[] data1 = "1234567890".getBytes();
        MyByteArrayInputStream in1 = new MyByteArrayInputStream( data1 );
        MyByteArrayOutputStream out1 = new MyByteArrayOutputStream();
        StreamUtils.copyStream( in1, out1, true );
        String result = new String( out1.toByteArray() );
        assertEquals( "1234567890", result );
        try
        {
            in1.read();  //check it has been closed.
            fail( "InputStream was not closed." );
        } catch( IllegalStateException e )
        {
            // expected
        }
        try
        {
            out1.write( 34 );  //check it has been closed.
            fail( "OutputStream was not closed." );
        } catch( IllegalStateException e )
        {
            // expected
        }
    }

    public void testCopyStream2()
        throws Exception
    {
        byte[] data1 = "1234567890".getBytes();
        MyByteArrayInputStream in1 = new MyByteArrayInputStream( data1 );
        MyByteArrayOutputStream out1 = new MyByteArrayOutputStream();
        StreamUtils.copyStream( in1, out1, false );
        String result = new String( out1.toByteArray() );
        assertEquals( "1234567890", result );
        try
        {
            in1.read();  //check it has been closed.
        } catch( IllegalStateException e )
        {
            fail( "InputStream was closed." );
        }
        try
        {
            out1.write( 34 );  //check it has been closed.
        } catch( IllegalStateException e )
        {
            fail( "OutputStream was closed." );
        }
    }

    public void testCopyReaderToWriter()
        throws Exception
    {
        String s = "HabbaZout\u4512\u1243\u9812";
        StringReader reader = new StringReader( s );
        StringWriter writer = new StringWriter();
        StreamUtils.copyReaderToWriter( reader, writer, true );
        assertEquals( s, writer.getBuffer().toString() );
    }

    public void testCopyReaderToStream()
        throws Exception
    {
        String s = "HabbaZout\u1298\u1243\u9812";
        StringReader reader = new StringReader( s );
        ByteArrayOutputStream baos = new ByteArrayOutputStream( );
        StreamUtils.copyReaderToStream( reader, baos, "UTF-8", true );
        assertEquals( s, baos.toString() );

        reader = new StringReader( s );
        baos = new ByteArrayOutputStream( );
        StreamUtils.copyReaderToStream( reader, baos, "ISO-8859-1", true );
        //noinspection ErrorNotRethrown
        try
        {
            assertEquals( s, baos.toString() );
            fail( "Didn't fail incorrect encoding.");
        } catch( ComparisonFailure e )
        {
            // expected
        }
    }

    public void testCopyStreamToWriter()
        throws Exception
    {
        String s = "HabbaZout\u1298\u1243\u9812";
        ByteArrayInputStream in = new ByteArrayInputStream( s.getBytes( "UTF-8" ) );
        StringWriter writer = new StringWriter();
        StreamUtils.copyStreamToWriter( in, writer, "UTF-8", true );
        assertEquals( s, writer.getBuffer().toString() );

        in = new ByteArrayInputStream( s.getBytes( "ISO-8859-1" ) );
        writer = new StringWriter();
        StreamUtils.copyStreamToWriter( in, writer, "UTF-8", true );
        //noinspection ErrorNotRethrown
        try
        {
            assertEquals( s, writer.getBuffer().toString() );
            fail( "Didn't fail incorrect encoding.");
        } catch( ComparisonFailure e )
        {
            // expected
        }

    }

    private static class MyByteArrayOutputStream extends ByteArrayOutputStream
    {

        private boolean m_closed = false;

        public void close()
            throws IOException
        {
            m_closed = true;
            super.close();
        }

        public synchronized void write( int b )
            throws IllegalStateException
        {
            if( m_closed )
            {
                throw new IllegalStateException( "Stream closed" );
            }
            super.write( b );
        }
    }

    private static class MyByteArrayInputStream extends ByteArrayInputStream
    {
        private boolean m_closed = false;

        public MyByteArrayInputStream( byte[] buf )
        {
            super( buf );
        }

        public void close()
            throws IOException
        {
            m_closed = true;
            super.close();
        }

        public synchronized int read()
        {
            if( m_closed )
            {
                throw new IllegalStateException( "Stream closed" );
            }
            return super.read();
        }
    }
}
