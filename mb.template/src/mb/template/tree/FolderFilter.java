package mb.template.tree;

import java.io.File;
import java.io.FileFilter;


public class FolderFilter implements FileFilter
{

    @Override
    public boolean accept(File f)
    {
        if(f.isDirectory() && !f.isHidden() || f.isFile()&& !f.isHidden())
        {
            return true;
        }
        return false;
    }


}
