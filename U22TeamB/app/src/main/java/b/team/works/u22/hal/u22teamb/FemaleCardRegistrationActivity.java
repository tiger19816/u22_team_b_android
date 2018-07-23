package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FemaleCardRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_card_registration);

        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);

        Spinner etCardDoneDeadlineYear = findViewById(R.id.spInputCardExpirationYear);
        List<String> items = new ArrayList<String>();
        items.add("年");
        for(int i=(nowYear); i<(nowYear+7); i++){
            items.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_dropdown_item , items);
        etCardDoneDeadlineYear.setAdapter(adapter);
    }

    public void onMaleInformationClick(View view){

        Female female = (Female) getIntent().getSerializableExtra("FEMALE");
        female.setInputChecked();

        EditText etCardNo = findViewById(R.id.etInputCreditCardNumber);
        String cardNo = etCardNo.getText().toString();
        female.setFemaleCardNo(cardNo);

        Spinner etCardDoneDeadlineMonth = findViewById(R.id.spInputCardExpirationMonth);
        Spinner etCardDoneDeadlineYear = findViewById(R.id.spInputCardExpirationYear);
        String cardDoneDeadline = etCardDoneDeadlineMonth.getSelectedItem() + "/" + etCardDoneDeadlineYear.getSelectedItem();
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
