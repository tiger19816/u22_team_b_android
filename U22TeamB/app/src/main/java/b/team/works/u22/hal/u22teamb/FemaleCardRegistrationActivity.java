package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FemaleCardRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_card_registration);
    }

    public void onMaleInformationClick(View view){

        Female female = (Female) getIntent().getSerializableExtra("FEMALE");
        female.setInputChecked();

        EditText etCardNo = findViewById(R.id.etInputCreditCardNumber);
        String cardNo = etCardNo.getText().toString();
        female.setFemaleCardNo(cardNo);

        EditText etCardDoneDeadline = findViewById(R.id.etInputCardExpirationDate);
        String cardDoneDeadline = etCardDoneDeadline.getText().toString();
        female.setFemaleCardExpirationDate(cardDoneDeadline);

        EditText etCardNominalName = findViewById(R.id.etInputCreditCardNumber);
        String cardNominalName = etCardNominalName.getText().toString();
        female.setFemaleCardNominee(cardNominalName);

        EditText etCardSecurityKey = findViewById(R.id.etInputCardSecurityCode);
        String cardSecurityKey = etCardSecurityKey.getText().toString();
        female.setFemaleCardSecurityCode(cardSecurityKey);

        if(female.getInputChecked()) {
            Intent intent = new Intent(FemaleCardRegistrationActivity.this,FemaleMaleInformationRegistrationActivity.class);
            intent.putExtra("FEMALE", female);
            startActivity(intent);
        }else{
            Toast.makeText(FemaleCardRegistrationActivity.this , "入力チェック2完了" , Toast.LENGTH_SHORT).show();
        }
    }
}
