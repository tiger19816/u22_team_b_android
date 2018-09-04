package b.team.works.u22.hal.u22teambstore;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class FullDialogFragment2 extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("確認");
        // 元画面から値を取得
        String storeId = getArguments().getString("id");
        builder.setMessage(storeId + "\nこのIDで登録されました。ログイン時このIDを使用します。");
        builder.setPositiveButton("OK" , new DialogButtonClickListener());
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
                    String storeName = getArguments().getString("name");
                    Toast.makeText(parent, getString(R.string.main_welcome_message)+storeName+getString(R.string.honor_title), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(parent, ReservationListActivity.class);
                    startActivity(intent);
                    parent.finish();
                    break;
            }
        }
    }
}
