package com.gc.gconline.handbook.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 *
 * @ClassName: AwsClientUtil.java
 * @Description:
 * @author qsnp241
 * @date: 2018年12月28日 下午6:56:40
 */
@SuppressWarnings({ "hiding" })
public class AwsClientUtil {
	private static Logger logger = LoggerFactory.getLogger(AwsClientUtil.class);
	private static AmazonS3 s3;
	private static String bucketName;

	static {
		s3 = AmazonS3ClientInstance.getInstance();
		Properties properties = new Properties();
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(
					AwsClientUtil.class
					.getResourceAsStream("/handbook.properties"), "UTF-8");
			properties.load(inputStreamReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		bucketName = properties.getProperty("handbbok.aws.bucketName");
	}

	private AwsClientUtil() {
	}

	/**
	 * 
	 * @param bucketName
	 * @desc 创建名称为bucketName的Bucket
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:07:52
	 */
	public static void createBucket(String bucketName) {
		if (s3.doesBucketExist(bucketName)) {
			logger.info(bucketName + " already exist!");
			return;
		}
		logger.info("creating " + bucketName + " ...");
		s3.createBucket(bucketName);
		logger.info(bucketName + " has been created!");
	}

	/**
	 * 
	 * 
	 * @desc 删除默认bucket,有文件则操作失败
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:55:47
	 */
	public static void deleteBucket() {
		deleteBucket(bucketName);
	}

	/**
	 * 
	 * @param bucketName
	 * @desc 删除bucket,有文件则操作失败
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:37:39
	 */
	public static void deleteBucket(String bucketName) {
		if (!s3.doesBucketExist(bucketName)) {
			logger.info(bucketName + " does not exists!");
			return;
		}
		ObjectListing objectListing = s3.listObjects(bucketName);
		List<S3ObjectSummary> s3Objects = objectListing.getObjectSummaries();
		if (s3Objects != null && !s3Objects.isEmpty()) {
			logger.info(bucketName
					+ " has ObjectSummary , it is forbidden to be deleted");
			return;
		}
		logger.info("deleting " + bucketName + " ...");
		s3.deleteBucket(bucketName);
		logger.info(bucketName + " has been deleted!");
	}

	/**
	 * 
	 * 
	 * @desc 删除默认桶,有文件则删除文件
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:55:20
	 */
	public static void deleteBucketAndFile() {
		deleteBucketAndFile(bucketName);
	}

	/**
	 * 
	 * @param bucketName
	 * @desc 删除指定桶,有文件则删除文件
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:26:54
	 */
	public static void deleteBucketAndFile(String bucketName) {
		if (!s3.doesBucketExist(bucketName)) {
			logger.info(bucketName + " does not exists!");
			return;
		}
		logger.info("deleting " + bucketName + " ...");
		ObjectListing objectListing = s3.listObjects(bucketName);
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			String key = objectSummary.getKey();
			s3.deleteObject(bucketName, key);
		}
		s3.deleteBucket(bucketName);
		logger.info(bucketName + " has been deleted!");
	}

