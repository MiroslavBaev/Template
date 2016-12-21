/**
 * 
 */
package mb.template.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @author Mbaev
 *
 */
public class PlaceholderPattern
{
    // "(?i)__Prefix____TaskName__";

    private final static List<Pattern> patterns = new ArrayList<>();



    public PlaceholderPattern()
    {
        
    }



    public static List<Pattern> getPatterns()
    {
        setPatterns("__Prefix____TaskName__", false);
        //setPatterns("__TaskName__", false);
        
        return patterns;
    }



    public static void setPatterns(String pattern, boolean caseSensitive)
    {
        String notCaseSensitiveRegex = "(?i)";
        
        if(!caseSensitive)
        {
            pattern = notCaseSensitiveRegex + pattern;
        }
        
        patterns.add(Pattern.compile(pattern));
    }
}
