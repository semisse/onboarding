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
    protected static final String COUNTRY_NAME = "PT";
    protected static final String ID_NOT_FOUND = "Could not find any ID's in the contribution file";
    private Map<String, VatValueDescriptor> countriesVat = new HashMap<>();

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        VatValueDescriptor descriptor = (VatValueDescriptor) contribution;
        if (countriesVat.containsKey(descriptor.getId())) {
            VatValueDescriptor existingDescriptor = countriesVat.get(descriptor.getId());
            existingDescriptor.merge(descriptor);
        } else if (!countriesVat.containsKey(descriptor.getId())) {
            countriesVat.put(descriptor.getId(), descriptor);
        }
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        VatValueDescriptor descriptor = (VatValueDescriptor) contribution;
        countriesVat.remove(descriptor.getId());
    }

    @Override
    public Double computePrice(DocumentModel doc) throws NuxeoException {
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        String distributorCountry = productAdapter.getDistributorLocation();
        Double price = productAdapter.getPrice();

        if (distributorCountry != null && countriesVat.get(distributorCountry) != null) {
            return price * countriesVat.get(distributorCountry).getVatValue();
        } else if (countriesVat.get(distributorCountry) == null) {
            return price * countriesVat.get(COUNTRY_NAME).getVatValue();
        } else {
            throw new NuxeoException(ID_NOT_FOUND);
        }
    }
}