/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  IBM Corporation - initial API and implementation
 *  Harald Wellmann - adapted for Pax Exam
 *******************************************************************************/

package org.ops4j.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * class for exploding jar/zip files onto the file system
 * 
 * @author Barry Feigenbaum
 */
public class ZipExploder {
    /**
     * create a zip exploder for unpacking .jar/.zip files
     */
    public ZipExploder() {
        this(false);
    }

    /**
     * create a zip exploder for unpacking .jar/.zip files onto the file system
     * 
     * @param verbose
     *            - set to <code>true</code> for verbose mode
     */
    public ZipExploder(boolean verbose) {
        setVerbose(verbose);
    }

    /**
     * create a zip exploder for unpacking .jar/.zip files onto the file system
     * 
     * @param verbose
     *            - set to <code>true</code> for verbose mode
     * @param sorted
     *            - set to <code>true</code> for sorted file mode
     */
    public ZipExploder(boolean verbose, boolean sorted) {
        this(verbose);
        setSortNames(sorted);
    }

    protected boolean verbose;

    /**
     * Get the verbose mode state.
     * 
     * @return verbosity
     */
    public boolean getVerbose() {
        return verbose;
    }

    /**
     * set the verbose mode state
     * 
     * @param f
     *            - verbosity
     */
    public void setVerbose(boolean f) {
        verbose = f;
    }

    protected boolean sortNames;

    /**
     * @return Returns the sortNames.
     */
    public boolean getSortNames() {
        return sortNames;
    }

    /**
     * @param sortNames
     *            The sortNames to set.
     */
    public void setSortNames(boolean sortNames) {
        this.sortNames = sortNames;
    }

    /**
     * Explode source JAR and/or ZIP files into a target directory
     * 
     * @param zipNames
     *            names of source files
     * @param jarNames
     *            names of source files
     * @param destDir
     *            target directory name (should already exist)
     * @exception IOException
     *                error creating a target file
     * @deprecated use {@link #process(File[], File[], File)} for a type save
     *             variant of this method
     */
    @Deprecated
    public void process(String[] zipNames, String[] jarNames, String destDir) throws IOException {
        //Delegation to preferred method
        process(FileUtils.pathNamesToFiles(zipNames), FileUtils.pathNamesToFiles(jarNames), new File(destDir));
    }

    /**
     * Explode source JAR and/or ZIP files into a target directory
     * 
     * @param zipFiles
     *            source files
     * @param jarFiles
     *            source files
     * @param destDir
     *            target directory (should already exist)
     * @exception IOException
     *                error creating a target file
     */
    public void process(File[] zipFiles, File[] jarFiles, File destDir) throws IOException {
        processZips(zipFiles, destDir);
        processJars(jarFiles, destDir);
    }

    /**
     * Explode source JAR files into a target directory
     * 
     * @param jarNames
     *            names of source files
     * @param destDir
     *            target directory name (should already exist)
     * @exception IOException
     *                error creating a target file
     * @deprecated use {@link #processJars(File[], File)} for a type save
     *             variant
     */
    @Deprecated
    public void processJars(String[] jarNames, String destDir) throws IOException {
        //Delegation to preferred method
        processJars(FileUtils.pathNamesToFiles(jarNames), new File(destDir));
    }

    /**
     * Explode source JAR files into a target directory
     * 
     * @param jarFiles
     *            list of source files
     * @param destDir
     *            target directory name (should already exist)
     * @exception IOException
     *                error creating a target file
     */
    public void processJars(File[] jarFiles, File destDir) throws IOException {
        for (int i = 0; i < jarFiles.length; i++) {
            processFile(jarFiles[i], destDir);
        }
    }

    /**
     * Explode source ZIP files into a target directory
     * 
     * @param zipNames
     *            names of source files
     * @param destDir
     *            target directory name (should already exist)
     * @exception IOException
     *                error creating a target file
     * @deprecated use {@link #processZips(File[], File)} for a type save
     *             variant
     */
    @Deprecated
    public void processZips(String[] zipNames, String destDir) throws IOException {
        //Delegation to preferred method
        processZips(FileUtils.pathNamesToFiles(zipNames), new File(destDir));
    }

    /**
     * Explode source ZIP files into a target directory
     * 
     * @param zipFiles
     *            list of source files
     * @param destDir
     *            target directory name (should already exist)
     * @exception IOException
     *                error creating a target file
     */
    public void processZips(File[] zipFiles, File destDir) throws IOException {
        for (int i = 0; i < zipFiles.length; i++) {
            processFile(zipFiles[i], destDir);
        }
    }

    /**
     * Explode source ZIP or JAR file into a target directory
     * 
     * @param zipName
     *            names of source file
     * @param destDir
     *            target directory name (should already exist)
     * @exception IOException
     *                error creating a target file
     * @deprecated use {@link #processFile(File, File)} for a type save variant
     */
    @Deprecated
    public void processFile(String zipName, String destDir) throws IOException {
        //Delegation to preferred method
        processFile(new File(zipName), new File(destDir));
    }

