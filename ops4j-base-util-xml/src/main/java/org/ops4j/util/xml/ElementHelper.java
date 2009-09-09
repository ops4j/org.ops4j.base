/*
 * Copyright 2004 Stephen McConnell
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

package org.ops4j.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.ops4j.util.collections.PropertyResolver;

/**
 * Utility class supporting the XML document parsing.
 *
 * @author <a href="http://www.ops4j.org">Open Participation Software for Java</a>
 * @version $Id$
 */
public final class ElementHelper
{

    /**
     * Constructor (disabled)
     */
    private ElementHelper()
    {
    }

    /**
     * Return the root element of the supplied input stream.
     *
     * @param input the input stream containing a XML definition
     *
     * @return the root element
     *
     * @throws IOException                  If an underlying I/O problem occurred.
     * @throws ParserConfigurationException if there is a severe problem in the XML parsing subsystem.
     * @throws SAXException                 If the XML is malformed in some way.
     */
    public static Element getRootElement( InputStream input )
        throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating( false );
        factory.setNamespaceAware( false );
        try
        {
            factory.setFeature( "http://xml.org/sax/features/namespaces", false );
            factory.setFeature( "http://xml.org/sax/features/validation", false );
            factory.setFeature( "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false );
            factory.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
        }
        catch( Throwable ignore )
        {
            // ignore. we did our best.
        }
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse( input );
        return document.getDocumentElement();
    }

    /**
     * Return a named child relative to a supplied element.
     *
     * @param root the parent DOM element
     * @param name the name of a child element
     *
     * @return the child element of null if the child does not exist
     */
    public static Element getChild( Element root, String name )
    {
        if( null == root )
        {
            return null;
        }
        NodeList list = root.getElementsByTagName( name );
        int n = list.getLength();
        if( n < 1 )
        {
            return null;
        }
        return (Element) list.item( 0 );
    }

    /**
     * Return all children matching the supplied element name.
     *
     * @param root the parent DOM element
     * @param name the name against which child element will be matched
     *
     * @return the array of child elements with a matching name
     */
    public static Element[] getChildren( Element root, String name )
    {
        if( null == root )
        {
            return new Element[0];
        }
        NodeList list = root.getElementsByTagName( name );
        int n = list.getLength();
        ArrayList<Element> result = new ArrayList<Element>();
        for( int i = 0; i < n; i++ )
        {
            Node item = list.item( i );
            if( item instanceof Element )
            {
                result.add( (Element) item );
            }
        }
        Element[] retval = new Element[result.size()];
        result.toArray( retval );
        return retval;
    }

    /**
     * Return all children of the supplied parent.
     *
     * @param root the parent DOM element
     *
     * @return the array of all children
     */
    public static Element[] getChildren( Element root )
    {
        if( null == root )
        {
            return new Element[0];
        }
        NodeList list = root.getChildNodes();
        int n = list.getLength();
        if( n < 1 )
        {
            return new Element[0];
        }
        ArrayList<Element> result = new ArrayList<Element>();
        for( int i = 0; i < n; i++ )
        {
            Node item = list.item( i );
            if( item instanceof Element )
            {
                result.add( (Element) item );
            }
        }

        Element[] retval = new Element[result.size()];
        result.toArray( retval );
        return retval;
    }

    /**
     * Return the value of an element.
     *
     * @param node the DOM node
     *
     * @return the node value
     */
    public static String getValue( Element node )
    {
        if( null == node )
        {
            return null;
        }
        String value;
        if( node.getChildNodes().getLength() > 0 )
        {
            value = node.getFirstChild().getNodeValue();
        }
        else
        {
            value = node.getNodeValue();
        }
        return normalize( value );
    }

    /**
     * Return the value of an element attribute.
     *
     * @param node the DOM node
     * @param key  the attribute key
     *
     * @return the attribute value or null if the attribute is undefined
     */
    public static String getAttribute( Element node, String key )
    {
        return getAttribute( node, key, null );
    }

    /**
     * Return the value of an element attribute.
     *
     * @param node the DOM node
     * @param key  the attribute key
     * @param def  the default value if the attribute is undefined
     *
     * @return the attribute value or the default value if undefined
     */
    public static String getAttribute( Element node, String key, String def )
    {
        if( null == node )
        {
            return def;
        }
        String value = node.getAttribute( key );
        if( null == value )
        {
            return def;
        }
        if( "".equals( value ) )
        {
            return def;
        }
        return normalize( value );
    }

    /**
     * Parse the value for any property tokens relative to system properties.
     *
     * @param value the value to parse
     *
     * @return the normalized string
     */
    private static String normalize( String value )
    {
        return normalize( value, System.getProperties() );
    }

    /**
     * Parse the value for any property tokens relative to the supplied properties.
     *
     * @param value the value to parse
     * @param props the reference properties
     *
     * @return the normalized string
     */
    private static String normalize( String value, Properties props )
    {
        return PropertyResolver.resolve( props, value );
    }
}
