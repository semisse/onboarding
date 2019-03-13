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

package org.nuxeo.onboarding.product.services;

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import static org.nuxeo.onboarding.product.utils.DummyData.*;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestProductService {
    DocumentModel productDocument;
    ProductAdapter productAdapter;
    protected static final String DOCUMENT_TYPE_PRODUCT = "product";

    @Inject
    protected ProductService productservice;
    @Inject
    protected CoreSession session;

    @Before
    public void setUp() {
        productDocument = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT, DOCUMENT_TYPE_PRODUCT);
        productAdapter = productDocument.getAdapter(ProductAdapter.class);
        productAdapter.setTitle(DOCUMENT_TITLE);
        productAdapter.setPrice(PRICE);
        productAdapter.setDistributor(DISTRIBUTOR_NAME, DISTRIBUTOR_LOCATION_PT);
        productAdapter.save();
    }

    @Test(expected = NuxeoException.class)
    public void shouldThrowExceptionWhenDocIsNotProduct() {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_VISUAL, "File");
        productservice.computePrice(doc);
    }

    @Test
    public void shouldUsePTVATWhenThereIsNoDistributorLocation() {
        productAdapter.setDistributor(DISTRIBUTOR_NAME, "");
        Double docWithCalculatedVAT = productservice.computePrice(productAdapter.getDoc());
        Assert.assertEquals(12.3d, docWithCalculatedVAT, 0.1);
    }

    @Test
    public void shouldUsePTVATWhenLocationIsNotFound() {
        productAdapter.setDistributor(DISTRIBUTOR_NAME, DISTRIBUTOR_LOCATION_US);
        productAdapter.save();
        Double docWithCalculatedVAT = productservice.computePrice(productAdapter.getDoc());
        Assert.assertEquals(12.3d, docWithCalculatedVAT, 0.1);
    }

    @Test
    public void shouldCalculatePriceWithVAT() {
        productAdapter.setDistributor(DISTRIBUTOR_NAME, DISTRIBUTOR_LOCATION_DE);
        productAdapter.save();
        Double docWithCalculatedVAT = productservice.computePrice(productAdapter.getDoc());
        Assert.assertEquals(11.9d, docWithCalculatedVAT, 0.1);
    }

    @Test
    @Deploy("org.nuxeo.onboarding.product.onboarding-core:OSGI-INF/vat-value-descriptor-contrib-test.xml")
    public void shouldLoadNewContributions() {
        // New contribution with existing key.
        // Should apply the old value, since the new is lower.
        Double docWithCalculatedVAT = productservice.computePrice(productAdapter.getDoc());
        Assert.assertEquals(12.3d, docWithCalculatedVAT, 0.1);

        // Testing with a non-existing key.
        DocumentModel newDoc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT, DOCUMENT_TYPE_PRODUCT);
        ProductAdapter newProductAdapter = newDoc.getAdapter(ProductAdapter.class);
        newProductAdapter.setTitle(DOCUMENT_TITLE);
        newProductAdapter.setPrice(PRICE);
        newProductAdapter.setDistributor(DISTRIBUTOR_NAME, DISTRIBUTOR_LOCATION_FR);
        newProductAdapter.save();
        Double newDocWithCalculatedVAT = productservice.computePrice(newDoc);
        Assert.assertEquals(12d, newDocWithCalculatedVAT, 0.1);
    }
    @Test(expected = NuxeoException.class)
    @Deploy("org.nuxeo.onboarding.product.onboarding-core:OSGI-INF/vat-value-descriptor-error-contrib-test.xml")
    public void shouldThrowAnNuxeoExceptionIfThereAreNoIdsInContributionFile () {
        DocumentModel newDoc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT, DOCUMENT_TYPE_PRODUCT);
        ProductAdapter newProductAdapter = newDoc.getAdapter(ProductAdapter.class);
        productservice.computePrice(newProductAdapter.getDoc());
    }

}