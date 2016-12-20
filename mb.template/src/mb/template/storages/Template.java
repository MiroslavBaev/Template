/**
 * 
 */
package mb.template.storages;

import java.io.Serializable;


/**
 * @author mbaev
 *
 */
public class Template implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String path;
    private int numberOfSelections;


    public Template(String path, int numberOfSelections)
    {
        this.path = path;
        
        this.numberOfSelections = numberOfSelections;
    }
    




    public String getPath()
    {
        return path;
    }



    public void setPath(String path)
    {
        this.path = path;
    }



    public int getNumberOfSelections()
    {
        return numberOfSelections;
    }



    public void setNumberOfSelections(int numberOfSelections)
    {
        this.numberOfSelections = numberOfSelections;
    }


}
