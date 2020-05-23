package com.example.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

public class Frag2 extends DialogFragment {
    EditText editText;
    Activity activity;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof Activity){
            activity=(Activity) context;
        }
    }
    //Создание диалогового окна и вызов метода интерфейса в активити
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment2, null);
        editText = v.findViewById(R.id.edit2);
        Button button = (Button) v.findViewById(R.id.button2);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(editText.getText().toString().equals("")) Toast.makeText(getActivity(),"Введите сумму",Toast.LENGTH_SHORT).show();
                else {
                    int value = Integer.parseInt(editText.getText().toString());
                    editText.setText("");
                    try {
                        ((Summary) activity).fragsum(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dismiss();
                }
            }
        });

        return v;
    }
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
