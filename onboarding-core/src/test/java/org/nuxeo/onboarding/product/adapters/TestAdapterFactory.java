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
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

import static org.nuxeo.onboarding.product.utils.DummyData.*;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestAdapterFactory {
    protected static final String DOCUMENT_TYPE_PRODUCT = "product";
    protected static final String DOCUMENT_TYPE_VISUAL = "visual";
    protected static final String DOCUMENT_TYPE_FILE = "File";

    @Inject
    protected CoreSession session;

    @Test(expected = NuxeoException.class)
    public void shouldNotCallTheAdapterIfNotProductOrVisual() {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_VISUAL, DOCUMENT_TYPE_FILE);
        doc.getAdapter(VisualAdapter.class);
    }

    @Test
    public void shouldCallAndGetTheProductAdapter() {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT, DOCUMENT_TYPE_PRODUCT);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        Assert.assertNotNull(productAdapter);
    }

    @Test
    public void shouldCallAndGetTheVisualAdapter() {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_VISUAL, DOCUMENT_TYPE_VISUAL);
        VisualAdapter visualAdapter = doc.getAdapter(VisualAdapter.class);
        Assert.assertNotNull(visualAdapter);
    }
}