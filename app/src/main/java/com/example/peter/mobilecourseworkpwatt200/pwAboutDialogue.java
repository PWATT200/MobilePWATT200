package com.example.peter.mobilecourseworkpwatt200;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Peter on 12/12/2014.
 */
public class pwAboutDialogue extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder pwAboutDialogue = new AlertDialog.Builder(getActivity());
        pwAboutDialogue.setMessage("This app contains the location of weather station around Glasgow and the weather report").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        pwAboutDialogue.setTitle("About");
        pwAboutDialogue.setIcon(R.drawable.ic_action_about);
        return pwAboutDialogue.create();
    }

}
