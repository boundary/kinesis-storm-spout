/*
 * Copyright 2013-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.services.kinesis.stormspout;

import com.amazonaws.regions.Regions;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Kinesis Spout configuration.
 */
public class KinesisSpoutConfig implements Serializable {
    private static final long serialVersionUID = 3528909581809795597L;

    private final String streamName;
    private Regions region = Regions.DEFAULT_REGION;
    private int maxRecordsPerCall = 10000;
    private InitialPositionInStream initialPositionInStream = InitialPositionInStream.LATEST;
    private int checkpointIntervalMillis = 60000;

    private final String zookeeperConnectionString;
    private String zookeeperPrefix = "kinesis_storm_spout";
    private int zookeeperSessionTimeoutMillis = 10000;

    private IKinesisRecordScheme scheme = new DefaultKinesisRecordScheme();

    // Gets set by the spout later on.
    private String topologyName = "UNNAMED_TOPOLOGY";

    public KinesisSpoutConfig(final String streamName, final String zookeeperConnectionString) {
        this.streamName = streamName;
        this.zookeeperConnectionString = zookeeperConnectionString;
    }

    /**
     * Constructor.
     * 
     * @param streamName Name of the Kinesis stream.
     * @param maxRecordsPerCall Max number records to fetch from Kinesis in a single GetRecords call
     * @param initialPositionInStream Fetch records from this position if a checkpoint doesn't exist
     * @param zookeeperPrefix Prefix for Zookeeper paths when storing spout state.
     * @param zookeeperConnectionString Endpoint for connecting to ZooKeeper (e.g. "localhost:2181").
     * @param zookeeperSessionTimeoutMillis Timeout for ZK session.
     * @param checkpointIntervalMillis Save checkpoint to Zookeeper this often.
     * @param scheme Used to convert a Kinesis record into a tuple
     */
    public KinesisSpoutConfig(final String streamName,
            final int maxRecordsPerCall,
            final InitialPositionInStream initialPositionInStream,
            final String zookeeperPrefix,
            final String zookeeperConnectionString,
            final int zookeeperSessionTimeoutMillis,
            final int checkpointIntervalMillis,
            final IKinesisRecordScheme scheme) {
        this.scheme = scheme;
        this.streamName = streamName;
        checkValueIsPositive(maxRecordsPerCall, "maxRecordsPerCall");
        this.maxRecordsPerCall = maxRecordsPerCall;
        this.initialPositionInStream = initialPositionInStream;
        this.zookeeperPrefix = zookeeperPrefix;
        this.zookeeperConnectionString = zookeeperConnectionString;
        checkValueIsPositive(zookeeperSessionTimeoutMillis, "zookeeperSessionTimeoutMillis");
        this.zookeeperSessionTimeoutMillis = zookeeperSessionTimeoutMillis;
        checkValueIsPositive(checkpointIntervalMillis, "checkpointIntervalMillis");
        this.checkpointIntervalMillis = checkpointIntervalMillis;
    }

    private void checkValueIsPositive(int argument, String argumentName) {
        if (argument <= 0) {
            throw new IllegalArgumentException("Value of " + argumentName + " must be positive, but was " + argument);
        }
    }

    /**
     * @return Scheme used to convert a Kinesis record to a tuple.
     */
    public IKinesisRecordScheme getScheme() {
        return scheme;
    }

    /**
     * @param scheme Scheme used to convert a Kinesis record to a tuple.
     * @return KinesisSpoutConfig
     */
    public KinesisSpoutConfig withKinesisRecordScheme(IKinesisRecordScheme scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * @return Prefix used when storing spout state in Zookeeper.
     */
    public String getZookeeperPrefix() {
        return zookeeperPrefix;
    }

    /**
     * @param prefix Prefix (path) used when storing spout state in Zookeeper.
     * @return KinesisSpoutConfig
     */
    public KinesisSpoutConfig withZookeeperPrefix(String prefix) {
        this.zookeeperPrefix = prefix;
        return this;
    }

    /**
     * @return Zookeeper connection string.
     */
    public String getZookeeperConnectionString() {
        return zookeeperConnectionString;
    }

    /**
     * @return Zookeeper session timeout.
     */
    public int getZookeeperSessionTimeoutMillis() {
        return zookeeperSessionTimeoutMillis;
    }

    /**
     * @param zookeeperSessionTimeoutMillis Zookeeper session timeout
     * @return KinesisSpoutConfig
     */
    public KinesisSpoutConfig withZookeeperSessionTimeoutMillis(int zookeeperSessionTimeoutMillis) {
        checkValueIsPositive(zookeeperSessionTimeoutMillis, "zookeeperSessionTimeoutMillis");
        this.zookeeperSessionTimeoutMillis = zookeeperSessionTimeoutMillis;
        return this;
    }

    /**
     * @return Checkpoint interval (e.g. checkpoint every 30 seconds).
     */
    public int getCheckpointIntervalMillis() {
        return checkpointIntervalMillis;
    }

    /**
     * @param checkpointIntervalMillis Save checkpoint to Zookeeper this often.
     * @return KinesisSpoutConfig
     */
    public KinesisSpoutConfig withCheckpointIntervalMillis(int checkpointIntervalMillis) {
        checkValueIsPositive(checkpointIntervalMillis, "checkpointIntervalMillis");
        this.checkpointIntervalMillis = checkpointIntervalMillis;
        return this;
    }

    /**
     * @return Name of the Kinesis stream.
     */
    public String getStreamName() {
        return streamName;
    }

    /**
     * @return Name of the topology.
     */
    public String getTopologyName() {
        return topologyName;
    }

    /**
     * @param topologyName Name of the topology.
     */
    public void setTopologyName(final String topologyName) {
        this.topologyName = topologyName;
    }

    /**
     * @return the initialPositionInStream
     */
    public InitialPositionInStream getInitialPositionInStream() {
        return initialPositionInStream;
    }

    /**
     * @param initialPosition Fetch records from this position if a checkpoint doesn't exist
     * @return KinesisSpoutConfig
     */
    public KinesisSpoutConfig withInitialPositionInStream(InitialPositionInStream initialPosition) {
        this.initialPositionInStream = initialPosition;
        return this;
    }

    /**
     * @return maxRecordsPerCall (GetRecords API call)
     */
    public int getMaxRecordsPerCall() {
        return maxRecordsPerCall;
    }

    /**
     * @param maxRecordsPerCall Max number records to fetch from Kinesis in a single GetRecords call
     * @return KinesisSpoutConfig
     */
    public KinesisSpoutConfig withMaxRecordsPerCall(int maxRecordsPerCall) {
        checkValueIsPositive(maxRecordsPerCall, "maxRecordsPerCall");
        this.maxRecordsPerCall = maxRecordsPerCall;
        return this;
    }

    /**
     * @return region
     */
    public Regions getRegion() {
        return region;
    }

    /**
     * @param region to use for kinesis stream, defaults to @see com.amazonaws.regions.Region.DEFAULT_REGION
     * @return KinesisSpoutConfig
     */
    public KinesisSpoutConfig withRegion(Regions region) {
        this.region = checkNotNull(region);
        return this;
    }
}
