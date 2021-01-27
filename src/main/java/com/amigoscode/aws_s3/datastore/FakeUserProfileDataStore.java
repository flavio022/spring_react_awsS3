package com.amigoscode.aws_s3.datastore;

import com.amigoscode.aws_s3.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("437acc1c-d97d-4e0d-bc4d-37e3f46a027e"), "Flavio", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("d98e4e7a-448c-4021-b8e5-9599234c0f49"), "Donga", null));
    }
    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }
}
