/*
 * Copyright 2006 Niclas Hedhman.
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
package org.ops4j.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.ops4j.lang.NullArgumentException;
import org.ops4j.monitors.stream.StreamMonitor;

/**
 * Utility methods for manipulation of Files.
 */
public final class FileUtils {

    /**
     * Private Constructor to ensure no instances are created.
     */
    private FileUtils() {

    }

    /**
     * Copies a file.
     * 
     * @param src
     *            The source file.
     * @param dest
     *            The destination file.
     * @param monitor
     *            The monitor to use for reporting.
     * @throws IOException
     *             if any underlying I/O problem occurs.
     * @throws FileNotFoundException
     *             if the source file does not exist.
     */
    public static void copyFile(File src, File dest, StreamMonitor monitor) throws IOException, FileNotFoundException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        int length = (int) src.length();
        try {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dest);
            StreamUtils.copyStream(monitor, src.toURI().toURL(), length, fis, fos, true);
        } catch (FileNotFoundException e) {
            reportError(monitor, e, src.toURI().toURL());
            throw e;
        } catch (NullArgumentException e) {
            reportError(monitor, e, src.toURI().toURL());
            throw e;
        } catch (MalformedURLException e) {
            reportError(monitor, e, src.toURI().toURL());
            throw e;
        } catch (IOException e) {
            reportError(monitor, e, src.toURI().toURL());
            throw e;
        }
    }

    /**
     * @param monitor
     *            The monitor to report to.
     * @param e
     *            The exception that occurred.
     * @param url
     *            The URL that was involved.
     */
    private static void reportError(StreamMonitor monitor, Exception e, URL url) {
        if (monitor != null) {
            monitor.notifyError(url, e.getMessage());
        }
    }

    /**
     * Searches the classpath for the file denoted by the file path and returns
     * the corresponding file.
     * 
     * @param filePath
     *            path to the file
     * @return a file corresponding to the path
     * @throws FileNotFoundException
     *             if the file cannot be found
     */
    public static File getFileFromClasspath(final String filePath) throws FileNotFoundException {
        try {
            URL fileURL = FileUtils.class.getClassLoader().getResource(filePath);
            if (fileURL == null) {
                throw new FileNotFoundException("File [" + filePath + "] could not be found in classpath");
            }
            return new File(fileURL.toURI());
        } catch (URISyntaxException e) {
            throw new FileNotFoundException("File [" + filePath + "] could not be found: " + e.getMessage());
        }
    }

    /**
     * Deletes the file or recursively deletes a directory depending on the file
     * passed.
     * 
     * @param file
     *            file or directory to be deleted.
     * @return true if the file was deleted.
     */
    public static boolean delete(final File file) {
        boolean delete = false;
        if (file != null && file.exists()) {
            // even if is a directory try to delete. maybe is empty or maybe is a *nix symbolic link
            delete = file.delete();
            if (!delete && file.isDirectory()) {
                File[] childs = file.listFiles();
                if (childs != null && childs.length > 0) {
                    for (File child : childs) {
                        delete(child);
                    }
                    // then try again as by now the directory can be empty
                    delete = file.delete();
                }
            }
        }
        return delete;
    }

    /**
     * Constructs an array of {@link File} objects from a given {@link String}
     * array
     * 
     * @param names
     *            the names to convert
     * @return the file object array
     */
    public static File[] pathNamesToFiles(String... names) {
        File[] files = new File[names.length];
        for (int i = 0; i < files.length; i++) {
            String pathname = names[i];
            if (pathname != null) {
                files[i] = new File(pathname);
            }
        }
        return files;
    }

}
