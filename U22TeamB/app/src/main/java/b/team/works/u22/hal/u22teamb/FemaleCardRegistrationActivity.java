package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
        setTheme(R.style.MyCustomTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_card_registration);

        setTitle(getString(R.string.female_card_registration_title));

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);

        Spinner etCardDoneDeadlineYear = findViewById(R.id.spInputCardExpirationYear);
        List<String> items = new ArrayList<String>();
        items.add("年");
        for(int i=(nowYear); i<(nowYear+7); i++){
            items.add(String.valueOf(i).substring(2 , 4));
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
        String cardDoneDeadline = etCardDoneDeadlineMonth.getSelectedItem() + "" + etCardDoneDeadlineYear.getSelectedItem();
        female.setFemaleCardExpirationDate(cardDoneDeadline);

        EditText etCardNominalName = findViewById(R.id.etInputCardCardHolder);
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
            Toast.makeText(FemaleCardRegistrationActivity.this , getString(R.string.female_card_registration_input_card_complete_2) , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * アクションバー。
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
