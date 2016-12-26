/**
 * 
 */
package mb.template.storages;

import java.io.Serializable;


/**
 * @author mbaev
 *
 */
public class TemplateFolder implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String path;
    private int numberOfSelections;


    public TemplateFolder(String path, int numberOfSelections)
    {
        this.path = path;
        
        this.numberOfSelections = numberOfSelections;
    }
    




    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }





    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TemplateFolder other = (TemplateFolder) obj;
        if (path == null)
        {
            if (other.path != null)
                return false;
        }
        else if (!path.equals(other.path))
            return false;
        return true;
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
