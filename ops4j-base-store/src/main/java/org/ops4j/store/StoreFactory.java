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
 * @author Toni Menzel
 */
public class StoreFactory
{

    /**
     * Get a default store instance.
     * If you don't know, and just need the store an input stream, use this one.
     *
     * @return {@link #sharedLocalStore()}
     */
    public static Store<InputStream> defaultStore()
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
    public static Store<InputStream> sharedLocalStore()
    {
        return new TemporaryStore( new File( System.getProperty( "java.io.tmpdir" ) + "/tb" ), false );
    }

    /**
     * If the store must be unique, here is it.
     *
     * @return unique storage
     *
     * @throws java.io.IOException in case no temp folder has been found.
     */
    public static Store<InputStream> anonymousStore()
        throws IOException
    {
        File temp = File.createTempFile( "ops4j-store-anonymous-", "" );
        temp.delete();
        temp.mkdir();
        return new TemporaryStore( temp, true );
    }


}
