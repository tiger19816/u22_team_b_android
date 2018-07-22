package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FemaleNewMemberRegistrationActivity extends AppCompatActivity {

    private List<String> _list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.female_new_member_registration);
    }

    /**
     *「次へボタンをクリックした時。」
     * @param view
     */
    public void onMaleInformationClick(View view){

        //妻情報のエンティティークラス
        Female female = new Female();

        String femaleIcon = "icon.jpg";
        female.setFemaleIcon(femaleIcon);

        EditText etFemaleName = findViewById(R.id.etInputName);
        String femaleName = etFemaleName.getText().toString();
        female.setFemaleName(femaleName);

        EditText etFemaleBirthday = findViewById(R.id.etInputBirthdate);
        String femaleBirthday = etFemaleBirthday.getText().toString();
        female.setFemaleBirthDay(femaleBirthday);

        EditText etFemaleMail = findViewById(R.id.etInputMail);
        String femaleMail = etFemaleMail.getText().toString();
        female.setFemaleMail(femaleMail);

        EditText etFemalePassword = findViewById(R.id.etInputPassword);
        String femalePassword = etFemalePassword.getText().toString();
        female.setFemalePassword(femalePassword);

        if(female.getInputChecked()) {
            Intent intent = new Intent(FemaleNewMemberRegistrationActivity.this, FemaleCardRegistrationActivity.class);
            intent.putExtra("FEMALE", female);
            startActivity(intent);
        }else{
            Toast.makeText(FemaleNewMemberRegistrationActivity.this , "入力チェック完了" , Toast.LENGTH_SHORT).show();
        }
    }
}
