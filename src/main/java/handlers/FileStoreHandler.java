package handlers;

import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class FileStoreHandler {
    protected static MinioClient minioClient;
    protected static String user = "testMusicPlayer";
    protected static String pass = "123456778";
    protected static String url = "http://localhost:9000";


    private static void getClient() {
        minioClient =  MinioClient.builder()
                .endpoint(url)
                .credentials(user, pass).build();
    }

    public static String uploadTrack (String bucket, String path) {
        getClient();
        String uniqueFileName = UUID.randomUUID().toString();
        try {
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucket)
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

    private static void closeClient () {
        try {
            minioClient.close();
        } catch (Exception e) {
            System.err.println("Could not close the minio client:"
                    + e.getMessage());
        }
    }
}
