package cn.org.wangchangjiu.extension;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@State(name = "textProvider.cache", storages = {@Storage(value = "textProvider-cache.xml")})
public class TextProviderCache implements PersistentStateComponent<TextProviderCache> {

    public Set<String> items = new HashSet<>() ;

    public static TextProviderCache getInstance(Project project) {
        return project.getService(TextProviderCache.class);
    }

    @Override
    public @Nullable TextProviderCache getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TextProviderCache state) {
        if (state.items == null) {
            return;
        }
        this.items = state.items;
    }

}
