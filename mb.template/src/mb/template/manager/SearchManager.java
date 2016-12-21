/**
 * 
 */
package mb.template.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author mbaev
 *
 */
public class SearchManager
{
    private List<String> foundMatches;
    private List<Pattern> patterns;
    
    boolean caseSensitive;
    
    private Matcher matcher;




    public SearchManager(List<Pattern> patterns)
    {
        this.foundMatches = new ArrayList<>();
        this.patterns = patterns;
        
        this.matcher = null;
    }



    /*
     * 
     * Search in content by pattern
     * 
     */
    public List<String> search(String content)
    {
        for (Pattern pattern : patterns)
        {
            matcher = pattern.matcher(content);

            while (matcher.find())
            {
                foundMatches.add(matcher.group());
            }

        }

        return foundMatches;
    }



    /*
     * Escape placeholder
     */
    public String escapeName(String placeholder)
    {
        char openBracket = '{';
        char closeBracket = '}';
        char specialSymbol = '$';
        String escapeSymbol = "\\";

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < placeholder.length(); i++)
        {
            if (placeholder.charAt(i) == openBracket
                    || placeholder.charAt(i) == closeBracket
                    || placeholder.charAt(i) == specialSymbol)
            {
                result.append(escapeSymbol + placeholder.charAt(i));
            }
            else
            {
                result.append(placeholder.charAt(i));
            }

        }

        return result.toString();
    }



    /*
     * Remove extension from file name
     */
    public String removeExtensionFromFile(String filename)
    {
        String DOT = ".";

        if (filename.lastIndexOf(DOT) > 0)
        {
            filename = filename.substring(0, filename.lastIndexOf(DOT));

            return filename;
        }

        return filename;
    }


}
