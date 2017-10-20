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
package org.ops4j.store.intern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ops4j.io.StreamUtils;
import org.ops4j.io.FileUtils;
import org.ops4j.store.Handle;
import org.ops4j.store.Store;

import static org.ops4j.store.StoreFactory.convertToHex;

/**
 * Entity store like implementation.
 * Stores incoming data (store) to disk at a temporary location.
 * The handle is valid for use (load) only for this instance's lifetime. (tmp storage location)
 *
 * Uses an SHA-1 hash for indexing.
 */
public class TemporaryStore implements Store<InputStream>
{

    public static final String FILENAME_PREFIX = "ops4jstore-";
    public static final String FILENAME_SUFFIX = ".bin";
    private static Logger LOG = LoggerFactory.getLogger( TemporaryStore.class );
    final private File m_dir;

    public TemporaryStore( final File folder, final boolean flushStoreage )
    {
        m_dir = folder;

        if( m_dir.exists() && flushStoreage )
        {
            FileUtils.delete( m_dir );
        }
        m_dir.mkdirs();
        LOG.debug( "Storage Area is " + m_dir.getAbsolutePath() );
    }

    public Handle store( InputStream inp )
        throws IOException
    {
        LOG.debug( "Enter store()" );
        final File intermediate = File.createTempFile( FILENAME_PREFIX, ".tmp" );

        FileOutputStream fis = null;
        final String h;

        fis = new FileOutputStream( intermediate );
        h = hash( inp, fis );

        fis.close();
        if( !getLocation( h ).exists() )
        {
            StreamUtils.copyStream( new FileInputStream( intermediate ), new FileOutputStream( getLocation( h ) ),
                                    true
            );
        }
        else
        {
            LOG.debug( "Object for " + h + " already exists in store." );
        }
        intermediate.delete();

        Handle handle = new Handle()
        {

            public String getIdentification()
            {
                return h;
            }
        };
        LOG.debug( "Exit store(): " + h );
        return handle;
    }

    private File getLocation( String id )
    {
        return new File( m_dir, getFileName( id ) );
    }

    private String getFileName( String id )
    {
        return FILENAME_PREFIX + id + FILENAME_SUFFIX;
    }

    public InputStream load( Handle handle )
        throws IOException
    {
        return new FileInputStream( getLocation( handle.getIdentification() ) );
    }

    public URI getLocation( Handle handle )
        throws IOException
    {
        return getLocation( handle.getIdentification() ).toURI();
    }

    public String hash( final InputStream is, OutputStream storeHere )
        throws IOException
    {

        byte[] sha1hash;

        try
        {
            MessageDigest md;
            md = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = new byte[1024];
            int numRead = 0;
            while( ( numRead = is.read( bytes ) ) >= 0 )

            {
                md.update( bytes, 0, numRead );
                storeHere.write( bytes, 0, numRead );
            }
            sha1hash = md.digest();
        }
        catch( NoSuchAlgorithmException e )
        {
            throw new RuntimeException( e );
        }
        catch( FileNotFoundException e )
        {
            throw new RuntimeException( e );
        }
        catch( IOException e )
        {
            throw new RuntimeException( e );
        }
        return convertToHex( sha1hash );
    }



}
