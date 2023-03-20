/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.server.frontend.installer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.vaadin.flow.server.frontend.FrontendTools;

public class DefaultFileDownloaderTest {

    @Rule
    public final TemporaryFolder tmpDir = new TemporaryFolder();

    private String baseDir;

    @Before
    public void setup() {
        baseDir = tmpDir.getRoot().getAbsolutePath();
    }

    @Test
    public void installNodeFromFileSystem_NodeIsInstalledToTargetDirectory()
            throws IOException, DownloadException {
        File targetDir = new File(baseDir + "/installation");

        Assert.assertFalse(
                "Clean test should not contain a installation folder",
                targetDir.exists());
        File downloadDir = tmpDir.newFolder(FrontendTools.DEFAULT_NODE_VERSION);
        String downloadFileName = "MyDownload.zip";

        File archiveFile = new File(downloadDir, downloadFileName);
        archiveFile.createNewFile();
        Path tempArchive = archiveFile.toPath();

        DefaultFileDownloader downloader = new DefaultFileDownloader(
                new ProxyConfig(Collections.emptyList()));

        downloader.download(tempArchive.toUri(),
                new File(targetDir, downloadFileName), null, null);

        Assert.assertTrue("File was not 'downloaded' to target directory",
                new File(targetDir, downloadFileName).exists());
        Assert.assertFalse(
                "File 'downloaded' was a directory event though file expected",
                new File(targetDir, downloadFileName).isDirectory());
    }
}
