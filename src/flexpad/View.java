package flexpad;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Benjamin on 2017-05-15.
 */
public class View extends JFrame {
    private JMenuBar menuBar;
    private JMenu[] menus;
    private JMenuItem items;
    private JPanel panel;
    public JTextArea area = new JTextArea();
    private ImageIcon icon;
    private JSeparator separator;
    private JSeparator jSeparator;
    private JButton setupIconsBar;
    private JButton button;
    private JToolBar toolBar;
    private EmptyBorder emptyBorder;
    private JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    private String currentFile = "empty";
    private boolean changed = false;
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    protected UndoManager undo = new UndoManager();
    String findStr, findInStr;
    int clicked;


    public View() {
        super();
        fileChooser.addChoosableFileFilter(new userFileFilter());
        setupUI();
    }

    private void setupUI() {
        setupFrame();
        setupPanel();
        setupMenuBarList();
        this.getContentPane().add(panel);
    }

    private void setupFrame() {
        setSize(1100,500);
        setTitle("Flexpad");
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/AllTheIcons/balloon@2x.png"));
        ImageIcon icon = new ImageIcon( image);
        setIconImage(icon.getImage());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int action1 = JOptionPane.showConfirmDialog(getParent(), "Are you sure you want to Exit " +
                        " Flexpad? ", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
                if (action1==JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
                super.windowClosing(e);
            }
        });
      //  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setupMenuBarList();
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        setJMenuBar((JMenuBar) menuBar.add(setupMenuBarList()));
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e4){

        }
        setVisible(true);

    }

    private void setupPanel() {
        panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        panel.add(toolBar, BorderLayout.PAGE_START);

    }

    public  JComponent setupFileComponents() {
        menus = new JMenu[5];

        menus[0] = new JMenu("File");
        menus[0].setMnemonic(KeyEvent.VK_F);
        menus[0].getAccessibleContext().setAccessibleDescription("Handling files");

        items = new JMenuItem("New");
        items.getAccessibleContext().setAccessibleDescription("newFile");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            area.setText("");
        });
        menus[0].add(items);

        items = new JMenuItem("Open...");
        items.getAccessibleContext().setAccessibleDescription("open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                readFile(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        menus[0].add(items);

        items = new JMenuItem("Save");
        items.getAccessibleContext().setAccessibleDescription("save");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                saveFile(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        menus[0].add(items);

        items = new JMenuItem("Save As...");
        items.getAccessibleContext().setAccessibleDescription("saveAs");
        items.addActionListener(e -> {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                saveFile(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        menus[0].add(items);

        separator = new JSeparator();
        menus[0].add(separator);

        items = new JMenuItem("Page Setup...");
        items.getAccessibleContext().setAccessibleDescription("pageSetup");
        items.addActionListener(e -> {
            PrinterJob pj = PrinterJob.getPrinterJob();
            PageFormat pf = pj.pageDialog(pj.defaultPage());

        });
        menus[0].add(items);

        items = new JMenuItem("Print...");
        items.getAccessibleContext().setAccessibleDescription("print");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            try {
                area.print();
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }

        });
        menus[0].add(items);

        separator = new JSeparator();
        menus[0].add(separator);

        items = new JMenuItem("Exit");
        items.getAccessibleContext().setAccessibleDescription("exit");
        items.addActionListener(e -> {int action = JOptionPane.showConfirmDialog(this,"Are you sure you want " +
                " to Exit Flexpad?", "Confirm Dialog", JOptionPane.OK_CANCEL_OPTION);
            if (action == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        });
        menus[0].add(items);


        return menus[0];


    }


    private void saveFile(String fileName) {
        try {
            FileWriter w = new FileWriter(fileName);
            area.write(w);
            w.close();
        }
        catch (IOException err) {
                err.printStackTrace();
        }

    }

    private void readFile(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            area.read(reader, null);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  JComponent setupEditComponents() {
        menus = new JMenu[5];

        menus[1] = new JMenu("Edit");
        menus[1].setMnemonic(KeyEvent.VK_E);
        menus[1].getAccessibleContext().setAccessibleDescription("Editing files");

        items = new JMenuItem("Undo");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            undoAction();
        });
        menus[1].add(items);

        separator = new JSeparator();
        menus[1].add(separator);

        ActionMap map = area.getActionMap();

        items = new JMenuItem("Cut");
        items.getAccessibleContext().setAccessibleDescription("Cut");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            items.setAction(map.get(DefaultEditorKit.cutAction));
        });
        menus[1].add(items);

        items = new JMenuItem("Copy");
        items.getAccessibleContext().setAccessibleDescription("Copy");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            items.setAction(map.get(DefaultEditorKit.copyAction));
        });
        menus[1].add(items);

        items = new JMenuItem("Paste");
        items.getAccessibleContext().setAccessibleDescription("Paste");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            items.setAction(map.get(DefaultEditorKit.pasteAction));

        });
        menus[1].add(items);

        items = new JMenuItem("Delete");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            items.setText("");

        });
        menus[1].add(items);

        separator = new JSeparator();
        menus[1].add(separator);

        items = new JMenuItem("Find...");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            findInStr = area.getText();
            clicked = 0;
            FindDialogBox fdb = new FindDialogBox();
        });
        menus[1].add(items);

        items = new JMenuItem("Find Next");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            findInStr = area.getText();
            clicked = 0;
            FindDialogBox fdb = new FindDialogBox();
        });
        menus[1].add(items);

        items = new JMenuItem("Replace...");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
            changed = false;
            findInStr = area.getText();
            clicked = 0;
            FindReplaceDialogBox frdb = new FindReplaceDialogBox();


        });
        menus[1].add(items);

        items = new JMenuItem("Go To...");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        menus[1].add(items);

        separator = new JSeparator();
        menus[1].add(separator);

        items = new JMenuItem("Select All");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menus[1].add(items);

        items = new JMenuItem("Time/Date");
        items.getAccessibleContext().setAccessibleDescription("Open");
        items.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, ActionEvent.CTRL_MASK));
        items.addActionListener(e -> {
           setupDateAndTime();
        });
        menus[1].add(items);

        return menus[1];


    }

    private void setupDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        Date date = new Date();
     //   System.out.println(dateFormat.format(date));
        area.setText(dateFormat.format(date));
    }


    private void undoAction() {
    //    final UndoManager undoManager = new UndoManager();
        area.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        area.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), "Undo");
        area.getActionMap().put("Undo", new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                        undo.undo();

                } catch (CannotUndoException e1){
                    System.out.println("Unable to undo: " + e1);
                    e1.printStackTrace();

                }
           //     updateUndoState();
          //      redoAction.updateRedoState();

            }
        });


    }

    public  JComponent setupFormatComponents() {
        menus = new JMenu[5];

        menus[2] = new JMenu("Format");
        menus[2].setMnemonic(KeyEvent.VK_F);
        menus[2].getAccessibleContext().setAccessibleDescription("Formatting files");

        items = new JMenuItem("Word Wrap");
        items.getAccessibleContext().setAccessibleDescription("Word Wrap");
        menus[2].add(items);

        items = new JMenuItem("Font...");
        items.getAccessibleContext().setAccessibleDescription("Font");
        items.addActionListener(e -> {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String []fontFamilies = ge.getAvailableFontFamilyNames();
            JOptionPane.showMessageDialog(null, fontFamilies);
        });
        menus[2].add(items);

        return menus[2];


    }

    public  JComponent setupViewComponents() {
        menus = new JMenu[5];

        menus[3] = new JMenu("View");
        menus[3].setMnemonic(KeyEvent.VK_V);
        menus[3].getAccessibleContext().setAccessibleDescription("View files");

        items = new JMenuItem("Status Bar");
        items.getAccessibleContext().setAccessibleDescription("Open");
        menus[3].add(items);

        return menus[3];


    }

    public  JComponent setupHelpComponents() {
        menus = new JMenu[5];

        menus[4] = new JMenu("Help");
        menus[4].setMnemonic(KeyEvent.VK_H);
        menus[4].getAccessibleContext().setAccessibleDescription("Help files");

        items = new JMenuItem("View Help");
        items.getAccessibleContext().setAccessibleDescription("Open");
        menus[4].add(items);

        separator = new JSeparator();
        menus[4].add(separator);

        items = new JMenuItem("About Flexpad");
        items.getAccessibleContext().setAccessibleDescription("Open");
        menus[4].add(items);

        return menus[4];


    }

    public JToolBar setupIcons() {
        toolBar = new JToolBar();
        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/newFolder.png")));
        button.setToolTipText("New");
        button.addActionListener(e -> {
            area.setText("");
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/openProject.png")));
        button.setToolTipText("Open...");
        button.addActionListener(e -> {
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                readFile(fileChooser.getSelectedFile().getAbsolutePath());
            }

        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());


        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/menu-saveall.png")));
        button.setToolTipText("Save");
        button.addActionListener(e -> {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                saveFile(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/menu-saveall.png")));
        button.setToolTipText("Save All");
        button.addActionListener(e -> {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                saveFile(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/openProject.png")));
        button.setToolTipText("Close");
        button.addActionListener(e -> {
            area.setText("");
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/openProject.png")));
        button.setToolTipText("CloseAll");
        button.addActionListener(e -> {
            area.setText("");
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/print.png")));
        button.setToolTipText("Print");
        button.addActionListener(e -> {
            try {
                area.print();
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }

        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        jSeparator = new JSeparator();
        jSeparator.setOrientation(JSeparator.VERTICAL);
        jSeparator.setMaximumSize(new Dimension(2,toolBar.getPreferredSize().height));
        toolBar.add(jSeparator);

        ActionMap map = area.getActionMap();

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/menu-cut.png")));
        button.setToolTipText("Cut");
        button.addActionListener(e -> {
            button.setAction(map.get(DefaultEditorKit.cutAction));
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/copy.png")));
        button.setToolTipText("Copy");
        button.addActionListener(e -> {
            button.setAction(map.get(DefaultEditorKit.copyAction));
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/menu-paste.png")));
        button.setToolTipText("Paste");
        button.addActionListener(e -> {
            button.setAction(map.get(DefaultEditorKit.pasteAction));
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        jSeparator = new JSeparator();
        jSeparator.setOrientation(JSeparator.VERTICAL);
        jSeparator.setMaximumSize(new Dimension(2,toolBar.getPreferredSize().height));
        toolBar.add(jSeparator);

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/undo.png")));
        button.setToolTipText("Undo");
        button.addActionListener(e -> {
            undoAction();
        });
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/redo.png")));
        button.setToolTipText("Redo");
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        jSeparator = new JSeparator();
        jSeparator.setOrientation(JSeparator.VERTICAL);
        jSeparator.setMaximumSize(new Dimension(2,toolBar.getPreferredSize().height));
        toolBar.add(jSeparator);

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/find.png")));
        button.setToolTipText("Find...");
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/menu-replace.png")));
        button.setToolTipText("Replace...");
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        jSeparator = new JSeparator();
        jSeparator.setOrientation(JSeparator.VERTICAL);
        jSeparator.setMaximumSize(new Dimension(2,toolBar.getPreferredSize().height));
        toolBar.add(jSeparator);

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/zoomIn.png")));
        button.setToolTipText("Zoom In (Ctrl+Mouse Wheel Up)");
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        button = new JButton(new ImageIcon(getClass().getResource("/AllTheIcons/zoomOut.png")));
        button.setToolTipText("Zoom Out (Ctrl+Mouse Wheel Down");
        toolBar.add(button);
        emptyBorder = new EmptyBorder(4,4,4,4);
        button.setBorder(emptyBorder);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(getBackground());

        return toolBar;


    }



    public JMenuBar setupMenuBarList(){
        menuBar = new JMenuBar();

        JMenuBar[] menuList = new JMenuBar[3];
        menuList[0] =  new JMenuBar();
        menuList[0].add(setupFileComponents());
        menuList[0].add(setupEditComponents());
        menuList[0].add(setupFormatComponents());
        menuList[0].add(setupViewComponents());
        menuList[0].add(setupHelpComponents());

        menuList[1] = new JMenuBar();
        menuList[1].add(setupIcons());

        menuBar.setLayout(new BorderLayout());
        menuBar.add(menuList[0], BorderLayout.NORTH);
        menuBar.add(menuList[1]);

        return menuBar;

    }

    protected class MyUndoableEditListener
            implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            undo.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }

    class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }


    }

    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

}
