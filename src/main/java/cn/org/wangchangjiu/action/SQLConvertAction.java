package cn.org.wangchangjiu.action;

import cn.org.wangchangjiu.sqltomongo.core.common.MongoParserResult;
import cn.org.wangchangjiu.sqltomongo.core.parser.SelectSQLTypeParser;
import cn.org.wangchangjiu.ui.SQLConvertDialog;
import cn.org.wangchangjiu.util.SQLToMongoTextProvider;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

/**
 * @Classname SQLConvertAction
 * @Description
 * @Date 2022/12/7 18:31
 * @Created by wangchangjiu
 */
public class SQLConvertAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        String sqlText = editor.getSelectionModel().getSelectedText();

        if(StringUtils.isEmpty(sqlText)){
            new SQLConvertDialog(anActionEvent).show();
            return;
        }
        try {
            MongoParserResult mongoParserResult = SelectSQLTypeParser.defaultConverterResult(sqlText);

            String mongoText = mongoParserResult.toJsonString();
            // 获取需要复制的文本
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(mongoText);
            clipboard.setContents(selection, null);
            Notifications.Bus.notify(new Notification("Print", "SQL语法转Mongo语法",
                    "转Mongo语法成功，并复制，请直接粘贴文本即可", NotificationType.INFORMATION), anActionEvent.getProject());

            SQLToMongoTextProvider.addCueWord(sqlText);
        } catch (Exception e){
            Notifications.Bus.notify(new Notification("Print", "SQL语法转Mongo语法",
                    e.getMessage(), NotificationType.ERROR), anActionEvent.getProject());
        }
    }
}
