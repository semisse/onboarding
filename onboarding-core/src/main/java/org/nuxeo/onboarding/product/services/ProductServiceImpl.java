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

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.onboarding.product.descriptors.VatValueDescriptor;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import java.util.HashMap;
import java.util.Map;

public class ProductServiceImpl extends DefaultComponent implements ProductService {

    protected Map<String, VatValueDescriptor> countriesVat = new HashMap<>();

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        VatValueDescriptor descriptor = (VatValueDescriptor) contribution;
        countriesVat.put(descriptor.getId(), descriptor);
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        VatValueDescriptor descriptor = (VatValueDescriptor) contribution;
        countriesVat.remove(descriptor.getId());
    }

    public Double computePrice(DocumentModel doc) throws NuxeoException {
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        String distributorCountry = productAdapter.getDistributorLocation();

        if (distributorCountry == null) {
            distributorCountry = "PT";
            Double price = productAdapter.getPrice();
            return price * countriesVat.get(distributorCountry).getVatValue();
        } else {
            Double price = productAdapter.getPrice();
            return price * countriesVat.get(distributorCountry).getVatValue();
        }
    }
}