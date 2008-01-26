/*
 * Copyright 2004 Stephen J. McConnell.
 * Copyright 2004 Apache Software Foundation
 * Copyright 2005 Niclas Hedhman
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

package org.ops4j.util.environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;

/**
 * Encapsulates operating system and shell specific access to environment
 * variables.
 *
 * @author <a href="http://www.ops4j.org">Open Particpation Software for Java</a>
 * @version $Id$
 */
public class Environment extends Properties
{

    /**
     * os.name System property
     */
    public static final String OSNAME = System.getProperty( "os.name" );

    /**
     * user.name System property
     */
    public static final String USERNAME = System.getProperty( "user.name" );

    private static final HashSet<String> UNICES = new HashSet<String>();

    /**
     * the user's platform specific shell executable
     */
    private static String m_SHELL = null;

    static
    {
        UNICES.add( "Linux" );
        UNICES.add( "SunOS" );
        UNICES.add( "Solaris" );
        UNICES.add( "MPE/iX" );
        UNICES.add( "AIX" );
        UNICES.add( "FreeBSD" );
        UNICES.add( "Irix" );
        UNICES.add( "Digital Unix" );
        UNICES.add( "HP-UX" );
        UNICES.add( "Mac OS X" );
    }

    /**
     * Creates a snapshot of the current shell environment variables for a user.
     *
     * @throws EnvironmentException if there is an error accessing the environment
     */
    public Environment()
        throws EnvironmentException
    {
        Properties properties = getEnvVariables();
        Enumeration list = properties.propertyNames();
        while( list.hasMoreElements() )
        {
            String key = (String) list.nextElement();
            setProperty( key, properties.getProperty( key ) );
        }
    }

    /**
     * Returns the Data directory of the application for the logged in user.
     * On Unix systems, the $HOME/.<i>applicationname</i> (all lower-case) directory will be returned.
     * On Windows systems, $APPDATA/<i>applicationname</i> (with uppercase, first letter) will be returned.
     *
     * @param applicationname The name of the application.
     *
     * @return The directory where the application can store data.
     */
    public static File getDataDirectory( String applicationname )
    {
        if( isUnix() )
        {
            String dirname = "/home/" + USERNAME + "." + applicationname.toLowerCase();
            File dir = new File( dirname );
            return dir;
        }
        if( isWindows() )
        {
            String name = toFirstCap( applicationname );
            String dirname = getEnvVariable( "APPDATA" ) + File.separator + name;
            File dir = new File( dirname );
            return dir;
        }
        String message = "Environment operations not supported on unrecognized operatings system";
        UnsupportedOperationException cause = new UnsupportedOperationException( message );
        throw new EnvironmentException( cause );
    }

    /**
     * Returns the System directory of the application for the logged in user.
     * This method returns the directory where the application should be, or has been installed,
     * on the system, for the current user. On Unix systems, the following rules are applied;
     * <ol>
     * <li>applicationname is made into lowercase.
     * <li>application is looked for in the $HOME/<i>applicationname</i></li>
     * <li>application is looked for in the /usr/share/<i>applicationname</i></li>
     * <li>if the $USER == 'root', then use /usr/share/<i>applicationname</i></li>
     * <li>use $HOME/<i>applicationname</i></li>
     * </ol>
     * On Windows, the PROGRAMFILES environment variable is looked up, if not found, then use "C:\Program Files\"
     * and then append the <i>applicationname</i> with an initial uppercase character.
     *
     * @param applicationname The name of the application.
     *
     * @return The directory where the application is either installed or can be installed.
     *
     * @throws EnvironmentException if the current system in not Windows or Unix/Linux.
     */
    public static File getSystemDirectory( String applicationname )
        throws EnvironmentException
    {
        if( isUnix() )
        {
            String name = applicationname.toLowerCase();
            String dirname = "/home/" + USERNAME + name;
            File homeDir = new File( dirname );
            if( homeDir.exists() )
            {
                return homeDir;
            }
            dirname = "/usr/share/" + name;
            File shareDir = new File( dirname );
            if( shareDir.exists() )
            {
                return shareDir;
            }
            if( "root".equals( USERNAME ) )
            {
                return shareDir;
            }
            else
            {
                return homeDir;
            }
        }

        if( isWindows() )
        {
            String name = toFirstCap( applicationname );
            String shared = getEnvVariable( "PROGRAMFILES" );
            if( shared == null )
            {
                shared = "C:\\Program Files\\";
            }
            File dir = new File( shared, name );
            return dir;
        }
        String message = "Environment operations not supported on unrecognized operatings system";
        UnsupportedOperationException cause = new UnsupportedOperationException( message );
        throw new EnvironmentException( cause );
    }

