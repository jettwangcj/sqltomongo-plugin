package cn.org.wangchangjiu.util;

import com.intellij.ui.TextFieldWithAutoCompletionListProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 我们的自动提示内容的"Provider"
 */
public class SQLToMongoTextProvider extends TextFieldWithAutoCompletionListProvider<String> {

    /**
     *  自动提示补全的所有可选项列表
     */
    public static Set<String> items = new HashSet<>() {{
        add("select");
        add("from");
        add("where");
        add("order");
        add("by");
        add("limit");
        add("having");
        add("count");
        add("sum");
    }};

    public SQLToMongoTextProvider() {
        super(items);
    }

    public static void addAllCueWord(Set<String> all){
       items.addAll(all);
    }

    public static void addCueWord(String sqlText){
        String[] split = sqlText.split(" ");
        Arrays.asList(split).stream().forEach(item -> {
            if(item.contains(",")){
                Arrays.asList(item.split(",")).stream().forEach(it -> items.add(it.trim()));
            } else {
                items.add(item.trim());
            }
        });
    }

    @Override
    protected @NotNull String getLookupString(@NotNull String item) {
        return item;
    }
}
