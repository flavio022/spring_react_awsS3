package com.amigoscode.aws_s3.bucket;

public enum BucketName {
    PROFILE_IMAGE("amigoscode-image-upload-flavio");
    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
