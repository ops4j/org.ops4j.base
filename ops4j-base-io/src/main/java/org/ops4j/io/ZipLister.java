package org.ops4j.io;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import org.ops4j.lang.NullArgumentException;

/**
 * Implementation of lister that list content of a zip file.
 *
 * @author Alin Dreghiciu
 * @since September 04, 2007
 */
public class ZipLister
    implements Lister
{

    /**
     * The url of the file from which the zip file was constructed.
     */
    private URL m_baseURL;
    /**
     * The root zip entries to be listed.
     */
    private final Enumeration<? extends ZipEntry> m_zipEntries;
    /**
     * File path include filters.
     */
    private final Pattern[] m_includes;
    /**
     * File path exclude filters.
     */
    private final Pattern[] m_excludes;

    /**
     * Creates a zip lister.
     *
     * @param baseURL    the url from which the zip file was created
     * @param zipEntries the zip file to be listed.
     * @param filter     filter to be used to filter entries from the zip
     */
    public ZipLister( final URL baseURL,
                      final Enumeration<? extends ZipEntry> zipEntries,
                      final Pattern filter )
    {
        NullArgumentException.validateNotNull( baseURL, "Base url" );
        NullArgumentException.validateNotNull( zipEntries, "Zip entries" );
        NullArgumentException.validateNotNull( filter, "Filter" );

        m_baseURL = baseURL;
        m_zipEntries = zipEntries;
        m_includes = new Pattern[]{ filter };
        m_excludes = new Pattern[0];
    }

    /**
     * Creates a zip lister.
     *
     * @param baseURL    the url from which the zip file was created
     * @param zipEntries the zip file to be listed.
     * @param includes   filters to be used to include entries from the directory
     * @param excludes   filters to be used to exclude entries from the directory
     */
    public ZipLister( final URL baseURL,
                      final Enumeration<? extends ZipEntry> zipEntries,
                      final Pattern[] includes,
                      final Pattern[] excludes )
    {
        NullArgumentException.validateNotNull( baseURL, "Base url" );
        NullArgumentException.validateNotNull( zipEntries, "Zip entries" );
        NullArgumentException.validateNotNull( includes, "Include filters" );
        NullArgumentException.validateNotNull( includes, "Exclude filters" );

        m_baseURL = baseURL;
        m_zipEntries = zipEntries;
        m_includes = includes;
        m_excludes = excludes;
    }

    /**
     * {@inheritDoc}
     */
    public List<URL> list()
        throws MalformedURLException
    {
        final List<URL> content = new ArrayList<URL>();
        // then we filter them based on configured filter
        while( m_zipEntries.hasMoreElements() )
        {
            final ZipEntry entry = m_zipEntries.nextElement();
            final String fileName = entry.getName();
            if( !entry.isDirectory()
                && matchesIncludes( fileName ) && !matchesExcludes( fileName ) )
            {
                content.add( new URL( "jar:" + m_baseURL.toExternalForm() + "!/" + fileName ) );
            }
        }

        return content;
    }

    /**
     * Checks if the file name matches inclusion patterns.
     *
     * @param fileName file name to be matched
     *
     * @return true if matches, false otherwise
     */
    private boolean matchesIncludes( final String fileName )
    {
        if( m_includes.length == 0 )
        {
            return true;
        }
        for( Pattern include : m_includes )
        {
            if( include.matcher( fileName ).matches() )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the file name matches exclusion patterns.
     *
     * @param fileName file name to be matched
     *
     * @return true if matches, false otherwise
     */
    private boolean matchesExcludes( final String fileName )
    {
        for( Pattern include : m_excludes )
        {
            if( include.matcher( fileName ).matches() )
            {
                return true;
            }
        }
        return false;
    }

}
