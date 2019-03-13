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

package org.nuxeo.onboarding.product.adapters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static org.nuxeo.onboarding.product.utils.DummyData.*;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
@Deploy("org.nuxeo.ecm.platform.filemanager.core")
public class TestVisualAdapter {
    @Inject
    protected CoreSession session;
    protected File getTestFile(String relativePath) {
        return FileUtils.getResourceFileFromContext(relativePath);
    }
    protected static final String DOCUMENT_TYPE_VISUAL = "visual";

    @Test
    public void shouldCallTheVisualAdapterAndSetProperties() throws IOException {
        File file = getTestFile(SAMPLE_JPEG);
        Blob originalBlob = Blobs.createBlob(file, "image/jpeg", null, null);
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_VISUAL, DOCUMENT_TYPE_VISUAL);
        VisualAdapter visualAdapter = doc.getAdapter(VisualAdapter.class);
        visualAdapter.setTitle(DOCUMENT_TITLE);
        visualAdapter.setDescription(DESCRIPTION);
        visualAdapter.setFileContent((Serializable) originalBlob);
        visualAdapter.save();
        Assert.assertEquals(DOCUMENT_TITLE, visualAdapter.getTitle());
        Assert.assertEquals(DESCRIPTION, visualAdapter.getDescription());
        Assert.assertEquals(originalBlob, visualAdapter.getFileContent());
    }
}
