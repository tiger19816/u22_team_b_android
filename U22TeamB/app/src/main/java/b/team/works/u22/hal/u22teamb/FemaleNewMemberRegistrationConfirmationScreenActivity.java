package b.team.works.u22.hal.u22teamb;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

public class FemaleNewMemberRegistrationConfirmationScreenActivity extends AppCompatActivity {

    private String femaleName;
    private String femaleBirthday;
    private String femalePassword;
    private String femaleMail;
    private String femaleIcon;
    private String femaleCardNo;
    private String femaleCardDoneDeadline;
    private String femaleCardSecurityCode;
    private String femaleCardNomineeName;
    private String femaleLatitude;//緯度
    private String femaleLongitude;//経度

    private String maleBirthday;
    private String maleHeight;
    private String maleWeight;
    private String maleProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.female_new_member_registration_confirmation_screen);

        Female female = (Female) getIntent().getSerializableExtra("FEMALE");
        Male male = (Male) getIntent().getSerializableExtra("MALE");

        this.femaleName = female.getFemaleName();
        this.femaleBirthday = female.getFemaleBirthDay();
        this.femalePassword = female.getFemalePassword();
        this.femaleMail = female.getFemaleMail();
        this.femaleIcon = female.getFemaleIcon();
        this.femaleCardNo = female.getFemaleCardNo();
        this.femaleCardDoneDeadline = female.getFemaleCardExpirationDate();
        this.femaleCardSecurityCode = female.getFemaleCardSecurityCode();
        this.femaleCardNomineeName = female.getFemaleCardNominee();
        this.femaleLatitude = female.getFemaleLatitude();
        this.femaleLongitude = female.getFemaleLongitude();

        this.maleBirthday = male.getMaleBirthday();
        this.maleHeight = male.getMaleHeight();
        this.maleWeight = male.getMaleWeight();
        this.maleProfession = male.getMaleProfession();
    }

    public void onFinishRegistrationClick(View view){
        Intent intent = new Intent(FemaleNewMemberRegistrationConfirmationScreenActivity.this,FemaleStoreMapListActivity.class);
        startActivity(intent);
    }
}
