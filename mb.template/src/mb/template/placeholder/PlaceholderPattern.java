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

    private  List<Pattern> patterns = new ArrayList<>();



    public PlaceholderPattern()
    {
        patterns.add(Pattern.compile("\\$\\{(.*?)\\}"));
    }



    public List<Pattern> getPatterns()
    {
        
        return patterns;
    }


//
//    public static void setPatterns(String pattern, boolean caseSensitive)
//    {
//        String notCaseSensitiveRegex = "(?i)";
//        
//        if(!caseSensitive)
//        {
//            pattern = notCaseSensitiveRegex + pattern;
//        }
//        
//        patterns.add(Pattern.compile(pattern));
//    }
}
