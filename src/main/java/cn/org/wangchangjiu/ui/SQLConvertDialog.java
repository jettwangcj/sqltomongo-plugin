package cn.org.wangchangjiu.ui;

import cn.org.wangchangjiu.extension.TextProviderCache;
import cn.org.wangchangjiu.sqltomongo.core.common.MongoParserResult;
import cn.org.wangchangjiu.sqltomongo.core.parser.SelectSQLTypeParser;
import cn.org.wangchangjiu.util.SQLToMongoTextProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.GotItTooltip;
import com.intellij.util.textCompletion.TextFieldWithCompletion;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SQLConvertDialog extends DialogWrapper {

    private AnActionEvent anActionEvent;

    private JPanel contentPane;
    private JLabel inputLabel;
    private com.intellij.util.textCompletion.TextFieldWithCompletion inputSqlText;
    private JLabel outLabel;
    private JTextArea outMongoText;
    private JButton copyBut;
    private JScrollPane outScrollPane;
    private JScrollPane inputSqlScrollPane;
    private JButton buttonOK;

    public SQLConvertDialog(AnActionEvent anActionEvent) {
        super(true);
        init();

        this.anActionEvent = anActionEvent;

        copyBut.setText("copy");
        copyBut.setEnabled(false);

        copyBut.addActionListener(actionEvent -> {
            String mongoText = outMongoText.getText();
            // 获取需要复制的文本
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(mongoText);
            clipboard.setContents(selection, null);
            Notifications.Bus.notify(new Notification("Print", "SQL语法转Mongo语法",
                    "复制成功", NotificationType.INFORMATION), anActionEvent.getProject());
        });

        // 加载自动提示词
        TextProviderCache instance = TextProviderCache.getInstance(ProjectManager.getInstance().getDefaultProject());
        if(instance != null && instance.items != null){
            SQLToMongoTextProvider.addAllCueWord(instance.items);
        }

    }

    @Override
    protected void doOKAction() {
        String inputSql = inputSqlText.getText();
        if(StringUtils.isEmpty(inputSql)){
            new GotItTooltip("got.it.id", "", ProjectManager.getInstance().getDefaultProject()).
                            withShowCount(Integer.MAX_VALUE).
                            withHeader("没有编辑内容，请输入想要翻译的SQL语句").
                            show(inputLabel, GotItTooltip.RIGHT_MIDDLE);
            return;
        }

        String resultMongo;
        try {
            MongoParserResult mongoParserResult = SelectSQLTypeParser.defaultConverterResult(inputSql);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonElement = JsonParser.parseString(mongoParserResult.toJson());
            resultMongo = String.format("db.%s.aggregate(%s)", mongoParserResult.getCollectionName(),gson.toJson(jsonElement));

        } catch (Exception e){
            resultMongo = e.getMessage();
        }
        outMongoText.setText(resultMongo);
        copyBut.setEnabled(true);

        SQLToMongoTextProvider.addCueWord(inputSql);
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        TextProviderCache.getInstance(ProjectManager.getInstance().getDefaultProject()).items.addAll(SQLToMongoTextProvider.items);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        setTitle("SQLToMongo转换器");
        setSize(600, 500);
        setModal(false);
        setResizable(true);
        setOKButtonText("转换Mongo语法");
        setCancelButtonText("退出");

        return contentPane;
    }

    private void createUIComponents() {
        inputSqlText = new TextFieldWithCompletion(ProjectManager.getInstance().getDefaultProject(),
                new SQLToMongoTextProvider(), "", true, true, true, true);
        inputSqlText.setOneLineMode(false);
        inputSqlText.setAutoscrolls(true);
        inputSqlText.setEnabled(true);
    }


}