    /**
     * Capitalizes the first character.
     *
     * @param applicationname The application name to be capitalized in the first character.
     *
     * @return A capitalized first character of the provided application name.
     */
    private static String toFirstCap( String applicationname )
    {
        char first = applicationname.charAt( 0 );
        first = Character.toUpperCase( first );
        String name = first + applicationname.substring( 1 );
        return name;
    }

    /**
     * Gets the value of a shell environment variable.
     *
     * @param name the name of variable
     *
     * @return the String representation of an environment variable value
     *
     * @throws EnvironmentException if there is a problem accessing the environment
     */
    public static String getEnvVariable( String name )
        throws EnvironmentException
    {
        if( isUnix() )
        {
            Properties properties = getUnixShellVariables();
            return properties.getProperty( name );
        }
        else if( isWindows() )
        {
            Properties properties = getWindowsShellVariables();
            return properties.getProperty( name );
        }

        String osName = System.getProperty( "os.name" );
        throw new EnvironmentException( name, "Non-supported operating system: " + osName );
    }

    /**
     * Checks to see if the operating system is a UNIX variant.
     *
     * @return true of the OS is a UNIX variant, false otherwise
     */
    public static boolean isUnix()
    {
        return UNICES.contains( OSNAME );
    }

    /**
     * Checks to see if the operating system is a UNIX variant.
     *
     * @return true of the OS is a UNIX variant, false otherwise
     */
    public static boolean isMacOsX()
    {
        if( -1 != OSNAME.indexOf( "Mac OS X" ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Checks to see if the operating system is a Windows variant.
     *
     * @return true of the OS is a Windows variant, false otherwise
     */
    public static boolean isWindows()
    {
        return ( -1 != OSNAME.indexOf( "Windows" ) );
    }

    /**
     * Checks to see if the operating system is NetWare.
     *
     * @return true of the OS is NetWare, false otherwise
     */
    public static boolean isNetWare()
    {
        return ( -1 != OSNAME.indexOf( "netware" ) );
    }

    /**
     * Checks to see if the operating system is OpenVMS.
     *
     * @return true of the OS is a NetWare variant, false otherwise
     */
    public static boolean isOpenVMS()
    {
        return ( -1 != OSNAME.indexOf( "openvms" ) );
    }

    /**
     * Gets all environment variables within a Properties instance where the
     * key is the environment variable name and value is the value of the
     * property.
     *
     * @return the environment variables and values as Properties
     *
     * @throws EnvironmentException if os is not recognized
     */
    public static Properties getEnvVariables()
        throws EnvironmentException
    {
        if( isUnix() )
        {
            return getUnixShellVariables();
        }

        if( isWindows() )
        {
            return getWindowsShellVariables();
        }
        String message = "Environment operations not supported on unrecognized operatings system";
        UnsupportedOperationException cause = new UnsupportedOperationException( message );
        throw new EnvironmentException( cause );
    }

    /**
     * Gets the user's shell executable.
     *
     * @return the shell executable for the user
     *
     * @throws EnvironmentException the there is a problem accessing shell
     *                              information
     */
    public static String getUserShell()
        throws EnvironmentException
    {
        if( isMacOsX() )
        {
            return getMacUserShell();
        }

        if( isWindows() )
        {
            return getWindowsUserShell();
        }

        if( isUnix() )
        {
            return getUnixUserShell();
        }

        String message = "Environment operations not supported on unrecognized operatings system";
        UnsupportedOperationException cause = new UnsupportedOperationException( message );
        throw new EnvironmentException( cause );
    }

    // ------------------------------------------------------------------------
    // Private UNIX Shell Operations
    // ------------------------------------------------------------------------

    /**
     * Gets the shell used by the Windows user.
     *
     * @return the shell: cmd.exe or command.com.
     */
    private static String getWindowsUserShell()
    {
        if( null != m_SHELL )
        {
            return m_SHELL;
        }

        if( -1 != OSNAME.indexOf( "98" )
            || -1 != OSNAME.indexOf( "95" )
            || -1 != OSNAME.indexOf( "Me" ) )
        {
            m_SHELL = "command.com";
            return m_SHELL;
        }

        m_SHELL = "cmd.exe";
        return m_SHELL;
    }

    /**
     * Gets the default login shell used by a mac user.
     *
     * @return the Mac user's default shell as referenced by cmd:
     *         'nidump passwd /'
     *
     * @throws EnvironmentException if os information is not resolvable
     */
    private static String getMacUserShell()
        throws EnvironmentException
    {
        if( null != m_SHELL )
        {
            return m_SHELL;
        }
        String[] args = { "nidump", "passwd", "/" };
        return readShellFromPasswdFile( args );
    }

    /**
     * Gets the default login shell used by a unix user.
     *
     * @return the Mac user's default shell as referenced by cmd:
     *         'nidump passwd /'
     *
     * @throws EnvironmentException if os information is not resolvable
     */
    private static String getUnixUserShell()
        throws EnvironmentException
    {
        if( null != m_SHELL )
        {
            return m_SHELL;
        }
        String[] args = { "cat", "/etc/passwd" };
        return readShellFromPasswdFile( args );
    }

    // Support Methods.........

    /**
     * Reads which shell the current user has as settings in the /etc/passwd file.
     *
     * @param args commandline arguments needed.
     *
     * @return The shell command used by the current user, such as /bin/bash
     */
    private static String readShellFromPasswdFile( String[] args )
    {
        Process process = null;
        BufferedReader reader = null;
        try
        {
            process = startProcess( args );
            reader = createReader( process );
            processPasswdFile( reader );
            process.waitFor();
            reader.close();
        }
        catch( InterruptedException t )
        {
            throw new EnvironmentException( t );
        }
        catch( IOException t )
        {
            throw new EnvironmentException( t );
        }
        finally
        {
            if( process != null )
            {
                process.destroy();
            }
            close( reader );
        }
        String message = "User " + USERNAME + " is not present in the passwd database";
        throw new EnvironmentException( message );
    }

    /**
     * Process a password file.
     *
     * @param reader The Reader connected to the password file.
     *
     * @throws IOException if an underlying I/O problem occurs.
     */
    private static void processPasswdFile( BufferedReader reader )
        throws IOException
    {
        String entry = reader.readLine();
        while( null != entry )
        {
            // Skip entries other than the one for this username
            if( entry.startsWith( USERNAME ) )
            {
                // Get the shell part of the passwd entry
                int index = entry.lastIndexOf( ':' );

                if( index == -1 )
                {
                    String message = "passwd database contains malformed user entry for " + USERNAME;
                    throw new EnvironmentException( message );
                }

                m_SHELL = entry.substring( index + 1 );
                break;
            }
            entry = reader.readLine();
        }
    }

    /**
     * Adds a set of Windows variables to a set of properties.
     *
     * @return the environment properties
     *
     * @throws EnvironmentException if an error occurs
     */
    private static Properties getUnixShellVariables()
        throws EnvironmentException
    {
        Properties properties = new Properties();
        String cmdExec = getUnixEnv();

        int exitValue = readEnvironment( cmdExec, properties );

        // Check that we exited normally before returning an invalid output
        if( 0 != exitValue )
        {
            throw new EnvironmentException( "Environment process failed  with non-zero exit code of " + exitValue );
        }

        return properties;
    }

    /**
     * Gets the UNIX env executable path.
     *
     * @return the absolute path to the env program
     *
     * @throws EnvironmentException if it cannot be found
     */
    private static String getUnixEnv()
        throws EnvironmentException
    {
        File env = new File( "/bin/env" );

        if( env.exists() && env.canRead() && env.isFile() )
        {
            return env.getAbsolutePath();
        }

        env = new File( "/usr/bin/env" );
        if( env.exists() && env.canRead() && env.isFile() )
        {
            return env.getAbsolutePath();
        }
        String message = "Could not find the UNIX env executable";
        throw new EnvironmentException( message );
    }

    /**
     * Adds a set of Windows variables to a set of properties.
     *
     * @return the environment properties
     *
     * @throws EnvironmentException if an error occurs
     */
    private static Properties getWindowsShellVariables()
        throws EnvironmentException
    {
        Properties properties = new Properties();
        // build the command based on the shell used: cmd.exe or command.com
        StringBuffer buffer = new StringBuffer( getWindowsUserShell() );
        buffer.append( " /C SET" );
        String cmdExec = buffer.toString();

        int exitValue = readEnvironment( cmdExec, properties );
        if( 0 != exitValue )
        {
            String message = "Environment process failed with non-zero exit code of " + exitValue;
            throw new EnvironmentException( message );
        }

        return properties;
    }

    /**
     * Process the lines of the environment variables returned.
     *
     * @param reader     The Reader that containes the lines to be parsed.
     * @param properties The Properties objects to be populated with the environment variable names and values.
     *
     * @throws IOException if an underlying I/O problem occurs.
     */
    private static void processLinesOfEnvironmentVariables( BufferedReader reader, Properties properties )
        throws IOException
    {
        String line = reader.readLine();
        while( null != line )
        {
            int index = line.indexOf( '=' );

            if( -1 == index && line.length() != 0 )
            {
                String message = "Skipping line - could not find '=' in line: '" + line + "'";
                System.err.println( message );
            }
            else
            {
                String name = line.substring( 0, index );
                String value = line.substring( index + 1, line.length() );
                properties.setProperty( name, value );
            }
            line = reader.readLine();
        }
    }

    /**
     * Starts a OS level process.
     *
     * @param exec The commandline arguments, including the process to be started.
     *
     * @return The Process instance.
     *
     * @throws IOException if an underlying I/O exception occurs.
     */
    private static Process startProcess( String[] exec )
        throws IOException
    {
        Process process = Runtime.getRuntime().exec( exec );
        return process;
    }

    /**
     * Creates a Reader instance for the process output.
     *
     * @param process The process to attach the reader to.
     *
     * @return A Reader attached to the process.
     */
    private static BufferedReader createReader( Process process )
    {
        InputStream inputStream = process.getInputStream();
        InputStreamReader in = new InputStreamReader( inputStream );
        BufferedReader reader = new BufferedReader( in );
        return reader;
    }

    /**
     * Reads the environment variables and stores them in a Properties object.
     *
     * @param cmdExec    The command to execute to get hold of the environment variables.
     * @param properties The Properties object to be populated with the environment variables.
     *
     * @return The exit value of the OS level process upon its termination.
     */
    private static int readEnvironment( String cmdExec, Properties properties )
    {
        // fire up the shell and get echo'd results on stdout
        BufferedReader reader = null;
        Process process = null;
        int exitValue = 99;
        try
        {
            String[] args = new String[]{ cmdExec };
            process = startProcess( args );
            reader = createReader( process );
            processLinesOfEnvironmentVariables( reader, properties );
            process.waitFor();
            reader.close();
        }
        catch( InterruptedException t )
        {
            throw new EnvironmentException( "NA", t );
        }
        catch( IOException t )
        {
            throw new EnvironmentException( "NA", t );
        }
        finally
        {
            if( process != null )
            {
                process.destroy();
                exitValue = process.exitValue();
            }
            close( reader );
        }
        return exitValue;
    }

    /**
     * Closes a Reader.
     * <p/>
     * Any exception during the close is printed to the System.err, as no such are expected.
     *
     * @param reader The reader to be closed.
     */
    private static void close( BufferedReader reader )
    {
        try
        {
            if( null != reader )
            {
                reader.close();
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }
}
