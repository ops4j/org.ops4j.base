/*  Copyright 2007 Niclas Hedhman.
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
package org.ops4j.util.mime;

import junit.framework.TestCase;

public class MimeTypeHandlerTest extends TestCase
{
    public void testJarType()
    {
        String type = "jar";
        String mime = MimeTypeHandler.getMimeType( type );
        assertEquals( "application/x-jar", mime );
    }

    public void testZipType()
    {
        String type = "zip";
        String mime = MimeTypeHandler.getMimeType( type );
        assertEquals( "application/x-zip", mime );
    }

    public void testPdfType()
    {
        String type = "pdf";
        String mime = MimeTypeHandler.getMimeType( type );
        assertEquals( "application/pdf", mime );
    }

    public void testPngType()
    {
        String type = "png";
        String mime = MimeTypeHandler.getMimeType( type );
        assertEquals( "image/png", mime );
    }

    public void testGifType()
    {
        String type = "gif";
        String mime = MimeTypeHandler.getMimeType( type );
        assertEquals( "image/gif", mime );
    }
    public void testJpgType()
    {
        String type = "jpg";
        String mime = MimeTypeHandler.getMimeType( type );
        assertEquals( "image/jpg", mime );
    }
    public void testLinkType()
    {
        String type = "link";
        String mime = MimeTypeHandler.getMimeType( type );
        assertEquals( "application/x-ops4j-link", mime );
    }
}
