package org.koishi.launcher.h2co3.launcher.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.koishi.launcher.h2co3.resources.component.activity.H2CO3Activity;
import org.koishi.launcher.h2co3.resources.component.dialog.H2CO3MessageDialog;

@Keep
public class ExitActivity extends H2CO3Activity {

    @SuppressLint("StringFormatInvalid")
    //invalid on some translations but valid on most, cant fix that atm
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int code = -1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            code = extras.getInt("code", -1);
        }

        AlertDialog exitDialog = new H2CO3MessageDialog(this)
                .setMessage("ERROR" + code)
                .setPositiveButton("Exit", (dialog, which) -> finish())
                .setOnDismissListener(dialog -> ExitActivity.this.finish())
                .show();
    }

    public static void showExitMessage(Context ctx, int code) {
        Intent i = new Intent(ctx,ExitActivity.class);
        i.putExtra("code",code);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

}
