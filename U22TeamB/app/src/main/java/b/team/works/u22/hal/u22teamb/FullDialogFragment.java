package b.team.works.u22.hal.u22teamb;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * 通常ダイアログクラス。
 */

public class FullDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(getString(R.string.dialog_message));
        builder.setPositiveButton(getString(R.string.dialog_female) , new DialogButtonClickListener());
        builder.setNeutralButton(getString(R.string.dialog_cancel) , new DialogButtonClickListener());
        builder.setNegativeButton(getString(R.string.dialog_male) , new DialogButtonClickListener());
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
            Intent intent;
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    intent = new Intent(parent,FemaleNewMemberRegistrationActivity.class);
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    intent = new Intent(parent,MaleAdditionalInformationEntryActivity.class);
                    startActivity(intent);
            }
        }

    }
}
