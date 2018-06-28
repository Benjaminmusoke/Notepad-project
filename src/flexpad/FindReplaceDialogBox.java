package flexpad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Benjamin on 2017-05-31.
 */
class FindReplaceDialogBox
{
    //Frame frFReplace;
    Dialog frFReplace;
    Button btFRFindNext, btFRReplace, btFRReplaceAll, btFRClose;
    Label lbFRFind,lbFRReplace;
    TextField tfFRFind, tfFRReplace;
    JFrame frame = new JFrame();
    String infoMessage;
    String findStr, findInStr;
    int startIndex, endIndex;
    int clicked;
    TextArea txtArea;
    boolean flag = false;
    boolean changed = false;

    public FindReplaceDialogBox() {
        frFReplace = new Dialog(frame);
        frFReplace.setSize(330, 130);
        frFReplace.setLocation(500, 300);
        frFReplace.setTitle("Find and Replace");
        frFReplace.setBackground(new Color(236, 236, 236));
        frFReplace.setFont(new Font("Calibri", Font.BOLD, 12));
        frFReplace.setLayout(new GridBagLayout());
        GridBagConstraints gbcFR = new GridBagConstraints();
        gbcFR.gridx = 0;
        gbcFR.gridy = 0;
        gbcFR.gridwidth = 1;
        gbcFR.anchor = GridBagConstraints.WEST;
        Insets insetFRReplace = new Insets(4, 4, 0, 8);
        gbcFR.insets = insetFRReplace;
        lbFRFind = new Label("Find");
        frFReplace.add(lbFRFind, gbcFR);
        gbcFR.gridx = 1;
        gbcFR.gridy = 0;
        gbcFR.gridwidth = 3;
        gbcFR.anchor = GridBagConstraints.WEST;
        tfFRFind = new TextField(26);
        frFReplace.add(tfFRFind, gbcFR);
        tfFRFind.addTextListener(e -> clicked = 0);
        gbcFR.gridx = 0;
        gbcFR.gridy = 1;
        gbcFR.gridwidth = 1;
        gbcFR.anchor = GridBagConstraints.WEST;
        lbFRReplace = new Label("Replace With");
        frFReplace.add(lbFRReplace, gbcFR);
        gbcFR.gridx = 1;
        gbcFR.gridy = 1;
        gbcFR.gridwidth = 3;
        gbcFR.anchor = GridBagConstraints.WEST;
        tfFRReplace = new TextField(26);
        frFReplace.add(tfFRReplace, gbcFR);
        gbcFR.gridx = 0;
        gbcFR.gridy = 2;
        gbcFR.gridwidth = 1;
        gbcFR.anchor = GridBagConstraints.WEST;
        btFRFindNext = new Button("Find Next");
        frFReplace.add(btFRFindNext, gbcFR);
        btFRFindNext.addActionListener(e -> {
            if (tfFRFind.getText() != "") {
                findStr = tfFRFind.getText();
                System.out.println(findStr);
                startIndex = findInStr.indexOf(findStr, clicked);
                System.out.println(startIndex);
                if (startIndex != -1) {
                    endIndex = findStr.length();
                    System.out.println(endIndex);
                    txtArea.select(startIndex, startIndex + endIndex);
                    clicked = startIndex + 1;
                    flag = true;
                } else {
                    infoMessage = "Cannot find " + "\"" + findStr + "\"";
                    MessageBox msg = new MessageBox();
                }
            }
        });
        gbcFR.gridx = 1;
        gbcFR.gridy = 2;
        gbcFR.gridwidth = 1;
        gbcFR.anchor = GridBagConstraints.WEST;
        btFRReplace = new Button("Replace");
        frFReplace.add(btFRReplace, gbcFR);
        btFRReplace.addActionListener(e -> {
            if (tfFRFind.getText() != "" && (flag == false || startIndex == 0)) {
                findStr = tfFRFind.getText();
                System.out.println(findStr);
                startIndex = findInStr.indexOf(findStr, clicked);
                System.out.println(startIndex);
                if (startIndex != -1) {
                    endIndex = findStr.length();
                    System.out.println(endIndex);
                    txtArea.replaceRange(tfFRReplace.getText(), startIndex, startIndex + endIndex);
                    clicked = startIndex + 1;
                } else {
                    infoMessage = "Cannot find " + "\"" + findStr + "\"";
                    MessageBox msg = new MessageBox();
                }
            } else if (tfFRFind.getText() != "" && startIndex != 0 && flag == true) {
                if (startIndex != -1) {
                    endIndex = findStr.length();
                    System.out.println(endIndex);
                    txtArea.replaceRange(tfFRReplace.getText(), startIndex, startIndex + endIndex);
                    clicked = startIndex + 1;
                } else {
                    infoMessage = "Cannot find " + "\"" + findStr + "\"";
                    MessageBox msg = new MessageBox();
                }
                flag = false;
            }
        });
        gbcFR.gridx = 2;
        gbcFR.gridy = 2;
        gbcFR.gridwidth = 1;
        gbcFR.anchor = GridBagConstraints.WEST;
        btFRReplaceAll = new Button("Replace All");
        frFReplace.add(btFRReplaceAll, gbcFR);
        btFRReplaceAll.addActionListener(e -> {
            if (changed == true) {
                findInStr = txtArea.getText();
            }
            findInStr = findInStr.replaceAll(tfFRFind.getText(), tfFRReplace.getText());
            txtArea.setText(findInStr);
        });
        gbcFR.gridx = 3;
        gbcFR.gridy = 2;
        gbcFR.gridwidth = 1;
        gbcFR.anchor = GridBagConstraints.WEST;
        btFRClose = new Button("Close");frFReplace.add(btFRClose,gbcFR);
        btFRClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = 0;
                findInStr ="";
                findStr="";
                startIndex = 0;
                endIndex = 0;
                frFReplace.setVisible(true);
                frFReplace.dispose();
            }
        });
    }

    }