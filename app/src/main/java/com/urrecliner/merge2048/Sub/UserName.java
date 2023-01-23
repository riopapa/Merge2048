package com.urrecliner.merge2048.Sub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import com.urrecliner.merge2048.GInfo;

public class UserName {

    public void get(Context context, GInfo gInfo) {

        Activity activity = (Activity) context;
        activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("ID를 변경할 수 있습니다");
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            if (gInfo.userName.length() == 0)
                gInfo.userName = "YourID";
            input.setText(gInfo.userName);
            input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
            builder.setView(input);
            builder.setPositiveButton("이 ID를 씁니다", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    gInfo.userName = input.getText().toString();
                    dialog.cancel();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("수정 안 할래요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                    dialog.dismiss();
                }
            });
            builder.show();
        });
    }
}
//        get.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);