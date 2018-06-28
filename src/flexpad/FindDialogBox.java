package flexpad;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Benjamin on 2017-05-31.
 */

public class FindDialogBox
{
    //Frame frFind;
    JFrame frame = new JFrame();
    Dialog frFind;
    Button btFind,btClose;
    Label lbFind;
    TextField tfFind;
    Checkbox cbMatchCase;
    String infoMessage;
    String findStr, findInStr;
    int startIndex, endIndex;
    int clicked;
    TextArea txtArea;


    public FindDialogBox()
    {
        frFind = new Dialog(frame);
        frFind.setSize(300,100);
        frFind.setLocation(700,150);
        frFind.setTitle("Find");
        frFind.setBackground(new Color(236,236,236));
        frFind.setLayout(new GridBagLayout());
        GridBagConstraints gbcFind = new GridBagConstraints();
        lbFind = new Label("Find");
        tfFind = new TextField(20);
        tfFind.addTextListener(e -> clicked = 0);
        btFind = new Button("Find");
        btFind.addActionListener(e -> {
            if(tfFind.getText()!="")
            {
                findStr = tfFind.getText();
                System.out.println(findStr);
                startIndex=findInStr.indexOf(findStr,clicked);
                System.out.println(startIndex);
                if(startIndex!=-1)
                {
                    endIndex = findStr.length();
                    System.out.println(endIndex);
//frFind.toBack();
//frame.setFocusable(true);
                    frame.requestFocus();
                    txtArea.select(startIndex,startIndex+endIndex);
                    clicked = startIndex +1;
                }
                else
                {
                    infoMessage = "Cannot find " + "\""+findStr+"\"";
                    MessageBox msg = new MessageBox();
                }
            }
        });
        btClose = new Button("Close");
        btClose.addActionListener(e -> {
//tfFind.setText() = "";
            clicked = 0;
            findInStr ="";
            findStr="";
            startIndex = 0;
            endIndex = 0;
            frFind.setVisible(false);
            frFind.dispose();
        });
        cbMatchCase = new Checkbox("Match Case");
        gbcFind.gridx = 0;
        gbcFind.gridy = 0;
        gbcFind.ipadx = 2;
        gbcFind.ipady = 2;
        Insets insetFind = new Insets(4,4,0,8);
        gbcFind.insets = insetFind;
        gbcFind.anchor = GridBagConstraints.WEST;
        frFind.add(lbFind,gbcFind);
        gbcFind.gridx = 1;
        gbcFind.gridy = 0;
        gbcFind.gridwidth=3;
        frFind.add(tfFind,gbcFind);
        gbcFind.gridx = 0;
        gbcFind.gridy =1;
        gbcFind.gridwidth=1;
        frFind.add(cbMatchCase,gbcFind);
        gbcFind.gridx = 1;
        gbcFind.gridy = 1;
        frFind.add(btFind,gbcFind);
        gbcFind.gridx = 2;
        gbcFind.gridy = 1;
        frFind.add(btClose,gbcFind);
        frFind.setVisible(true);
    }
}

class MessageBox
{
    //Frame frMsg;
    JDialog frMsg;
    Label lbMsg;
    Button btMsgOk;
    String infoMessage;

    public MessageBox()
    {
        frMsg = new JDialog();
        frMsg.setSize(180,100);
        frMsg.setLocation(500,300);
        frMsg.setTitle("Text Editor");
        frMsg.setBackground(new Color(236,236,236));
        frMsg.setFont(new Font("Calibri",Font.BOLD,12));
        frMsg.setLayout(new GridBagLayout());
        GridBagConstraints gbcMsg = new GridBagConstraints();
        lbMsg = new Label(infoMessage);
        gbcMsg.gridx = 0;
        gbcMsg.gridy = 0;
        gbcMsg.ipadx = 2;
        gbcMsg.ipady = 2;
        frMsg.add(lbMsg,gbcMsg);
        btMsgOk = new Button("Ok");
        btMsgOk.addActionListener(e -> {
            frMsg.setVisible(false);
            frMsg.dispose();
        });
        gbcMsg.gridx = 0;
        gbcMsg.gridy = 1;
        gbcMsg.ipadx = 2;
        gbcMsg.ipady = 2;
        frMsg.add(btMsgOk,gbcMsg);
        frMsg.setVisible(true);
    }
}

