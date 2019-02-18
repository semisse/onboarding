package org.nuxeo.onboarding.product.operations;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.onboarding.product.adapters.ProductAdapterAdapter;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy({"org.nuxeo.onboarding.product.onboarding-core", "studio.extensions.sfialho-SANDBOX"})
public class TestAddVat {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Test
    public void testSingleProduct() throws OperationException {
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        doc = session.createDocument(doc);
        ProductAdapterAdapter product = doc.getAdapter(ProductAdapterAdapter.class);
        product.setDummyData();


        OperationContext ctx = new OperationContext(session);

        ctx.setInput(doc);
        automationService.run(ctx, AddVat.ID);

        DocumentModel returnedProduct = session.getDocument(new PathRef("/ProductTest"));

        Double priceWithVat = (Double) returnedProduct.getPropertyValue("product_schema:price");

        Assert.assertNotNull(returnedProduct);
        Assert.assertEquals(12.3, priceWithVat, 0.01);
    }

    @Test
    public void testSingleProductWithoutPrice() throws OperationException {
        DocumentModel product = session.createDocumentModel("/", "ProductTest", "product");
        product = session.createDocument(product);
        product.setPropertyValue("dc:title", "Product Test One");
        product = session.saveDocument(product);

        OperationContext ctx = new OperationContext(session);

        ctx.setInput(product);
        DocumentModel returnedProduct = (DocumentModel) automationService.run(ctx, AddVat.ID);

        Double priceWithVat = (Double) returnedProduct.getPropertyValue("product_schema:price");

        Assert.assertNotNull(returnedProduct);
        Assert.assertEquals(1.23, priceWithVat, 0.01);
    }

    @Test
    public void testAddVat() throws OperationException {
        DocumentModelList listWithProducts = new DocumentModelListImpl();
        for (int i = 1; i < 6; i++) {
            DocumentModel product = session.createDocumentModel("/", "ProductTest" + i, "product");
            product = session.createDocument(product);
            product.setPropertyValue("dc:title", "Product Test One");
            product.setPropertyValue("product_schema:price", 10d + i);
            product = session.saveDocument(product);
            listWithProducts.add(product);
        }

        OperationContext ctx = new OperationContext(session);

        ctx.setInput(listWithProducts);
        DocumentModelList returnedProductList = (DocumentModelList) automationService.run(ctx, AddVat.ID);

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
            DocumentModel product = session.createDocumentModel("/", "ProductTest" + i, "product");
            product = session.createDocument(product);
            product.setPropertyValue("dc:title", "Product Test One");
            product.setPropertyValue("product_schema:price", 10d + i);
            product = session.saveDocument(product);
            listWithProducts.add(product);
        }

        OperationContext ctx = new OperationContext(session);

        ctx.setInput(listWithProducts);
        DocumentModelList returnedProductList = (DocumentModelList) automationService.run(ctx, AddVat.ID);

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