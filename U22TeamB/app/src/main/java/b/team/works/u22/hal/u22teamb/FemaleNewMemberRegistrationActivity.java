package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FemaleNewMemberRegistrationActivity extends AppCompatActivity {

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

        EditText etFemaleName = findViewById(R.id.etInputName);
        String FemaleName = etFemaleName.getText().toString();

        EditText etFemaleBirthday = findViewById(R.id.etInputBirthdate);
        String FemaleBirthday = etFemaleBirthday.getText().toString();

        EditText etFemaleMail = findViewById(R.id.etInputMail);
        String FemaleMail = etFemaleMail.getText().toString();

        EditText etFemalePassword = findViewById(R.id.etInputPassword);
        String FemalePassword = etFemalePassword.getText().toString();



        Intent intent = new Intent(FemaleNewMemberRegistrationActivity.this,FemaleCardRegistrationActivity.class);
        startActivity(intent);
    }
}
