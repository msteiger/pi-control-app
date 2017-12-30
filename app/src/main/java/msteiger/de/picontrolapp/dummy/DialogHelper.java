package msteiger.de.picontrolapp.dummy;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import msteiger.de.picontrolapp.R;

/**
 * Created by msteiger on 30.12.2017.
 */

public final class DialogHelper {

    public static AlertDialog createErrorDialog(final Activity activity, String message, final boolean terminate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(R.string.error_dialog_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (terminate) {
                    activity.finish();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        return dialog;
    }

    private DialogHelper() {

    }
}
