/*
 * Copyright 2011 Harald Wellmann.
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
package org.ops4j.spi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Convenience methods for obtaining implementations of a given service via {@link ServiceLoader}.
 * A service is defined by an interface or an abstract class.
 * 
 * @author Harald Wellmann
 */
public class ServiceProviderFinder
{
    /**
     * Finds all providers for a given service.
     * 
     * @param klass service interface or class
     * @return list of all providers (not null)
     */
    public static <T> List<T> findServiceProviders( Class<T> klass )
    {
        ServiceLoader<T> loader = ServiceLoader.load( klass );
        List<T> providers = new ArrayList<T>();
        Iterator<T> it = loader.iterator();
        while ( it.hasNext() )
        {
            providers.add( it.next() );
        }
        return providers;
    }

    /**
     * Finds any provider for a given service.
     * 
     * @param klass service interface or class
     * @return the first provider found, or null
     */
    public static <T> T findAnyServiceProvider( Class<T> klass )
    {
        ServiceLoader<T> loader = ServiceLoader.load( klass );
        Iterator<T> it = loader.iterator();
        return it.hasNext() ? it.next() : null;
    }

    /**
     * Returns any provider for a given service and throws an exception when no provider
     * is found on the classpath.
     * 
     * @param klass service interface or class
     * @return the first provider found
     * @throws NoServiceProviderException
     */
    public static <T> T loadAnyServiceProvider( Class<T> klass )
    {
        T provider = findAnyServiceProvider( klass );
        if (provider == null) {
            throw new NoServiceProviderException( klass.getName()
                + ": no service provider found in META-INF/services on classpath" );            
        }
        return provider;
    }

    /**
     * Returns the unique service provider for a given service and throws an exception if there
     * is no unique provider
     * 
     * @param klass service interface or class
     * @return the first provider found
     * @throws NoServiceProviderException
     * @throws NonUniqueServiceProviderException
     */
    public static <T> T loadUniqueServiceProvider( Class<T> klass )
    {
        List<T> providers = findServiceProviders( klass );
        if( providers.isEmpty() )
        {
            throw new NoServiceProviderException( klass.getName()
                    + ": no service provider found in META-INF/services on classpath" );
        }
        else if( providers.size() > 1 )
        {
            throw new NonUniqueServiceProviderException( klass.getName()
                    + ": multiple service providers found in META-INF/services on classpath" );
        }
        return providers.get( 0 );
    }
}
