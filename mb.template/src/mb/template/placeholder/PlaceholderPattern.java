/**
 * 
 */
package mb.template.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @author mbaev
 *
 */
public class PlaceholderPattern
{
    private  List<Pattern> patterns = new ArrayList<>();



    public PlaceholderPattern()
    {
        setPatterns(Pattern.compile("\\$\\{(.*?)\\}"));
    }



    public List<Pattern> getPatterns()
    {
        return patterns;
    }
    
    private void setPatterns(Pattern pattern)
    {
        patterns.add(pattern);
    }


}
