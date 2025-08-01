package com.example.addabuzz.util;

import com.example.addabuzz.BuildConfig;

public class CloudinaryConfig {
    public static String getCloudName() {
        return BuildConfig.CLOUDINARY_CLOUD_NAME;
    }

    public static String getApiKey() {
        return BuildConfig.CLOUDINARY_API_KEY;
    }

    public static String getApiSecret() {
        return BuildConfig.CLOUDINARY_API_SECRET;
    }
}

