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
package org.ops4j.util.xml;

import java.io.InputStream;
import junit.framework.TestCase;
import org.w3c.dom.Element;

public class ElementHelperTest extends TestCase
{

    private InputStream m_input;

    protected void setUp()
        throws Exception
    {
        System.setProperty( "org.ops4j.base", "ville valle & victor" );
        m_input = getClass().getResourceAsStream( "test.xml" );
    }

    public void testGetRootElement()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        assertEquals( "habba", elem.getNodeName() );
    }

    public void testGetFirstChild()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element child = ElementHelper.getChild( elem, "zout" );
        assertEquals( "suput", child.getAttribute( "name" ) );
    }

    public void testGetThirdChild()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element child = ElementHelper.getChild( elem, "lingondricka" );
        assertEquals( "soppa", child.getAttribute( "oxo" ) );
    }

    public void testGetMissingChild()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element child = ElementHelper.getChild( elem, "truffel" );
        assertNull( "A non existent Element was returned.", child );
    }

    public void testGetAllChildren()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element[] children = ElementHelper.getChildren( elem );
        assertEquals( 5, children.length );
        assertEquals( "suput", children[ 0 ].getAttribute( "name" ) );
        assertEquals( "dravel", children[ 1 ].getAttribute( "name" ) );
        assertEquals( "soppa", children[ 2 ].getAttribute( "oxo" ) );
        assertEquals( "byggnadsmateriel", children[ 3 ].getAttribute( "name" ) );
    }

    public void testGetSomeChildren()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element[] children = ElementHelper.getChildren( elem, "zout" );
        assertEquals( 3, children.length );
        assertEquals( "suput", children[ 0 ].getAttribute( "name" ) );
        assertEquals( "dravel", children[ 1 ].getAttribute( "name" ) );
        assertEquals( "byggnadsmateriel", children[ 2 ].getAttribute( "name" ) );
    }

    public void testGetValue()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element child = ElementHelper.getChild( elem, "lingondricka" );
        String value = ElementHelper.getValue( child );
        assertEquals( "vasaloppet ", value );

        child = ElementHelper.getChild( elem, "frysskydd" );
        value = ElementHelper.getValue( child );
        assertEquals( "installerat\n  ", value );
    }

    public void testGetNormalizedValue()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element child = ElementHelper.getChild( elem, "zout" );
        String value = ElementHelper.getValue( child );
        assertEquals( "ville valle & victor", value );
    }

    public void testGetAttribute()
        throws Exception
    {
        Element elem = ElementHelper.getRootElement( m_input );
        Element child = ElementHelper.getChild( elem, "frysskydd" );
        String attribute = ElementHelper.getAttribute( child, "mycketkallt", null );
        assertEquals( "ville valle & victor", attribute );

        attribute = ElementHelper.getAttribute( child, "vadåkallt", "pilfink" );
        assertEquals( "pilfink", attribute );

        attribute = ElementHelper.getAttribute( child, "barakallt", null );
        assertNull( attribute );
    }
}
