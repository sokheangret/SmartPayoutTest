package sg.com.arrowlogic.smartpayouttest.util;

import android.widget.EditText;

public class Converter {

    /**
     * Convert string to integer
     * @param num
     * @return integer
     */
    public static int stringToInt(String num){
        return num.equals("") ? 0 : Integer.parseInt(num);
    }

    /**
     * Get text from edit text to string
     * @param editText
     * @return string
     */
    public static String textFromEditText(EditText editText){
        return editText.getText().toString();
    }
}
