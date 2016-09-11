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

package cn.teaey.apns4j.network;

import cn.teaey.apns4j.keystore.InvalidKeyStoreException;
import cn.teaey.apns4j.keystore.KeyStoreWrapper;

/**
 * @author teaey(xiaofei.wxf)
 * @since 1.0.3
 */
public final class ApnsChannelFactory {
    private final SecuritySocketFactory securitySocketFactory;

    private ApnsChannelFactory(SecuritySocketFactory securitySocketFactory) {
        this.securitySocketFactory = securitySocketFactory;
    }

    public static final Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private KeyStoreWrapper keyStoreWrapper;
        private ApnsGateway apnsGateway;

        public Builder keyStoreWrapper(KeyStoreWrapper keyStoreWrapper) {
            this.keyStoreWrapper = keyStoreWrapper;
            return this;
        }

        public Builder apnsServer(ApnsGateway apnsGateway) {
            this.apnsGateway = apnsGateway;
            return this;
        }

        public ApnsChannelFactory build() {
            try {
                return new ApnsChannelFactory(SecuritySocketFactory.newBuilder().appleServer(this.apnsGateway).keyStoreWrapper(this.keyStoreWrapper).build());
            } catch (InvalidKeyStoreException e) {
                throw new ApnsException(e);
            }
        }
    }


    public ApnsChannel newChannel() {
        return new ApnsChannel(this.securitySocketFactory);
    }

    public ApnsChannel newChannel(int tryTimes) {
        return new ApnsChannel(this.securitySocketFactory, tryTimes);
    }
}
