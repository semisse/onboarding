/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Samuel Fialho
 */

package org.nuxeo.onboarding.product.extensions;

import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.filemanager.api.FileImporterContext;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.nuxeo.onboarding.product.utils.DummyData.*;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
@Deploy("org.nuxeo.ecm.platform.filemanager.core")
public class TestVisualImporter {
    @Inject
    protected FileManager fileManager;
    @Inject
    protected CoreSession session;
    protected DocumentModel workspace;
    protected File getTestFile(String relativePath) {
        return FileUtils.getResourceFileFromContext(relativePath);
    }
    protected final static String MIME_TYPE = "application/jpeg";
    protected final static String DUBLIN_CORE = "dublincore";
    protected final static String TITLE = "title";
    protected final static String FILE = "file";
    protected final static String CONTENT = "content";
    protected final static String NEW_TITLE = "sample_COPY.jpeg";

    @Before
    public void setUp() {
        workspace = session.createDocumentModel(WORKSPACE_ROOT, WORKSPACE_NAME, WORKSPACE_TITLE);
        workspace = session.createDocument(workspace);
    }

    @Test
    public void shouldImportVisual() throws IOException {
        File file = getTestFile(SAMPLE_JPEG);
        Blob input = Blobs.createBlob(file, MIME_TYPE);

        FileImporterContext context = FileImporterContext.builder(session, input, workspace.getPathAsString())
                .overwrite(true)
                .fileName(SAMPLE_JPEG)
                .build();
        DocumentModel doc = fileManager.createOrUpdateDocument(context);
        assertNotNull(doc);
        assertEquals(SAMPLE_JPEG, doc.getProperty(DUBLIN_CORE, TITLE));
        Blob blob = (Blob) doc.getProperty(FILE, CONTENT);
        assertNotNull(blob);
        assertEquals(SAMPLE_JPEG, blob.getFilename());
    }

    @Test
    public void shouldImportDuplicatedVisualAndRenameDocument() throws IOException {
        File file = getTestFile(SAMPLE_JPEG);
        Blob input = Blobs.createBlob(file, MIME_TYPE);
        FileImporterContext context = FileImporterContext.builder(session, input, workspace.getPathAsString())
                .overwrite(true)
                .fileName(SAMPLE_JPEG)
                .build();
        DocumentModel doc = fileManager.createOrUpdateDocument(context);
        session.save();
        assertNotNull(doc);
        assertEquals(SAMPLE_JPEG, doc.getProperty(DUBLIN_CORE, TITLE));
        Blob blob = (Blob) doc.getProperty(FILE, CONTENT);
        assertNotNull(blob);
        assertEquals(SAMPLE_JPEG, blob.getFilename());

        // create again with same file
        DocumentModel duplicatedDocument = fileManager.createOrUpdateDocument(context);
        assertNotNull(duplicatedDocument);
        assertEquals(NEW_TITLE, duplicatedDocument.getProperty(DUBLIN_CORE, TITLE));
        Blob duplicatedBlob = (Blob) duplicatedDocument.getProperty(FILE, CONTENT);
        assertNotNull(duplicatedBlob);
        assertEquals(SAMPLE_JPEG, duplicatedBlob.getFilename());
    }
}
