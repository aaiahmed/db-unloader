package org.aaiahmed.dbunloader.storage;

import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.file.datalake.DataLakeDirectoryClient;
import com.azure.storage.file.datalake.DataLakeFileClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;

/*
* Following:
* https://docs.microsoft.com/en-us/azure/storage/blobs/data-lake-storage-directory-file-acl-java
* https://docs.microsoft.com/en-us/java/api/overview/azure/storage-file-datalake-readme?view=azure-java-stable
* */

public class Adls {
  private static final String endPointUrl = "https://%1s.dfs.core.windows.net";

  private final DataLakeServiceClient serviceClient;
  private final DataLakeFileSystemClient fileSystemClient;
  private final DataLakeDirectoryClient directoryClient;

  public Adls(
      final String accountName,
      final String accountKey,
      final String fileSystemName,
      final String adlsRootDir) {
    serviceClient = getDataLakeServiceClient(accountName, accountKey);
    fileSystemClient = getFileSystemClient(serviceClient, fileSystemName);
    directoryClient = getDirectoryClient(adlsRootDir);
  }

  public void uploadFile(final String adlsSubDir, final String fileToUpload) {

    final DataLakeDirectoryClient subDirClient = directoryClient.createSubdirectory(adlsSubDir);
    final DataLakeFileClient fileClient = subDirClient.getFileClient(fileToUpload);
    fileClient.uploadFromFile(fileToUpload);
  }

  public DataLakeServiceClient getDataLakeServiceClient(
      final String accountName, final String accountKey) {

    return new DataLakeServiceClientBuilder()
    .credential(new StorageSharedKeyCredential(accountName, accountKey))
    .endpoint(String.format(endPointUrl, accountName))
    .buildClient();
  }

  public DataLakeFileSystemClient getFileSystemClient(
      final DataLakeServiceClient serviceClient, final String fileSystemName) {
    return serviceClient.getFileSystemClient(fileSystemName);
  }

  public DataLakeDirectoryClient getDirectoryClient(final String path) {
    return fileSystemClient.createDirectory(path);
  }
}
