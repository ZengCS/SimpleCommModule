package cn.sxw.android.base.okhttp;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLContextFactory {

    private SSLContextFactory(){}


    /**
     * 读取https证书，并获取SSLContext对象
     * @return SSLContext对象
     */
    public static SSLSocketFactory getSSLSocketFactory(InputStream cerStream){
        try {
            //设置证书类型
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            //打开放在main文件下的 assets 下的Http证书
            Certificate certificate = factory.generateCertificate(cerStream);
            //证书类型
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //授信证书 , 授信证书密码（应该是服务端证书密码）
            keyStore.load(null);
            keyStore.setCertificateEntry("sxt",certificate);

            // 创建信任管理器工厂并初始化秘钥
            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null,trustManagers,new SecureRandom());

            return sslContext.getSocketFactory();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
