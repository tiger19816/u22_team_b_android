package b.team.works.u22.hal.u22teamb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class FemaleMaleInformationRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_male_information_registration);

        // アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 入力確認画面に遷移するボタンをクリックした時。
     * @param view
     */
    public void onCardRegistrationClick(View view){

        Female female = (Female) getIntent().getSerializableExtra("FEMALE");
        female.setInputChecked();

        Male male = new Male();

        if(female.getInputChecked()) {
            Intent intent = new Intent(FemaleMaleInformationRegistrationActivity.this,FemaleNewMemberRegistrationConfirmationScreenActivity.class);
            intent.putExtra("FEMALE", female);
            startActivity(intent);
        }else{
            Toast.makeText(FemaleMaleInformationRegistrationActivity.this , "入力チェック3完了" , Toast.LENGTH_SHORT).show();
        }

   }


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
