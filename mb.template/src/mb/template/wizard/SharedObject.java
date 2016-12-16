/**
 * 
 */
package mb.template.wizard;

import java.util.ArrayList;
import java.util.List;

import mb.template.placeholder.PlaceholderBean;


/**
 * Shared object used for transmission data between pages
 * 
 * @author mbaev
 *
 */
public class SharedObject
{
    private String templatePath; // Who template path is selected
    private String projectPath; // Who project path is selected

    private List<PlaceholderBean> placeholders;



    public String getProjectPath()
    {
        return this.projectPath;
    }



    public void setProjectPath(String projectPath)
    {
        this.projectPath = projectPath;
    }



    public SharedObject()
    {
        this.templatePath = null;
        this.placeholders = new ArrayList<>();
    }



    public List<PlaceholderBean> getPlaceholders()
    {
        return this.placeholders;
    }



    public void setPlaceholders(List<PlaceholderBean> placeholders)
    {
        this.placeholders = placeholders;
    }



    public String getTemplatePath()
    {
        return this.templatePath;
    }



    public void setTemplatePath(String path)
    {
        this.templatePath = path;
    }

}
