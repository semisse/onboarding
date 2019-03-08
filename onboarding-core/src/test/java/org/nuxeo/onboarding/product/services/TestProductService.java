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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.onboarding.product.descriptors.VatValueDescriptor;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestProductService {

    @Inject
    protected ProductService productservice;

    @Inject
    protected CoreSession session;

    @Inject
    protected VatValueDescriptor vatValueDescriptor;

    @Test(expected = NuxeoException.class)
    public void shouldThrowExceptionWhenDocIsNotProduct() {
        DocumentModel doc = session.createDocumentModel("/", "Visual Test", "File");
        Double docWithCalculatedVAT = productservice.computePrice(doc);
    }

    @Test
    public void shouldUsePTVATWhenThereIsNoDistributor() {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle("Test Product");
        productAdapter.setPrice(10d);
        productAdapter.save();
        Double docWithCalculatedVAT = productservice.computePrice(doc);
        Assert.assertEquals(12.3d, docWithCalculatedVAT, 1);
    }

    @Test
    public void shouldCalculatePriceWithVAT() {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle("Test Product");
        productAdapter.setPrice(10d);
        productAdapter.setDistributor("Test", "PT");
        productAdapter.save();

        Double docWithCalculatedVAT = productservice.computePrice(doc);
        Assert.assertEquals(12.3d, docWithCalculatedVAT, 1);
        productAdapter.setDistributor("Test", "ES");
        Assert.assertEquals(12.1d, docWithCalculatedVAT, 1);
        productAdapter.setDistributor("Test", "DE");
        Assert.assertEquals(11.9d, docWithCalculatedVAT, 1);
    }

    /*productAdapter.setDistributor("Test", "ES");
    Assert.assertEquals(12.3d, docWithCalculatedVAT, 1);
    productAdapter.setDistributor("Test", "DE");
    Assert.assertEquals(11.9d, docWithCalculatedVAT, 1);*/
    //@Deploy("")
}
