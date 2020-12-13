package org.aaiahmed.dbunloader.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;

public class AwsS3 {
    public static void uploadFile(
      final String clientRegion,
      final String bucketName,
      final String fileObjKeyName,
      final String fileName)
      throws SdkClientException {

    AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();

    PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName));
    s3Client.putObject(request);
  }
}
