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
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

import static org.nuxeo.onboarding.product.utils.DummyData.*;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestCalculateVAT {
    protected final static Double PT_VAT = 1.23d;
    protected static final String DOCUMENT_TYPE_PRODUCT = "product";
    protected static final String DOCUMENT_TYPE_VISUAL = "visual";
    protected final static String PRODUCT_SCHEMA_PRICE = "product_schema:price";

    @Inject
    protected CoreSession session;
    @Inject
    protected AutomationService automationService;

    @Test
    public void shouldVATUpdateVatInOneProduct() throws OperationException {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT, DOCUMENT_TYPE_PRODUCT);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle(DOCUMENT_TITLE);
        productAdapter.setPrice(PRICE);
        productAdapter.save();

        OperationContext ctx = new OperationContext(session);
        ctx.setInput(productAdapter.getDoc());
        automationService.run(ctx, CalculateVAT.ID);

        DocumentModel returnedProduct = session.getDocument(productAdapter.getDocRef());
        Double priceWithVat = (Double) returnedProduct.getPropertyValue(PRODUCT_SCHEMA_PRICE);

        Assert.assertNotNull(returnedProduct);
        Assert.assertEquals(12.3, priceWithVat, 0.1);
    }

    @Test(expected = Exception.class)
    public void shouldThrowAnExceptionIfDocumentNotProduct() throws OperationException {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_VISUAL, DOCUMENT_TYPE_VISUAL);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        OperationContext ctx = new OperationContext(session);
        ctx.setInput(productAdapter);
        automationService.run(ctx, CalculateVAT.ID);
    }

    @Test
    public void shouldUpdateVatWithDefaultValueIfVatOnDocIsNotSet() throws OperationException {
        DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT, DOCUMENT_TYPE_PRODUCT);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle(DOCUMENT_TITLE);
        productAdapter.setPrice(PRICE);
        productAdapter.save();

        OperationContext ctx = new OperationContext(session);
        ctx.setInput(productAdapter.getDocRef());
        DocumentModel returnedProduct = (DocumentModel) automationService.run(ctx, CalculateVAT.ID);
        Double priceWithVat = (Double) returnedProduct.getPropertyValue(PRODUCT_SCHEMA_PRICE);

        Assert.assertNotNull(returnedProduct);
        Assert.assertEquals(12.3, priceWithVat, 0.1);
    }

    @Test
    public void shouldUpdateVatInMultipleProducts() throws OperationException {
        DocumentModelList listWithProducts = new DocumentModelListImpl();
        for (int i = 1; i < 6; i++) {
            DocumentModel doc = session.createDocumentModel(WORKSPACE_ROOT, DOCUMENT_NAME_PRODUCT + i, DOCUMENT_TYPE_PRODUCT);
            ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
            productAdapter.setTitle(DOCUMENT_TITLE);
            productAdapter.setPrice(PRICE + i);
            productAdapter.save();
            listWithProducts.add(productAdapter.getDoc());
        }

        OperationContext ctx = new OperationContext(session);
        ctx.setInput(listWithProducts);
        DocumentModelList returnedProductList = (DocumentModelList) automationService.run(ctx, CalculateVAT.ID);
        Assert.assertNotNull(returnedProductList);
        Assert.assertEquals(listWithProducts.size(), returnedProductList.size());

        int index = 0;
        for (DocumentModel productItem : returnedProductList) {
            index++;
            Double priceWithoutVat = PRICE + index;
            Double priceTestWithVat = priceWithoutVat * PT_VAT;
            Double priceWithVat = (Double) productItem.getPropertyValue(PRODUCT_SCHEMA_PRICE);
            Assert.assertNotNull(priceWithVat);
            Assert.assertEquals(priceTestWithVat, priceWithVat, 0.1);
        }
    }
}