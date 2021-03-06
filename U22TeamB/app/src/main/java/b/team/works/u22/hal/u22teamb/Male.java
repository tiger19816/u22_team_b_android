package b.team.works.u22.hal.u22teamb;

import java.io.Serializable;

public class Male  implements Serializable {

    private String maleId;
    private String maleName;
    private String malePassword;
    private String maleBirthday;
    private String maleMail;
    private String maleHeight;
    private String maleWeight;
    private String maleProfession;
    private String maleProfessionName;

    private Boolean isInputChecked;

    private String maleIdErrorMessage;
    private String maleErrorName;
    private String malePasswordErrorMessage;
    private String maleBirthdayErrorMessage;
    private String maleMailErrorMessage;
    private String maleHeightErrorMessage;
    private String maleWeightErrorMessage;
    private String maleProfessionErrorMessage;

    public Male(){
        this.maleId = "";
        this.maleName = "";
        this.malePassword = "";
        this.maleBirthday = "";
        this.maleMail = "";
        this.maleHeight = "";
        this.maleWeight = "";
        this.maleProfession = "";
        this.maleProfessionName = "";

        this.isInputChecked = true;

        this.maleIdErrorMessage = "";
        this.maleErrorName = "";
        this.malePasswordErrorMessage = "";
        this.maleBirthdayErrorMessage = "";
        this.maleMailErrorMessage = "";
        this.maleHeightErrorMessage = "";
        this.maleWeightErrorMessage = "";
        this.maleProfessionErrorMessage = "";
    }

    public Male(String maleId , String maleName ,  String malePassword , String maleBirthday , String maleMail , String maleHeight , String maleWeight , String maleProfession){
        this.maleId = maleId;
        this.maleName = maleName;
        this.malePassword = malePassword;
        this.maleBirthday = maleBirthday;
        this.maleMail = maleMail;
        this.maleHeight = maleHeight;
        this.maleWeight = maleWeight;
        this.maleProfession = maleProfession;
        this.maleProfessionName = setMaleProfessionName(maleProfession);

        this.isInputChecked = true;

        this.maleIdErrorMessage = "";
        this.maleErrorName = "";
        this.malePasswordErrorMessage = "";
        this.maleBirthdayErrorMessage = "";
        this.maleMailErrorMessage = "";
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

    public String getMaleName() {
        return maleName;
    }

    public String getMaleErrorName() {
        return maleErrorName;
    }
    public void setMaleName(String maleName) {
        this.maleName = maleName;
        if("".equals(maleName)){
            this.maleErrorName = "氏名を入力して下さい。";
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

    public String getMaleMail(){return maleMail;}
    public String getMaleMailErrorMessage(){return maleMailErrorMessage;}
    public void setMaleMail(String maleMail){
        this.maleMail = maleMail;
        if("".equals(maleMail)){
            this.maleMailErrorMessage = "メールアドレスを入力してください。";
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
        if("0".equals(maleProfession)){
            this.maleProfessionErrorMessage = "職業を入力してください。";
            this.isInputChecked = false;
        }else{
            setMaleProfessionName(maleProfession);
        }
    }

    public String setMaleProfessionName(String maleProfessionId){

        switch (Integer.parseInt(maleProfessionId)){
            case 1:
                this.maleProfessionName = "公務員";
                break;
            case 2:
                this.maleProfessionName = "経営者・役員";
                break;
            case 3:
                this.maleProfessionName = "会社員";
                break;
            case 4:
                this.maleProfessionName = "自営業";
                break;
            case 5:
                this.maleProfessionName = "自由業";
                break;
            case 6:
                this.maleProfessionName = "専業主夫";
                break;
            case 7:
                this.maleProfessionName = "パート・アルバイト";
                break;
            case 8:
                this.maleProfessionName = "学生";
                break;
            case 9:
                this.maleProfessionName = "その他";
                break;
        }
        return maleProfessionName;
    }
}


