package flexpad;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by Benjamin on 2017-05-23.
 */
public class userFileFilter extends FileFilter {
    @Override
    public boolean accept(File file) {

        if (file.isDirectory()){
            return true;
        }
        String name = file.getName();

        String extension = fileUtils.getFileExtension(name);

        if (extension == null){
            return false;
        }

        if (extension.equals("txt")){
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Text Documents (*.txt)";
    }
}
