package jdev.lojavirtual.service;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class HostIgoringCliente implements Serializable{
	private static final long serialVersionUID = 1L;

	private String hostName;
	
	public HostIgoringCliente(String hostName) {
		this.hostName = hostName;
	}

	 
	public Client hostIgnoreClient () throws Exception {
		
		TrustManager[] trustManagers = new TrustManager[] { 
				
				new X509TrustManager() {
				
				@Override
				public X509Certificate[] getAcceptedIssuers() {
				return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					
				}
			}
		};
		
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustManagers, new SecureRandom());
		
		Set<String> hostNameList = new HashSet<String>();
		hostNameList.add(this.hostName);
		HttpsURLConnection.setDefaultHostnameVerifier(new IgnoreHostNameSSL(hostNameList));
		
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		
		DefaultClientConfig config = new DefaultClientConfig();
		Map<String, Object> properties = config.getProperties();
		HTTPSProperties httpsProperties = new HTTPSProperties(new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		}, sslContext);
		
		return null;
		
	}
}