    /**
     * Explode source ZIP or JAR file into a target directory
     * 
     * @param zipFile
     *            source file
     * @param destDir
     *            target directory (should already exist)
     * @exception IOException
     *                error creating a target file
     */
    public void processFile(File zipFile, File destDir) throws IOException {
        if (!destDir.exists()) {
            throw new IOException("Destionation directory '" + destDir + "' does not exists, or can't be read!");
        }
        ZipFile f = null;
        try {
            f = new ZipFile(zipFile);
            Map<String, ZipEntry> fEntries = getEntries(f);
            String[] names = fEntries.keySet().toArray(new String[] {});
            if (sortNames) {
                Arrays.sort(names);
            }
            // copy all files
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                ZipEntry e = fEntries.get(name);
                copyFileEntry(destDir, f, e);
            }
        } catch (IOException ioe) {
            String msg = ioe.getMessage();
            if (msg.indexOf(zipFile.toString()) < 0) {
                msg += " - " + zipFile.toString();
            }
            throw new IOException(msg);
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    /** Get all the entries in a ZIP file. */
    protected Map<String, ZipEntry> getEntries(ZipFile zf) {
        Enumeration<?> e = zf.entries();
        Map<String, ZipEntry> m = new HashMap<String, ZipEntry>();
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
            m.put(ze.getName(), ze);
        }
        return m;
    }

    /**
     * copy a single entry from the archive
     * 
     * @param destDir
     * @param zf
     * @param ze
     * @throws IOException
     * @deprecated use {@link #copyFileEntry(File, ZipFile, ZipEntry)} for a
     *             type save variant
     */
    @Deprecated
    public void copyFileEntry(String destDir, ZipFile zf, ZipEntry ze) throws IOException {
        //Delegation to preferred method
        copyFileEntry(new File(destDir), zf, ze);
    }

    /**
     * copy a single entry from the archive
     * 
     * @param destDir
     * @param zf
     * @param ze
     * @throws IOException
     */
    public void copyFileEntry(File destDir, ZipFile zf, ZipEntry ze) throws IOException {
        BufferedInputStream dis = new BufferedInputStream(zf.getInputStream(ze));
        try {
            copyFileEntry(destDir, ze.isDirectory(), ze.getName(), dis);
        } finally {
            try {
                dis.close();
            } catch (IOException ioe) {
            }
        }
    }

    protected void copyFileEntry(File destDir, boolean destIsDir, String destFileName, InputStream dis) throws IOException {
        File file = new File(destFileName);
        String parent = file.getParent();
        if (parent != null && parent.length() > 0) {
            File dir = new File(destDir, parent);
            if (dir != null) {
                dir.mkdirs();
            }
        }
        File outFile = new File(destDir, destFileName);
        if (destIsDir) {
            outFile.mkdir();
        } else {
            StreamUtils.copyStream(dis, new FileOutputStream(outFile), true);
        }
    }

    protected void print(String s) {
        System.out.print(s);
    }

    /** Print command help text. */
    protected static void printHelp() {
        System.out.println();
        System.out.println("Usage: java " + ZipExploder.class.getName() + " (-jar jarFilename... | -zip zipFilename...)... -dir destDir {-verbose}");
        System.out.println("Where:");
        System.out.println("  jarFilename path to source jar, may repeat");
        System.out.println("  zipFilename path to source zip, may repeat");
        System.out.println("  destDir    path to target directory; should exist");
        System.out.println("Note: one -jar or -zip is required; switch case or order is not important");
    }

    protected static void reportError(String msg) {
        System.err.println(msg);
        // printHelp();
        System.exit(1);
    }

    /**
     * Main command line entry point.
     * 
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            printHelp();
            System.exit(0);
        }
        List<String> zipNames = new ArrayList<String>();
        List<String> jarNames = new ArrayList<String>();
        String destDir = null;
        boolean jarActive = false, zipActive = false, destDirActive = false;
        boolean verbose = false;
        // process arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') { // switch
                arg = arg.substring(1);
                if (arg.equalsIgnoreCase("jar")) {
                    jarActive = true;
                    zipActive = false;
                    destDirActive = false;
                } else if (arg.equalsIgnoreCase("zip")) {
                    zipActive = true;
                    jarActive = false;
                    destDirActive = false;
                } else if (arg.equalsIgnoreCase("dir")) {
                    jarActive = false;
                    zipActive = false;
                    destDirActive = true;
                } else if (arg.equalsIgnoreCase("verbose")) {
                    verbose = true;
                } else {
                    reportError("Invalid switch - " + arg);
                }
            } else {
                if (jarActive) {
                    jarNames.add(arg);
                } else if (zipActive) {
                    zipNames.add(arg);
                } else if (destDirActive) {
                    if (destDir != null) {
                        reportError("duplicate argument - " + "-destDir");
                    }
                    destDir = arg;
                } else {
                    reportError("Too many parameters - " + arg);
                }
            }
        }
        if (destDir == null || (zipNames.size() + jarNames.size()) == 0) {
            reportError("Missing parameters");
        }
        if (verbose) {
            System.out.println("Effective command: " + ZipExploder.class.getName() + " " + (jarNames.size() > 0 ? "-jars " + jarNames + " " : "")
                    + (zipNames.size() > 0 ? "-zips " + zipNames + " " : "") + "-dir " + destDir);
        }
        try {
            ZipExploder ze = new ZipExploder(verbose);
            ze.process(FileUtils.pathNamesToFiles(zipNames.toArray(new String[zipNames.size()])), FileUtils.pathNamesToFiles(jarNames.toArray(new String[jarNames.size()])), new File(destDir));
        } catch (IOException ioe) {
            System.err.println("Exception - " + ioe.getMessage());
            ioe.printStackTrace(); // *** debug ***
            System.exit(2);
        }
    }
}
