package com.arrowland.arrowland.Classes;

import android.widget.EditText;

/**
 * Created by Mhack Bautista on 7/21/2018.
 */

public class FieldValidation {

    public boolean isEmpty(EditText et) {
        String field = et.getText().toString();

        if(field.isEmpty()) {
            return true;
        }

        return false;
    }

    public boolean setMinLen(EditText et, int len) {
        String field = et.getText().toString();

        if(field.length() > 0 && field.length() < len) {
            return true;
        }
        return false;

    }


}
