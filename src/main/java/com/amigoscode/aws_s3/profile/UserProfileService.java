package com.amigoscode.aws_s3.profile;

import com.amigoscode.aws_s3.bucket.BucketName;
import com.amigoscode.aws_s3.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {
    private final UserProfileDataAcessService userProfileDataAcessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAcessService userProfileDataAcessService,
                              FileStore fileStore) {
        this.userProfileDataAcessService = userProfileDataAcessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles() {
        return userProfileDataAcessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileID, MultipartFile file) {
        // 1. Check if image is not empty
        isFileEmpty(file);
        // 2. If file is an image
        isImage(file);
        // 3. The user exists in our database
        UserProfile user = getUserProfileOrThrow(userProfileID);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = extractMetadata(file);

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_SVG.getMimeType()
        ).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileID) {

        return userProfileDataAcessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileID))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileID)));
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    public byte[] dowloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUserProfileOrThrow(userProfileId);
        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getUserProfileId());

        return user.getUserProfileImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }
}