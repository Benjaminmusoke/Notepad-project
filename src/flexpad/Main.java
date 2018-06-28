package flexpad;

import javax.swing.*;

/**
 * Created by Benjamin on 2017-05-15.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new View();
            }
        });

    }
}
