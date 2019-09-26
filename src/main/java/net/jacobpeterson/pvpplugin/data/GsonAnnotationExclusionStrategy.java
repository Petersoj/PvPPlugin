package net.jacobpeterson.pvpplugin.data;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import net.jacobpeterson.pvpplugin.util.ExcludeDeserialization;

public class GsonAnnotationExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(ExcludeDeserialization.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
