package com.gc.gconline.handbook.util;



import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
public class AmazonS3ClientInstance {

	private static AmazonS3Client instance;
	private static String AccessKeyId = "a";
	private static String SecretAccessKey = "a";
	private static final String PROXY_HOST = "10.0.60.80";
	private static final String PROXY_PORT = "8080";
	private static final String PROXY_USERNAME = "a";
	private static final String PROXY_PASSWORD = "a";

	public AmazonS3ClientInstance() {
		ClientConfiguration clientConfig;
		BasicAWSCredentials basicCred;
		try {

			basicCred = new BasicAWSCredentials(AccessKeyId, SecretAccessKey);
			clientConfig = new ClientConfiguration();
			clientConfig.setMaxConnections(100);
			clientConfig.setRequestTimeout(1000000);
			clientConfig.setProtocol(Protocol.HTTP);
			clientConfig.withProxyHost(PROXY_HOST);
			clientConfig.withProxyPort(Integer.parseInt(PROXY_PORT));
			clientConfig.withProxyUsername(PROXY_USERNAME);
			clientConfig.withProxyPassword(PROXY_PASSWORD);
		} catch (Exception e) {
			throw new AmazonClientException("failed", e);
		}
		instance = new AmazonS3Client(basicCred, clientConfig);
		Region CNNORTH1 = Region.getRegion(Regions.CN_NORTH_1);
		instance.setRegion(CNNORTH1);
	}

	public static AmazonS3Client getInstance() {
		if (instance == null) {
			new AmazonS3ClientInstance();
		}
		return instance;
	}
}
