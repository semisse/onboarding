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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Features({OnboardingTestFeature.class})
public class TestCalculateVAT {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Test
    public void shouldVATbeUpdatedinOneProduct() throws OperationException {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        doc = session.createDocument(doc);
        ProductAdapter product = doc.getAdapter(ProductAdapter.class);
        product.setDocumentTitle("Test Product");
        product.setDocumentPrice(10d);

        OperationContext ctx = new OperationContext(session);

        ctx.setInput(doc);
        automationService.run(ctx, CalculateVAT.ID);

        DocumentModel returnedProduct = session.getDocument(new PathRef("/ProductTest"));

        Double priceWithVat = (Double) returnedProduct.getPropertyValue("product_schema:price");

        Assert.assertNotNull(returnedProduct);
        Assert.assertEquals(12.3, priceWithVat, 0.01);
    }

    @Test(expected = NuxeoException.class)
    public void shouldDocumentTypeNotBeProductThrowException() throws OperationException {
        DocumentModel doc = session.createDocumentModel("/", "VisualTest", "visual");
        OperationContext ctx = new OperationContext(session);
        ctx.setInput(doc);
        automationService.run(ctx, CalculateVAT.ID);
    }


    @Test
    public void shouldProductWithoutVATbeUpdatedWithDefaultValue() throws OperationException {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        doc = session.createDocument(doc);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setDocumentTitle("Test Product");
        productAdapter.setDocumentPrice(10d);
        doc = session.saveDocument(doc);

        OperationContext ctx = new OperationContext(session);

        ctx.setInput(doc);
        DocumentModel returnedProduct = (DocumentModel) automationService.run(ctx, CalculateVAT.ID);

        Double priceWithVat = (Double) returnedProduct.getPropertyValue("product_schema:price");

        Assert.assertNotNull(returnedProduct);
        Assert.assertEquals(12.3, priceWithVat, 0.01);
    }

    @Test
    public void shouldVATbeUpdatedInSeveralProducts() throws OperationException {
        DocumentModelList listWithProducts = new DocumentModelListImpl();
        for (int i = 1; i < 6; i++) {
            DocumentModel doc = session.createDocumentModel("/", "ProductTest" + i, "product");
            doc = session.createDocument(doc);
            ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
            productAdapter.setDocumentTitle("Test Product");
            productAdapter.setDocumentPrice(10d + i);
            doc = session.saveDocument(doc);
            listWithProducts.add(doc);
        }

        OperationContext ctx = new OperationContext(session);

        ctx.setInput(listWithProducts);
        DocumentModelList returnedProductList = (DocumentModelList) automationService.run(ctx, CalculateVAT.ID);

        Assert.assertNotNull(returnedProductList);
        Assert.assertEquals(listWithProducts.size(), returnedProductList.size());

        int index = 0;
        for (DocumentModel productItem : returnedProductList) {
            index++;

            Double priceWithoutVat = 10d + index;
            Double priceTestWithVat = priceWithoutVat * 1.23;

            Double priceWithVat = (Double) productItem.getPropertyValue("product_schema:price");
            Assert.assertNotNull(priceWithVat);
            Assert.assertEquals(priceTestWithVat, priceWithVat, 0.01);
        }
    }

    @Test
    public void testUpdateProducts() throws OperationException {
        DocumentModelList listWithProducts = new DocumentModelListImpl();
        for (int i = 1; i < 6; i++) {
            DocumentModel doc = session.createDocumentModel("/", "ProductTest" + i, "product");
            doc = session.createDocument(doc);
            ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
            productAdapter.setDocumentTitle("Test Product");
            productAdapter.setDocumentPrice(10d + i);
            doc = session.saveDocument(doc);
            listWithProducts.add(doc);
        }

        OperationContext ctx = new OperationContext(session);

        ctx.setInput(listWithProducts);
        DocumentModelList returnedProductList = (DocumentModelList) automationService.run(ctx, CalculateVAT.ID);

        Assert.assertNotNull(returnedProductList);
        Assert.assertEquals(listWithProducts.size(), returnedProductList.size());

        int index = 0;
        for (DocumentModel productItem : returnedProductList) {
            index++;

            Double priceWithoutVat = 10d + index;
            Double priceTestWithVat = priceWithoutVat * 1.23;

            Double priceWithVat = (Double) productItem.getPropertyValue("product_schema:price");
            Assert.assertNotNull(priceWithVat);
            Assert.assertEquals(priceTestWithVat, priceWithVat, 0.01);
        }

    }
}