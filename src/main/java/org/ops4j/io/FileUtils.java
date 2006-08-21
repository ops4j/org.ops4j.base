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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import org.ops4j.monitors.stream.StreamMonitor;
import org.ops4j.lang.NullArgumentException;

public class FileUtils
{

    public static void copyFile( File src, File dest, StreamMonitor monitor )
        throws IOException, FileNotFoundException
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        int length = (int) src.length();
        try
        {
            fis = new FileInputStream( src );
            fos = new FileOutputStream( dest );
            StreamUtils.copyStream( monitor, src.toURL(), length, fis, fos, true );
        } catch( FileNotFoundException e )
        {
            reportError( monitor, e, src.toURL() );
            throw e;
        } catch( NullArgumentException e )
        {
            reportError( monitor, e, src.toURL() );
            throw e;
        } catch( MalformedURLException e )
        {
            reportError( monitor, e, src.toURL() );
            throw e;
        } catch( IOException e )
        {
            reportError( monitor, e, src.toURL() );
            throw e;
        }
    }

    private static void reportError( StreamMonitor monitor, Exception e, URL url )
    {
        if( monitor != null )
        {
            monitor.notifyError( url, e.getMessage() );
        }
    }
}
