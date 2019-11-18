/*
 * Copyright 2004 Stephen J. McConnell.
 * Copyright 1999-2004 The Apache Software Foundation
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
package org.ops4j.util.i18n;

import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Locale;

/**
 * Manager for resources.
 *
 * @author <a href="http://www.ops4j.org">Open Participation Software for Java</a>
 */
public final class ResourceManager
{

    /**
     * Permission needed to clear complete cache.
     */
    private static final RuntimePermission CLEAR_CACHE_PERMISSION = new RuntimePermission( "i18n.clearCompleteCache" );

    /**
     * Resource lookup table.
     */
    private static final HashMap<String, WeakReference<Resources>> RESOURCES =
        new HashMap<String, WeakReference<Resources>>();

    /**
     * Private Constructor to block instantiation.
     */
    private ResourceManager()
    {
    }

    /**
     * Retrieve resource with specified basename.
     *
     * @param baseName the basename
     *
     * @return the Resources
     */
    public static Resources getBaseResources( String baseName )
    {
        return getBaseResources( baseName, null );
    }

    /**
     * Retrieve resource with specified basename.
     *
     * @param baseName    the basename
     * @param classLoader the classLoader to load resources from
     *
     * @return the Resources
     */
    public static Resources getBaseResources( String baseName, ClassLoader classLoader )
    {
        synchronized( ResourceManager.class )
        {
            Resources resources = getCachedResource( baseName );
            if( null == resources )
            {
                resources = new Resources( baseName, classLoader );
                putCachedResource( baseName, resources );
            }

            return resources;
        }
    }

    /**
     * Retrieve resource with specified basename.
     *
     * @param baseName    the basename
     * @param classLoader the classLoader to load resources from
     * @param locale      the locale of the resources requested.
     *
     * @return the Resources
     */
    public static Resources getBaseResources( String baseName, Locale locale, ClassLoader classLoader )
    {
        synchronized( ResourceManager.class )
        {
            Resources resources = getCachedResource( baseName + "_" + locale.hashCode() );
            if( null == resources )
            {
                resources = new Resources( baseName, locale, classLoader );
                putCachedResource( baseName + "_" + locale.hashCode(), resources );
            }
            return resources;
        }
    }

    /**
     * Clear the cache of all resources currently loaded into the system.
     * This method is useful if you need to dump the complete cache and because part of the application is reloading
     * and thus the resources may need to be reloaded.
     * Note that the caller must have been granted the "i18n.clearCompleteCache" {@link RuntimePermission} or
     * else a security exception will be thrown.
     *
     * @throws SecurityException if the caller does not have permission to clear cache
     */
    public static void clearResourceCache()
        throws SecurityException
    {
        synchronized( ResourceManager.class )
        {
            AccessController.checkPermission( CLEAR_CACHE_PERMISSION );
            RESOURCES.clear();
        }
    }

    /**
     * Cache specified resource in weak reference.
     *
     * @param baseName  the resource key
     * @param resources the resources object
     */
    private static void putCachedResource( String baseName, Resources resources )
    {
        synchronized( ResourceManager.class )
        {
            WeakReference<Resources> ref = new WeakReference<Resources>( resources );
            RESOURCES.put( baseName, ref );
        }
    }

    /**
     * Retrieve cached resource.
     *
     * @param baseName the resource key
     *
     * @return resources the resources object
     */
    private static Resources getCachedResource( String baseName )
    {
        synchronized( ResourceManager.class )
        {
            WeakReference weakReference = RESOURCES.get( baseName );
            if( null == weakReference )
            {
                return null;
            }
            else
            {
                return (Resources) weakReference.get();
            }
        }
    }

    /**
     * Retrieve resource for specified name.
     * The basename is determined by name postfixed with ".Resources".
     *
     * @param name the name to use when looking up resources
     *
     * @return the Resources
     */
    public static Resources getResources( String name )
    {
        return getBaseResources( name + ".Resources" );
    }

    /**
     * Retrieve resource for specified Classes package.
     * The basename is determined by name of classes package
     * postfixed with ".Resources".
     *
     * @param clazz the Class
     *
     * @return the Resources
     */
    public static Resources getPackageResources( Class clazz )
    {
        return getBaseResources( getPackageResourcesBaseName( clazz ), clazz.getClassLoader() );
    }

    /**
     * Retrieve resource for specified Classes package.
     * The basename is determined by name of classes package
     * postfixed with ".Resources".
     *
     * @param clazz  the Class
     * @param locale the locale of the package resources requested.
     *
     * @return the Resources
     */
    public static Resources getPackageResources( Class clazz, Locale locale )
    {
        return getBaseResources( getPackageResourcesBaseName( clazz ), locale, clazz.getClassLoader() );
    }

    /**
     * Retrieve resource for specified Class.
     * The basename is determined by name of Class
     * postfixed with "Resources".
     *
     * @param clazz the Class
     *
     * @return the Resources
     */
    public static Resources getClassResources( Class clazz )
    {
        return getBaseResources( getClassResourcesBaseName( clazz ), clazz.getClassLoader() );
    }

    /**
     * Retrieve resource for specified Class.
     * The basename is determined by name of Class
     * postfixed with "Resources".
     *
     * @param clazz  the Class
     * @param locale the requested Locale.
     *
     * @return the Resources
     */
    public static Resources getClassResources( Class clazz, Locale locale )
    {
        return getBaseResources( getClassResourcesBaseName( clazz ), locale, clazz.getClassLoader() );
    }

    /**
     * Retrieve resource basename for specified Classes package.
     * The basename is determined by name of classes package
     * postfixed with ".Resources".
     *
     * @param clazz the Class
     *
     * @return the resource basename
     */
    public static String getPackageResourcesBaseName( Class clazz )
    {
        final Package pkg = clazz.getPackage();

        String baseName;
        if( null == pkg )
        {
            String name = clazz.getName();
            if( -1 == name.lastIndexOf( "." ) )
            {
                baseName = "Resources";
            }
            else
            {
                baseName = name.substring( 0, name.lastIndexOf( "." ) ) + ".Resources";
            }
        }
        else
        {
            baseName = pkg.getName() + ".Resources";
        }

        return baseName;
    }

    /**
     * Retrieve resource basename for specified Class.
     * The basename is determined by name of Class postfixed with "Resources".
     *
     * @param clazz the Class
     *
     * @return the resource basename
     */
    public static String getClassResourcesBaseName( Class clazz )
    {
        return clazz.getName() + "Resources";
    }
}
