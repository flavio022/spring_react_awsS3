package com.amigoscode.aws_s3.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class UserProfileService {
    private final UserProfileDataAcessService userProfileDataAcessService;

    @Autowired
    public UserProfileService(UserProfileDataAcessService userProfileDataAcessService){
        this.userProfileDataAcessService = userProfileDataAcessService;
    }

    List<UserProfile> getUserProfiles(){
        return userProfileDataAcessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileID, MultipartFile file) {

    }
}
