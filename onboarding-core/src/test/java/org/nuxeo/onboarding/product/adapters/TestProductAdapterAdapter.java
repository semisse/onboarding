/*
 * (C) Copyright ${year} Nuxeo (http://nuxeo.com/) and others.
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
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy({"org.nuxeo.onboarding.product.onboarding-core", "studio.extensions.sfialho-SANDBOX"})
public class TestProductAdapterAdapter {
    @Inject
    CoreSession session;

    @Test
    public void shouldCallTheAdapter() {
        String doctype = "product";
        String testTitle = "My Adapter Title";

        DocumentModel doc = session.createDocumentModel("/", "test-adapter", doctype);
        ProductAdapterAdapter adapter = doc.getAdapter(ProductAdapterAdapter.class);
        adapter.setTitle(testTitle);
        Assert.assertEquals("Document title does not match when using the adapter", testTitle, adapter.getTitle());
    }
}