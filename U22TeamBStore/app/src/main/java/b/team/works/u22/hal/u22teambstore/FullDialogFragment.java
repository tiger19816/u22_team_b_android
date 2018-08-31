package b.team.works.u22.hal.u22teambstore;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class FullDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.signout_dialog_title));
        builder.setMessage(getString(R.string.signout_dialog_message));
        builder.setPositiveButton(getString(R.string.signout_dialog_possitive) , new DialogButtonClickListener());
        builder.setNegativeButton(getString(R.string.signout_dialog_negative) , new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * ダイアログのボタンが押された時の処理が記述されたメンバクラス。
     */
    private class DialogButtonClickListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog , int which){
            Activity parent = getActivity();
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SharedPreferences setting = parent.getSharedPreferences("SHOPUSER", 0);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.remove("ID");
                    editor.commit();
                    Intent intent = new Intent(parent,MainActivity.class);
                    parent.finish();
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
