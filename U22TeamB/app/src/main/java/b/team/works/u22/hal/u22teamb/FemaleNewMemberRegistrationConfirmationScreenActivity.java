package b.team.works.u22.hal.u22teamb;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FemaleNewMemberRegistrationConfirmationScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.female_new_member_registration_confirmation_screen);
    }

    public void onFinishRegistrationClick(View view){
        Intent intent = new Intent(FemaleNewMemberRegistrationConfirmationScreenActivity.this,FemaleStoreMapListActivity.class);
        startActivity(intent);
    }
}
