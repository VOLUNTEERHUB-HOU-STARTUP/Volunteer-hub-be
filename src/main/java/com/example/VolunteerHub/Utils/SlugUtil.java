package com.example.VolunteerHub.Utils;

import java.text.Normalizer;

public class SlugUtil {
    public static String toSlug(String textInput) {
        if (textInput == null) return null;

        // bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(textInput, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // thay khoảng trắng và ký tự đặc biệt bằng dấu  -
        slug = slug.toLowerCase().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-");

        return slug;
    }
}
