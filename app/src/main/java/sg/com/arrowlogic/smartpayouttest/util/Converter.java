package sg.com.arrowlogic.smartpayouttest.util;

import android.widget.EditText;

public class Converter {

    public static int stringToInt(String num){
        return num.equals("") ? 0 : Integer.parseInt(num);
    }

    public static String textFromEditText(EditText editText){
        return editText.getText().toString();
    }
}
