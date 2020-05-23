package com.example.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class Frag extends  DialogFragment {
EditText editText;
Activity activity;
@Override
public void onAttach(Context context){
    super.onAttach(context);
    if(context instanceof Activity){
        activity=(Activity) context;

    }
}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment, null);
        editText = v.findViewById(R.id.edit);
        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(editText.getText().toString().equals("")) Toast.makeText(getActivity(),"Введите сумму",Toast.LENGTH_SHORT).show();
                else {
                    int value = Integer.parseInt(editText.getText().toString());
                    editText.setText("");
                    try {
                        ((Mesage) activity).fragmentvalue(value);
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
