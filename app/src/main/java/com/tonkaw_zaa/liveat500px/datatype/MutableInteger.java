package com.tonkaw_zaa.liveat500px.datatype;

import android.os.Bundle;

/**
 * Created by Tonkaw_Zaa on 5/11/2017.
 */

public class MutableInteger {

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public MutableInteger(int value) {
        this.value = value;
    }

    private int value;

    public Bundle onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        value = savedInstanceState.getInt("value");
    }
}
