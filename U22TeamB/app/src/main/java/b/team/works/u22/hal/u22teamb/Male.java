package b.team.works.u22.hal.u22teamb;

import java.io.Serializable;

public class Male  implements Serializable {

    private String maleId;
    private String malePassword;
    private String maleBirthday;
    private String maleHeight;
    private String maleWeight;
    private String maleProfession;

    private Boolean isInputChecked;

    private String maleIdErrorMessage;
    private String malePasswordErrorMessage;
    private String maleBirthdayErrorMessage;
    private String maleHeightErrorMessage;
    private String maleWeightErrorMessage;
    private String maleProfessionErrorMessage;

    public Male(){
        this.maleId = "";
        this.malePassword = "";
        this.maleBirthday = "";
        this.maleHeight = "";
        this.maleWeight = "";
        this.maleProfession = "";

        this.isInputChecked = true;

        this.maleIdErrorMessage = "";
        this.malePasswordErrorMessage = "";
        this.maleBirthdayErrorMessage = "";
        this.maleHeightErrorMessage = "";
        this.maleWeightErrorMessage = "";
        this.maleProfessionErrorMessage = "";
    }

    public Male(String maleId , String malePassword , String maleBirthday , String maleHeight , String maleWeight , String maleProfession){
        this.maleId = maleId;
        this.malePassword = malePassword;
        this.maleBirthday = maleBirthday;
        this.maleHeight = maleHeight;
        this.maleWeight = maleWeight;
        this.maleProfession = maleProfession;

        this.isInputChecked = true;

        this.maleIdErrorMessage = "";
        this.malePasswordErrorMessage = "";
        this.maleBirthdayErrorMessage = "";
        this.maleHeightErrorMessage = "";
        this.maleWeightErrorMessage = "";
        this.maleProfessionErrorMessage = "";
    }

    public Boolean getInputChecked() { return isInputChecked; }
    public void setInputChecked() { this.isInputChecked = true; }


    public String getMaleId(){return maleId;}
    public String getMaleIdErrorMessage(){return maleIdErrorMessage;}
    public void setMaleId(String maleId){
        this.maleId = maleId;
        if("".equals(maleId)){
            this.maleIdErrorMessage = "IDを入力してください";
            this.isInputChecked = false;
        }
    }

    public String getMalePassword(){return malePassword;}
    public String getMalePasswordErrorMessage(){return malePasswordErrorMessage;}
    public void setMalePassword(String malePassword){
        this.malePassword = malePassword;
        if("".equals(malePassword)){
            this.malePasswordErrorMessage = "パスワードを入力してください。";
            this.isInputChecked = false;
        }
    }

    public String getMaleBirthday(){return maleBirthday;}
    public String getMaleBirthdayErrorMessage(){return maleBirthdayErrorMessage;}
    public void setMaleBirthday(String maleBirthday){
        this.maleBirthday = maleBirthday;
        if("".equals(maleBirthday)){
            this.maleBirthdayErrorMessage = "生年月日を入力してください。";
            this.isInputChecked = false;
        }
    }

    public String getMaleHeight(){return maleHeight;}
    public String getMaleHeightErrorMessage(){return maleHeightErrorMessage;}
    public void setMaleHeight(String maleHeight){
        this.maleHeight = maleHeight;
        if("".equals(maleHeight)){
            this.maleHeightErrorMessage = "身長を入力してください。";
            this.isInputChecked = false;
        }
    }

    public String getMaleWeight(){return maleWeight;}
    public String getMaleWeightErrorMessage() { return maleWeightErrorMessage; }
    public void setMaleWeight(String maleWeight){
        this.maleWeight = maleWeight;
        if("".equals(maleWeight)){
            this.maleWeightErrorMessage = "体重を入力してください。";
            this.isInputChecked = false;
        }
    }

    public String getMaleProfession(){return maleProfession;}
    public String getMaleProfessionErrorMessage() { return maleProfessionErrorMessage; }
    public void setMaleProfession(String maleProfession) {
        this.maleProfession = maleProfession;
        if("".equals(maleProfession)){
            this.maleProfessionErrorMessage = "職業を入力してください。";
            this.isInputChecked = false;
        }
    }
}


