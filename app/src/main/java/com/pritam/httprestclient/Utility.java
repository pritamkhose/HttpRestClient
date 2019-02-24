package com.pritam.httprestclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Utility {

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public static void alertDialog(Activity activity, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        // Setting Dialog Title
        if (title != null)
            alertDialog.setTitle(title);

        // Setting Dialog Message
        if (message != null)
            alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.ic_launcher_foreground);
        alertDialog.setCancelable(false);

        // Setting Cancel Button
        alertDialog.setButton( activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    static void alertDialogBack(final Activity activity) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        alertDialog.setMessage(activity.getResources().getString(R.string.wewillback));
        alertDialog.setCancelable(false);
        alertDialog.setButton(activity.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                activity.onBackPressed();
            }
        });

        alertDialog.show();
    }


    public static boolean isNetworkAvaiable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

}
