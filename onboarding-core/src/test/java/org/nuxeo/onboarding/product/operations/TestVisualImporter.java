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

package org.nuxeo.onboarding.product.operations;

import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.*;
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

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
@Deploy("org.nuxeo.ecm.platform.filemanager.core")

public class TestVisualImporter {

    protected DocumentModel workspace;

    @Inject
    protected CoreSession session;
    @Inject
    protected FileManager fileManager;

    @Before
    public void setUp() throws Exception {
        workspace = session.createDocumentModel("/", "workspace", "Workspace");
        workspace = session.createDocument(workspace);
    }

    protected File getTestFile(String relativePath) {
        return FileUtils.getResourceFileFromContext(relativePath);
    }

    @Test
    public void shouldImportVisual() throws IOException {
        File file = getTestFile("sample.jpeg");
        Blob input = Blobs.createBlob(file, "application/jpeg");

        FileImporterContext context = FileImporterContext.builder(session, input, workspace.getPathAsString())
                .overwrite(true)
                .fileName("sample.jpeg")
                .build();
        DocumentModel doc = fileManager.createOrUpdateDocument(context);
        DocumentRef docRef = doc.getRef();

        assertNotNull(doc);
        assertEquals("sample.jpeg", doc.getProperty("dublincore", "title"));
        Blob blob = (Blob) doc.getProperty("file", "content");
        assertNotNull(blob);
        assertEquals("sample.jpeg", blob.getFilename());

        // create again with same file
        doc = fileManager.createOrUpdateDocument(context);
        assertNotNull(doc);

        assertEquals("sample_COPY.jpeg", doc.getProperty("dublincore", "title"));
        blob = (Blob) doc.getProperty("file", "content");
        assertNotNull(blob);
        assertEquals("sample.jpeg", blob.getFilename());
    }
}