	/**
	 * 
	 * @param bucketName
	 * @return boolean
	 * @desc 验证s3上是否存在名称为bucketName的Bucket
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:12:28
	 */
	public static boolean checkBucketExists(String bucketName) {
		List<Bucket> buckets = s3.listBuckets();
		for (Bucket bucket : buckets) {
			if (Objects.equals(bucket.getName(), bucketName))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param uploadFile
	 * @param remoteFileName
	 * @return String
	 * @throws IOException
	 * @desc 将文件上传至S3上并且返回url
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:13:29
	 */
	public static String uploadToS3(File uploadFile, String remoteFileName) {
		return uploadToS3(uploadFile, remoteFileName, bucketName);
	}

	/**
	 * 
	 * @param uploadFile
	 * @param remoteFileName
	 * @param bucketName
	 * @return String
	 * @throws IOException
	 * @desc 将文件上传至S3上并且返回url
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:13:14
	 */
	public static String uploadToS3(File uploadFile, String remoteFileName,
			String bucketName) {
		try {
			long startTime = System.currentTimeMillis();
			logger.info(remoteFileName + "开始上传");
			createBucket(bucketName);
			// 上传文件
			s3.putObject(new PutObjectRequest(bucketName, remoteFileName,
					uploadFile)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			// 获取一个request
			GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
					bucketName, remoteFileName);
			// 生成公用的url
			String url = s3.generatePresignedUrl(urlRequest).toString();
			long endTime = System.currentTimeMillis();
			logger.info(remoteFileName + "上传结束,耗时" + (endTime - startTime)
					/ 1000.0 + "秒");
			logger.info("=====URL=====" + url + "=====URL=====");
			return url.substring(0, url.indexOf('?'));
		} catch (AmazonServiceException ase) {
			logger.error(ase.getMessage());
		} catch (AmazonClientException ace) {
			logger.error(ace.getMessage());
		}
		return null;
	}

	/**
	 * @Title: getUrlFromS3
	 * @Description: 获取文件的url
	 * @param @param remoteFileName 文件名
	 * @param @return
	 * @param @throws IOException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	/**
	 * 
	 * @param remoteFileName
	 * @return
	 * @throws IOException
	 * @desc 获取指定key的文件的url
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:14:01
	 */
	public static String getUrlFromS3(String remoteFileName) {
		return getUrlFromS3(bucketName, remoteFileName);
	}

	public static String getUrlFromS3(String bucketName, String remoteFileName) {
		try {

			GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(
					bucketName, remoteFileName);
			// 临时链接,具有时间限制
			String accUrl = s3.generatePresignedUrl(httpRequest).toString();
			// 对象寻址 bucket.s3域名/文件名 | s3域名/bucket/文件
			return accUrl.substring(0, accUrl.indexOf('?'));
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @return List<String>
	 * @desc 返回默认bucketName下的文件key
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:14:42
	 */
	public static List<String> listObjects() {
		return listObjects(bucketName);
	}

	/**
	 * 
	 * @param bucketName
	 * @return List<String>
	 * @desc 返回指定bucketName下的文件key
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:15:39
	 */
	public static List<String> listObjects(String bucketName) {
		if (!checkBucketExists(bucketName)) {
			logger.info(bucketName + " not exist!");
			return null;
		}
		List<String> remoteFileNames = new ArrayList<>();
		logger.info("Listing objects of " + bucketName);
		ObjectListing objectListing = s3.listObjects(bucketName);
		int objectNum = 0;
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			remoteFileNames.add(objectSummary.getKey());
			logger.info("remoteFileName-" + objectNum + " : "
					+ objectSummary.getKey());
			objectNum++;
		}
		logger.info("total " + objectNum + " object(s).");
		return remoteFileNames;
	}

	/**
	 * 
	 * @param key
	 * @return boolean
	 * @desc 判断指定key文件是否在默认桶中存在
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:16:02
	 */
	public static boolean isObjectExit(String key) {
		return isObjectExit(bucketName, key);
	}

	/**
	 * 
	 * @param bucketName
	 * @param key
	 * @return
	 * @desc 判断指定key文件是否在bucketName桶中存在
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:16:51
	 */
	public static boolean isObjectExit(String bucketName, String key) {
		if (!checkBucketExists(bucketName)) {
			logger.info(bucketName + " not exist!");
			return false;
		}
		ObjectListing objectListing = s3.listObjects(bucketName);
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			if (objectSummary.getKey().equals(key))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param remoteFileName
	 * @param path
	 *            下载的路径
	 * @throws IOException
	 * @desc 将默认bucket下的文件下载到本地路径
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:17:09
	 */
	public static void downFromS3(String remoteFileName, String path) {
		downFromS3(bucketName, remoteFileName, path);
	}

	/**
	 * 
	 * @param bucketName
	 * @param remoteFileName
	 * @param path
	 * @throws IOException
	 * @desc 将指定bucketName下的文件下载到本地路径
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:17:40
	 */
	public static void downFromS3(String bucketName, String remoteFileName,
			String path) {
		try {
			GetObjectRequest request = new GetObjectRequest(bucketName,
					remoteFileName);
			s3.getObject(request, new File(path));
			logger.info("aws文件" + remoteFileName + "保存至" + path);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * 
	 * @param key
	 * @desc 删除指定文件
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:18:32
	 */
	public static void deleteObject(String key) {
		deleteObject(bucketName, key);
	}

	/**
	 * 
	 * @param bucketName
	 * @param key
	 * @desc 删除指定文件
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:19:13
	 */
	public static void deleteObject(String bucketName, String key) {
		if (!checkBucketExists(bucketName)) {
			logger.info(bucketName + " does not exists!");
			return;
		}
		logger.info("deleting " + key + " in " + bucketName + " ...");
		s3.deleteObject(bucketName, key);
		logger.info(key + " in " + bucketName + " was deleted!");
	}

	/**
	 * 
	 * @param prefix
	 * @desc 删除指定前缀的文件
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:19:31
	 */
	public static void deleteObjectsWithPrefix(String prefix) {
		deleteObjectsWithPrefix(bucketName, prefix);
	}

	/**
	 * 
	 * @param bucketName
	 * @param prefix
	 * @desc 删除指定前缀的文件
	 * @user qsnp241
	 * @date 2018年9月29日 上午10:19:53
	 */
	public static void deleteObjectsWithPrefix(String bucketName, String prefix) {
		if (!s3.doesBucketExist(bucketName)) {
			logger.info(bucketName + " does not exists!");
			return;
		}
		logger.info("deleting " + prefix + "* in " + bucketName + " ...");
		ObjectListing objectListing = s3.listObjects(bucketName);
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			if (objectSummary.getKey().startsWith(prefix)) {
				s3.deleteObject(bucketName, objectSummary.getKey());
			}

		}
		logger.info("All " + prefix + "* was deleted!");
	}


}
