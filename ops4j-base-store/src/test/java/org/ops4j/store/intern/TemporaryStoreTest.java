/*
 * Copyright 2015 Toni Menzel.
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

import org.junit.Test;
import org.ops4j.store.StoreFactory;
import org.ops4j.store.StreamStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.*;

/**
 * @author Toni Menzel (toni.menzel@rebaze.com)
 */
public class TemporaryStoreTest
{
    @Test
    public void testAnonymousStore() throws IOException
    {
        StreamStore store1 = StoreFactory.anonymousStore();
        StreamStore store2 = StoreFactory.anonymousStore();
        try
        {
            File f1 = new File( store1.getLocation( store1.store( resource1() ) ) ).getParentFile();
            File f2 = new File( store2.getLocation( store2.store( resource1() ) ) ).getParentFile();
            assertFalse( "Anonymous Stores should not share the same parent (since they are anonymous)", f1.equals( f2 ) );
        }finally {
            safeClose(store1);
            safeClose(store2);
        }

    }

    @Test
    public void testSharedStore() throws IOException
    {
        StreamStore store1 = StoreFactory.sharedLocalStore();
        StreamStore store2 = StoreFactory.sharedLocalStore();
        try {
            File f1 = new File(store1.getLocation( store1.store( resource1() ) )).getParentFile();
            File f2 = new File(store2.getLocation( store2.store( resource1() ) )).getParentFile();
        assertEquals("Shared Stores should share the same parent (since they are shared)", f1,f2 );
        }finally {
            safeClose(store1);
            safeClose(store2);
        }
    }

    @Test
    public void testPuttingTheSameResourceTwice() throws IOException
    {
        StreamStore store = StoreFactory.sharedLocalStore();
        try {
            String id1 =  store.store( resource1()).getIdentification();
            String id2 =  store.store( resource1()).getIdentification();
            assertEquals(id1,id2);
        }finally {
            safeClose(store);
        }
    }

    @Test
    public void testCleanup() throws Exception
    {
        StreamStore store = StoreFactory.sharedLocalStore();
        try {
            File f = new File(store.getLocation( store.store( resource1() ) )).getParentFile();
            assertTrue(f.exists() && f.isDirectory());
            store.close();
            assertFalse(f.exists());
        }finally {
            safeClose(store);
        }
    }

    @Test
    public void testAutoclose() throws Exception
    {
        File f;
        try (StreamStore store = StoreFactory.sharedLocalStore())
        {
            f = new File( store.getLocation( store.store( resource1() ) ) ).getParentFile();
            assertTrue( f.exists() && f.isDirectory() );
        }
        assertFalse(f.exists());
    }

    private InputStream resource1()
    {
        return getClass().getResourceAsStream( "/testresource1.txt" );
    }

    private void safeClose( StreamStore store )
    {
        try
        {
            store.close();
        }
        catch ( Exception e )
        {
            // ignore.
        }
    }
}
