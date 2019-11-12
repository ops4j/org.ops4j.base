package org.ops4j.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

public class ZipExploderTest
{

    @Test
    public void testExplodeZip() throws IOException
    {
        File file = FileUtils.getFileFromClasspath("dirscanner.zip");
        assertNotNull(file);

        File destDir = Files.createTempDirectory("dirscanner").toFile();

        // Explode the zip to a tempdir
        ZipExploder exploder = new ZipExploder();
        exploder.processFile(file, destDir);

        File[] files = destDir.listFiles();
        assertEquals(3, files.length);

        // Delete the tempdir
        assertTrue(FileUtils.delete(destDir));
    }

    @Test
    public void testZipSlip() throws IOException
    {
        File file = FileUtils.getFileFromClasspath("dirscanner-zipslip.zip");
        assertNotNull(file);

        File destDir = Files.createTempDirectory("zipslip").toFile();

        // Explode the zip to a tempdir
        ZipExploder exploder = new ZipExploder();
        try {
            exploder.processFile(file, destDir);
            fail("Failure expected on a zip slip attack");
        } catch (IOException ex) {
            // expected
        }

        // Delete the tempdir
        assertTrue(FileUtils.delete(destDir));
    }

}
