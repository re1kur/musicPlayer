package handlers;

import io.minio.*;
import io.minio.errors.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class FileStorageHandler {
    protected static MinioClient minioClient;
    protected static String user = "testMusicPlayer";
    protected static String pass = "123456778";
    protected static String url = "http://localhost:9000";


    private static void getClient() {
        minioClient =  MinioClient.builder()
                .endpoint(url)
                .credentials(user, pass).build();
        isExistsBucket();
    }

    public static String uploadTrack (String path) {
        getClient();
        String uniqueFileName = UUID.randomUUID().toString();
        try {
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket("uploaded.tracks")
                    .object(uniqueFileName)
                    .filename(path)
                    .build());
            return uniqueFileName;
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException |
                 IOException | NoSuchAlgorithmException | ServerException | XmlParserException | InvalidKeyException e) {
            System.err.println("Could not upload the track: \n"
            + e.getMessage());
        } finally {
            closeClient();
        }
        return null;
    }
    private static void isExistsBucket() {
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("uploaded.tracks").build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("uploaded.tracks").build());
            } else {
                System.out.println("Bucket 'uploaded.tracks' exists.");
            }
        } catch (Exception e) {
            System.err.println("Could not check if bucket 'uploaded.tracks' exists."
            + e.getMessage());
        }
    }

    public static File downloadTrack(String uuid) {
        getClient();
        File track = null;
        try (GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                .bucket("uploaded.tracks")
                .object(uuid)
                .build())) {
            track = File.createTempFile("track_", ".mp3");
            try (FileOutputStream fos = new FileOutputStream(track);
                 InputStream is = object) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException |
                 IOException | NoSuchAlgorithmException | ServerException | XmlParserException | InvalidKeyException e) {
            System.err.println("Could not download the track: \n" + e.getMessage());
        } finally {
            closeClient();
        }
        return track;
    }

    public static void deleteTrack(String uuid) {
        getClient();
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("uploaded.tracks")
                    .object(uuid)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException |
                 IOException | NoSuchAlgorithmException | ServerException | XmlParserException | InvalidKeyException e) {
            System.err.println("Could not download the track: \n" + e.getMessage());
        } finally {
            closeClient();
        }
    }

    private static void closeClient () {
        try {
            minioClient.close();
        } catch (Exception e) {
            System.err.println("Could not close the minio client:"
                    + e.getMessage());
        }
    }
}
