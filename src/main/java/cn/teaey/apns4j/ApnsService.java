/*
 *
 *  * Copyright 2015 The Apns4j Project
 *  *
 *  * The Netty Project licenses this file to you under the Apache License,
 *  * version 2.0 (the "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at:
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  * License for the specific language governing permissions and limitations
 *  * under the License.
 *
 */

package cn.teaey.apns4j;

import cn.teaey.apns4j.network.*;
import cn.teaey.apns4j.network.async.ApnsFuture;
import cn.teaey.apns4j.network.async.PayloadSender;
import cn.teaey.apns4j.network.async.AsyncServiceShutdownException;
import cn.teaey.apns4j.protocol.NotifyPayload;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author teaey
 * @date 13-8-31
 * @since 1.0.0
 */
public class ApnsService implements PayloadSender<NotifyPayload> {
    private static final int processors = Runtime.getRuntime().availableProcessors();
    private final int size;
    private final ApnsChannelFactory apnsChannelFactory;
    private final ExecutorService executorService;
    private final ExecutorService errorExecutor;
    private final AtomicBoolean START = new AtomicBoolean(true);
    private final int tryTimes;
    private final ThreadLocal<PayloadSender<NotifyPayload>> threadSelfConnection = new ThreadLocal() {
        protected PayloadSender<NotifyPayload> initialValue() {
            return apnsChannelFactory.newChannel();
        }
    };

    private ApnsService(int executorSize, ApnsChannelFactory apnsChannelFactory, int tryTimes) {
        this.size = (executorSize > processors) ? processors : executorSize;
        this.apnsChannelFactory = apnsChannelFactory;
        this.executorService = Executors.newFixedThreadPool(this.size);
        this.errorExecutor = Executors.newFixedThreadPool(1);
        this.tryTimes = tryTimes;
    }
    public static ApnsService newApnsService(int executorSize, ApnsChannelFactory apnsChannelFactory) {
        return newApnsService(executorSize, apnsChannelFactory, ApnsChannel.DEFAULT_TRY_TIMES);
    }

    public static ApnsService newApnsService(int executorSize, ApnsChannelFactory apnsChannelFactory, int tryTimes) {
        return new ApnsService(executorSize, apnsChannelFactory, tryTimes);
    }

    public ApnsFuture sendAndFlush(String deviceToken, NotifyPayload payload) {
        return this.sendAndFlush(ApnsHelper.toByteArray(deviceToken), payload);
    }

    public ApnsFuture sendAndFlush(byte[] deviceToken, NotifyPayload payload) {
        if (!START.get()) {
            throw new AsyncServiceShutdownException("AsynService has shutdown");
        }
        Future f = executorService.submit(new APNSAsynTask(deviceToken, payload));
        return new ApnsFuture(f, deviceToken, payload);
    }

    /**
     * <p>shutdown.</p>
     */
    public void shutdown() {
        if (START.compareAndSet(true, false)) {
            executorService.shutdown();
        }
    }

    //private static final Logger log = LoggerFactory.getLogger(APNSAsynService.class);
    class APNSAsynTask implements Callable<Object> {
        private final byte[] deviceToken;
        private final NotifyPayload payload;

        public APNSAsynTask(byte[] deviceToken, NotifyPayload payload) {
            this.deviceToken = deviceToken;
            this.payload = payload;
        }

        @Override
        public Object call() {
            try {
                return threadSelfConnection.get().sendAndFlush(deviceToken, payload);
            } catch (Exception e) {
                //log.error("Failed cause IOException deviceToken={} payload={}", deviceToken, payload.toJsonString(), e);
                return e;
            }
        }
    }
}
