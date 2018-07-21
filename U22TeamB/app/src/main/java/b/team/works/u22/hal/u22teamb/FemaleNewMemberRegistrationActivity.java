package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FemaleNewMemberRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.female_new_member_registration);
    }

    public void onMaleInformationClick(View view){
        Intent intent = new Intent(FemaleNewMemberRegistrationActivity.this,femaleMaleInformationRegistration.class);
        startActivity(intent);
    }
}
