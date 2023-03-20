/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.data.provider;

import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableConsumer;

/**
 * Abstract lazy data view implementation which handles the interaction with a
 * data communicator.
 *
 * @param <T>
 *            the type of data
 */
public abstract class AbstractLazyDataView<T> extends AbstractDataView<T>
        implements LazyDataView<T> {

    private final DataCommunicator<T> dataCommunicator;

    /**
     * Creates a new instance and verifies the passed data provider is
     * compatible with this data view implementation.
     *
     * @param dataCommunicator
     *            the data communicator of the component
     * @param component
     *            the component
     */
    public AbstractLazyDataView(DataCommunicator<T> dataCommunicator,
            Component component) {
        super(dataCommunicator::getDataProvider, component);
        this.dataCommunicator = dataCommunicator;
    }

    /**
     * Returns the data communicator for the component and checks that the data
     * provider is of the correct type.
     *
     * @return the data communicator
     */
    protected DataCommunicator<T> getDataCommunicator() {
        verifyDataProviderType(dataCommunicator.getDataProvider().getClass());
        return dataCommunicator;
    }

    /**
     * Gets the item at the given index from the data available to the
     * component. Data is filtered and sorted the same way as in the component.
     * <p>
     * Calling this method with an index that is not currently active in the
     * component will cause a query to the backend, so do not call this method
     * carelessly. Use
     * {@link com.vaadin.flow.component.UI#beforeClientResponse(Component, SerializableConsumer)}
     * to access items that will be fetched later on.
     * 
     * @param index
     *            the index of the item to get
     * @return item on index
     * @throws IndexOutOfBoundsException
     *             requested index is outside of the filtered and sorted data
     *             set
     */
    @Override
    public T getItem(int index) {
        return getDataCommunicator().getItem(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<T> getItems() {
        DataCommunicator<T> verifiedDataCommunicator = getDataCommunicator();
        if (verifiedDataCommunicator.isDefinedSize()) {
            return verifiedDataCommunicator.getDataProvider()
                    .fetch(verifiedDataCommunicator.buildQuery(0,
                            verifiedDataCommunicator.getItemCount()));
        } else {
            return verifiedDataCommunicator.getDataProvider().fetch(
                    verifiedDataCommunicator.buildQuery(0, Integer.MAX_VALUE));
        }
    }

    @Override
    protected Class<?> getSupportedDataProviderType() {
        return BackEndDataProvider.class;
    }

    @Override
    public void setItemCountEstimate(int itemCountEstimate) {
        getDataCommunicator().setItemCountEstimate(itemCountEstimate);
    }

    @Override
    public int getItemCountEstimate() {
        return getDataCommunicator().getItemCountEstimate();
    }

    @Override
    public void setItemCountEstimateIncrease(int itemCountEstimateIncrease) {
        getDataCommunicator()
                .setItemCountEstimateIncrease(itemCountEstimateIncrease);
    }

    @Override
    public int getItemCountEstimateIncrease() {
        return getDataCommunicator().getItemCountEstimateIncrease();
    }

    @Override
    public void setItemCountFromDataProvider() {
        getDataCommunicator().setDefinedSize(true);
    }

    @Override
    public void setItemCountUnknown() {
        getDataCommunicator().setDefinedSize(false);
    }

}
