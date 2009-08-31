package org.ops4j.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;

public class DirectoryListerTest
    extends ListerTest
{

    private File m_dir;

    @Before
    public void setUp()
        throws FileNotFoundException
    {
        m_dir = FileUtils.getFileFromClasspath( "dirscanner" );
    }

    Lister createLister( Pattern filter )
    {
        return new DirectoryLister( m_dir, filter );
    }

    URL asURL( String fileName )
        throws MalformedURLException
    {
        return new File( m_dir, fileName ).toURL();
    }

    @Test( expected = IllegalArgumentException.class )
    public void cosntructorWithNullDir()
        throws MalformedURLException
    {
        new DirectoryLister( null, ListerUtils.parseFilter( "*" ) ).list();
    }

    @Test( expected = IllegalArgumentException.class )
    public void cosntructorWithNullFilter()
        throws MalformedURLException
    {
        new DirectoryLister( m_dir, null ).list();
    }

}
