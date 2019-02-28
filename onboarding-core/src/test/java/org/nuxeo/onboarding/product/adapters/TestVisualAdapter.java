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
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestVisualAdapter {
    @Inject
    private CoreSession session;

    @Test
    public void shouldCallTheVisualAdapterAndSetProperties() {
        String doctype = "visual";
        String title = "Test Visual";
        String description = "This is a test description";

        DocumentModel doc = session.createDocumentModel("/", "ProductTest", doctype);
        doc = session.createDocument(doc);
        VisualAdapter visualAdapter = doc.getAdapter(VisualAdapter.class);
        visualAdapter.setTitle(title);
        visualAdapter.setDescription(description);
        visualAdapter.save();

        IdRef docIdRef = new IdRef(doc.getId());
        doc = session.getDocument(docIdRef);
        Assert.assertNotNull(doc);

        Assert.assertEquals(title, visualAdapter.getTitle());
        Assert.assertEquals(description, visualAdapter.getDescription());
    }
}
