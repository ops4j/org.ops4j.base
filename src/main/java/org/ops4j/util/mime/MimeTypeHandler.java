/*
 * Copyright 2004-2005 Niclas Hedhman.
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

package org.ops4j.util.mime;

import java.util.HashMap;

/**
 * Mimetype utility hanlder.
 */
public final class MimeTypeHandler
{
   /**
    * Static mime type table of artifact types to mimetype strings.
    */
    private static final HashMap<String,String> MIME_TYPES = new HashMap<String, String>();

    static
    {
        MIME_TYPES.put( "jar", "application/x-jar" );
        MIME_TYPES.put( "zip", "application/x-zip" );
        MIME_TYPES.put( "pdf", "application/pdf" );
        MIME_TYPES.put( "png", "image/png" );
        MIME_TYPES.put( "gif", "image/gif" );
        MIME_TYPES.put( "jpg", "image/jpg" );
        MIME_TYPES.put( "link", "application/x-ops4j-link" );
    }

   /**
    * Return the mimetype given a artifact type.
    * @param artifactType the artifact type such as "block", "jar", etc.
    * @return the matching mimetype of null if unknown
    */
    static public String getMimeType( String artifactType )
    {
        return MIME_TYPES.get( artifactType );
    }

    /**
     * Returns the number of MimeTypes that has been defined.
     *
     * Only for use with testcases.
     * @return the known mimetype count
     */
    static public int getMimeTypesSize()
    {
        return MIME_TYPES.size();
    }

   /**
    * Disabled constructor.
    */
    private MimeTypeHandler()
    {
        // disable
    }
}