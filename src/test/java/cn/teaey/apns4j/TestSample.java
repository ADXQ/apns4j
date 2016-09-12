///*
// *
// *  * Copyright 2015 The Apns4j Project
// *  *
// *  * The Netty Project licenses this file to you under the Apache License,
// *  * version 2.0 (the "License"); you may not use this file except in compliance
// *  * with the License. You may obtain a copy of the License at:
// *  *
// *  *   http://www.apache.org/licenses/LICENSE-2.0
// *  *
// *  * Unless required by applicable law or agreed to in writing, software
// *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// *  * License for the specific language governing permissions and limitations
// *  * under the License.
// *
// */
//
//package cn.teaey.apns4j;
//
//import cn.teaey.apns4j.keystore.KeyStoreGetter;
//import cn.teaey.apns4j.network.async.ApnsFuture;
//import cn.teaey.apns4j.network.ApnsGateway;
//import cn.teaey.apns4j.protocol.ApnsPayload;
//import cn.teaey.feva.common.Interval;
//import junit.framework.Assert;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
///**
// * @author Teaey
// * @date 13-8-30
// */
//public class TestSample {
//
//    @Test
//    public void asyncService() throws InterruptedException {
//        KeyStoreGetter keyStoreGetter = Apns4j.buildKeyStoreWrapper(TestConts.keyStorePath, TestConts.keyStorePwd);
//        final ApnsService service = Apns4j.apnsService(4, keyStoreGetter, ApnsGateway.DEVELOPMENT);
//        List<Thread> tList = new ArrayList<Thread>();
//        for (int i = 0; i < 10; i++) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 0; i < 1; i++) {
//                        ApnsPayload apnsPayload = Apns4j.newApnsPayload()
//                                .alert("" + System.currentTimeMillis())
//                                .badge((int) System.currentTimeMillis() % 100);
//                        ApnsFuture f = service.sendAndFlush(TestConts.deviceToken, apnsPayload);
//                        try {
//                            Object v = f.get();
//                            if(v instanceof Exception) {
//                                System.out.println("Send faild:" + apnsPayload.getIdentifier());
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//            tList.add(t);
//        }
//        for (Thread each : tList) {
//            each.start();
//        }
//        for (Thread each : tList) {
//            each.join();
//        }
//        service.shutdown();
//    }
//
//    @Test
//    public void performanceTest() {
//        long takeMills = Interval.interval(new Runnable() {
//            public void run() {
//                try {
//                    ApnsPayload apnsPayload = Apns4j.newApnsPayload()
//                            //.alert("TEST1")
//                            //.badge(2)
//                            //.sound("default")
//                            .alertBody("TEST")
//                            .alertActionLocKey("TEST");
//
//                    apnsPayload.toJsonBytes();
//                    apnsPayload.toJsonString();
//                } catch (Exception e) {
//                }
//            }
//        }, 10000);
//        System.out.println("take " + takeMills + "ms");
//    }
//
//    /**
//     * build KeyStore
//     */
//    @Test
//    public void loadKeyStoreWithClassPath() {
//        KeyStoreGetter keyStore = Apns4j.buildKeyStoreWrapper(TestConts.keyStorePath, TestConts.keyStorePwd);
//        Assert.assertNotNull(keyStore);
//    }
//
//    @Test
//    public void loadKeyStoreWithSystemPath() {
//        KeyStoreGetter keyStore = Apns4j.buildKeyStoreWrapper(TestConts.keyStorePath, TestConts.keyStorePwd);
//        Assert.assertNotNull(keyStore);
//    }
//
//    @Test
//    public void loadKeyStoreByFile() {
//        File f = new File(TestConts.keyStorePath);
//        KeyStoreGetter keyStore = Apns4j.buildKeyStoreWrapper(f, TestConts.keyStorePwd);
//        Assert.assertNotNull(keyStore);
//    }
//
//    @Test
//    public void loadKeyStoreByInputStream() {
//        InputStream in = getClass().getClassLoader().getResourceAsStream(TestConts.keyStorePath);
//        KeyStoreGetter keyStore = Apns4j.buildKeyStoreWrapper(in, TestConts.keyStorePwd);
//        Assert.assertNotNull(keyStore);
//    }
//
//    @Test
//    public void loadKeyStoreByByteArray() throws IOException {
//        InputStream in = getClass().getClassLoader().getResourceAsStream(TestConts.keyStorePath);
//        byte[] bytes = new byte[in.available()];
//        in.read(bytes);
//        in.close();
//        KeyStoreGetter keyStore = Apns4j.buildKeyStoreWrapper(bytes, TestConts.keyStorePwd);
//        Assert.assertNotNull(keyStore);
//    }
//
//}
