/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.percolate;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.support.broadcast.BroadcastOperationRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.internal.InternalClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.Map;

/**
 *
 */
public class PercolateRequestBuilder extends BroadcastOperationRequestBuilder<PercolateRequest, PercolateResponse, PercolateRequestBuilder> {

    private PercolateSourceBuilder sourceBuilder;

    public PercolateRequestBuilder(Client client) {
        super((InternalClient) client, new PercolateRequest());
    }

    /**
     * Sets the type of the document to percolate.
     */
    public PercolateRequestBuilder setDocumentType(String type) {
        request.documentType(type);
        return this;
    }

    /**
     * A comma separated list of routing values to control the shards the search will be executed on.
     */
    public PercolateRequestBuilder setRouting(String routing) {
        request.routing(routing);
        return this;
    }

    /**
     * List of routing values to control the shards the search will be executed on.
     */
    public PercolateRequestBuilder setRouting(String... routings) {
        request.routing(Strings.arrayToCommaDelimitedString(routings));
        return this;
    }

    /**
     * Sets the preference to execute the search. Defaults to randomize across shards. Can be set to
     * <tt>_local</tt> to prefer local shards, <tt>_primary</tt> to execute only on primary shards, or
     * a custom value, which guarantees that the same order will be used across different requests.
     */
    public PercolateRequestBuilder setPreference(String preference) {
        request.preference(preference);
        return this;
    }

    /**
     * Enables percolating an existing document. Instead of specifying the source of the document to percolate, define
     * a get request that will fetch a document and use its source.
     */
    public PercolateRequestBuilder setGetRequest(GetRequest getRequest) {
        request.getRequest(getRequest);
        return this;
    }

    /**
     * Whether only to return total count and don't keep track of the matches (Count percolation).
     */
    public PercolateRequestBuilder setOnlyCount(boolean onlyCount) {
        request.onlyCount(onlyCount);
        return this;
    }

    public PercolateRequestBuilder setSource(PercolateSourceBuilder source) {
        sourceBuilder = source;
        return this;
    }

    public PercolateRequestBuilder setSource(Map<String, Object> source) {
        request.source(source);
        return this;
    }

    public PercolateRequestBuilder setSource(Map<String, Object> source, XContentType contentType) {
        request.source(source, contentType);
        return this;
    }

    public PercolateRequestBuilder setSource(String source) {
        request.source(source);
        return this;
    }

    public PercolateRequestBuilder setSource(XContentBuilder sourceBuilder) {
        request.source(sourceBuilder);
        return this;
    }

    public PercolateRequestBuilder setSource(BytesReference source) {
        request.source(source, false);
        return this;
    }

    public PercolateRequestBuilder setSource(BytesReference source, boolean unsafe) {
        request.source(source, unsafe);
        return this;
    }

    public PercolateRequestBuilder setSource(byte[] source) {
        request.source(source);
        return this;
    }

    public PercolateRequestBuilder setSource(byte[] source, int offset, int length) {
        request.source(source, offset, length);
        return this;
    }

    public PercolateRequestBuilder setSource(byte[] source, int offset, int length, boolean unsafe) {
        request.source(source, offset, length, unsafe);
        return this;
    }

    public PercolateRequestBuilder setPercolateDoc(PercolateSourceBuilder.DocBuilder docBuilder) {
        sourceBuilder().setDoc(docBuilder);
        return this;
    }

    public PercolateRequestBuilder setPercolateQuery(QueryBuilder queryBuilder) {
        sourceBuilder().setQueryBuilder(queryBuilder);
        return this;
    }

    public PercolateRequestBuilder setPercolateFilter(FilterBuilder filterBuilder) {
        sourceBuilder().setFilterBuilder(filterBuilder);
        return this;
    }

    private PercolateSourceBuilder sourceBuilder() {
        if (sourceBuilder == null) {
            sourceBuilder = new PercolateSourceBuilder();
        }
        return sourceBuilder;
    }

    @Override
    protected void doExecute(ActionListener<PercolateResponse> listener) {
        if (sourceBuilder != null) {
            request.source(sourceBuilder);
        }
        ((Client) client).percolate(request, listener);
    }

}
