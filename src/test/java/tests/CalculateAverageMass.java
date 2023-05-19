package tests;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CalculateAverageMass {
    public static void main(String[] args) throws IOException {

        // Setting Access Key id and Secret Access Key of AWS user
        System.setProperty("aws.accessKeyId", "AKIA6NAG3YKAYYO72LM7");
        System.setProperty("aws.secretAccessKey", "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");

        // Setting region details to S3Client object
        S3Client s3Client = S3Client.builder()
                .region(Region.US_WEST_2)
                .build();

        // Setting S3 bucket name
        String bucketName="majorly-meteoric";
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        // Calling S3 bucket data into S3Client object and storing list of files
        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);
        List<S3Object> objectSummaries = listObjectsResponse.contents();

        // Getting first file with provided in a summary table with columns of Day and AverageMassInGrams
        String fileKey = objectSummaries.get(0).key();
        ResponseInputStream<GetObjectResponse> objectInputStream = s3Client.getObject(GetObjectRequest.builder().bucket(bucketName).key(fileKey).build());

        // Reading file and getting average mass
        BufferedReader reader = new BufferedReader(new InputStreamReader(objectInputStream));
        reader.readLine();

        String line;
        int numberOfMasses=0;
        int totalMass=0;
        while ((line = reader.readLine()) != null) {
            numberOfMasses++;
            totalMass+=Integer.parseInt(line.split(",")[1]);
        }
        System.out.println("Average mass: "+totalMass/numberOfMasses);

    }
}
