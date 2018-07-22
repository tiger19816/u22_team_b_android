package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class FemaleCardRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_card_registration);
    }

    public void onMaleInformationClick(View view){

        Female female = (Female) getIntent().getSerializableExtra("FEMALE");

        Log.e("氏名",female.getFemaleName());

        Intent intent = new Intent(FemaleCardRegistrationActivity.this,FemaleMaleInformationRegistrationActivity.class);
        startActivity(intent);
    }
}
