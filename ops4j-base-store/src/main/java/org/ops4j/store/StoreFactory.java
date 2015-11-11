/*
 * Copyright 2009 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.store;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.ops4j.store.intern.TemporaryStore;

/**
 * Factory to create one or the other type of stores.
 * Mostly deals with the fact if you really need your own store or can live with a shared instance. (better)
 *
 * @author Toni Menzel (toni.menzel@rebaze.com)
 */
public class StoreFactory
{

    public static final String RELATIVE_STORAGE = "/tb";

    /**
     * Get a default store instance.
     * If you don't know, and just need the store an input stream, use this one.
     *
     * @return {@link #sharedLocalStore()}
     */
    public static StreamStore defaultStore()
    {
        return sharedLocalStore();
    }

     /**
     * Get a fixed folder on disk as store.
     * This way caches also survive multiple vm re-starts.
     * Also saves disk space by just storing unique items once.
     *
     * Relevant folder will be:
     * {@code new File( System.getProperty( "java.io.tmpdir" ) + "/tb"}
     * }
     *
     * @return a store instance pointing to a local folder on disk.
     */
    public static StreamStore sharedLocalStore()
    {
        return newStore(new File( System.getProperty( "java.io.tmpdir" ) + RELATIVE_STORAGE ));
    }

    /**
     * Create a store on a disk folder exactly as specified in the parameter.
     *
     * @param path the path on disk. Will not be flushed.
     * @return A ready to use store.
     * @since 1.6.0
     */
    public static StreamStore newStore( File path )
    {
        return new TemporaryStore( path, false );
    }

    /**
     *
     * Create a new store with specified values for storage location and flush-policy (be careful!).
     *
     * @param path the path on disk. Will not be flushed.
     * @param flush whether or not to flush the content in path (dangerous)
     * @return A ready to use store.
     * @since 1.6.0
     */
    public static StreamStore newStore( File path, boolean flush)
    {
        return new TemporaryStore( path, flush );
    }


    /**
     * If the store must be unique, here is it.
     *
     * @return unique storage
     *
     * @throws java.io.IOException in case no temp folder has been found.
     */
    public static StreamStore anonymousStore()
        throws IOException
    {
        File temp = File.createTempFile( "ops4j-store-anonymous-", "" );
        temp.delete();
        temp.mkdir();
        return new TemporaryStore( temp, true );
    }

    /**
     * Utility to convert raw data to a hex-only charset.
     *
     * @param data any charset data.
     * @return a hex-only charset
     * @since 1.6.0
     */
    public static String convertToHex( byte[] data )
    {
        StringBuffer buf = new StringBuffer();
        for( int i = 0; i < data.length; i++ )
        {
            int halfbyte = ( data[ i ] >>> 4 ) & 0x0F;
            int two_halfs = 0;
            do
            {
                if( ( 0 <= halfbyte ) && ( halfbyte <= 9 ) )
                {
                    buf.append( (char) ( '0' + halfbyte ) );
                }
                else
                {
                    buf.append( (char) ( 'a' + ( halfbyte - 10 ) ) );
                }
                halfbyte = data[ i ] & 0x0F;
            }
            while( two_halfs++ < 1 );
        }
        return buf.toString();
    }

}
