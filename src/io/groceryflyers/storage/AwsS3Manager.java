package io.groceryflyers.storage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.*;

/**
 * Created by jeremiep on 2016-01-31.
 */
public class AwsS3Manager {
    private static final String IAM_ACCESS_KEY = "AKIAJ6LIYPCCKMZNPN7Q";
    private static final String IAM_SECRET_KEY = "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";
    private static final String S3_BUCKET = "discount-shoppers";

    private static AwsS3Manager instance = null;

    public static AwsS3Manager getInstance() {
        if(AwsS3Manager.instance == null) {
            AwsS3Manager.instance = new AwsS3Manager();
        }

        return AwsS3Manager.instance;
    }

    private AmazonS3Client client;

    private AwsS3Manager() {
        this.initializeConnection();
    }

    private void initializeConnection() {
        this.client = new AmazonS3Client(new BasicAWSCredentials(
                IAM_ACCESS_KEY,
                IAM_SECRET_KEY
        ));
    }

    public String persist(String s3Key, File theFile) {
        try {
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            this.client.putObject(
                    new PutObjectRequest(S3_BUCKET, s3Key, theFile).withAccessControlList(acl)
            );

            return this.getPublicUrl(s3Key);
        } catch(AmazonServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPublicUrl(String s3Key) {
        try {
            return this.client.getUrl(S3_BUCKET, s3Key).toString();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void retrieve(String s3Key, File theFile) {
        try {
            S3Object s3Obj = this.client.getObject(
                    new GetObjectRequest(S3_BUCKET, s3Key)
            );

            InputStream fis = s3Obj.getObjectContent();
            OutputStream out = new FileOutputStream(theFile);

            int read = 0;
            byte[] buff = new byte[1024];
            while((read = fis.read(buff)) != -1) {
                out.write(buff, 0, read);
            }

            out.close();
            fis.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
