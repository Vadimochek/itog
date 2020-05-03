package com.example.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Frag2 extends Fragment {
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
        View v = inflater.inflate(R.layout.fragment2, null);
        editText = v.findViewById(R.id.edit2);
        Button button = (Button) v.findViewById(R.id.button2);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int value = Integer.parseInt(editText.getText().toString());
                editText.setText("");
                try{
                    ((Summary) activity).fragsum(value);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(),"Сохранено",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
