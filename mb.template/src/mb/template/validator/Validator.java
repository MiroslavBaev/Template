package mb.template.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * 
 * Validate user input
 * 
 * @author mbaev
 * 
 */
public class Validator
{

    private final static String START_WITH_ALPHABET_LETTER_REGEX = "[^A-Za-z]+";
    
    public static  boolean filenameIsEmpty(String filename)
    {
        if (filename.isEmpty() || filename == null || filename == "")
        {
            return true;
        }

        return false;
    }



    public static boolean filenameStartWithNumberOrSymbol(String filename)
    {
        if (filenameIsEmpty(filename))
        {
            return filenameIsEmpty(filename);
        }

        Pattern pattern = Pattern.compile(START_WITH_ALPHABET_LETTER_REGEX);

        String firstChar = String.valueOf(filename.charAt(0));
        Matcher matcher = pattern.matcher(firstChar);

        if (matcher.find())
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    
}
