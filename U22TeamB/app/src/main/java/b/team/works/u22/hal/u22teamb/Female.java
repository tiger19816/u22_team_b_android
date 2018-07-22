package b.team.works.u22.hal.u22teamb;

/**
 * 妻テーブル用クラス。
 * @autho Honoka Takada
 */

public class Female {

    private String femaleId;
    private String femaleName;
    private String femaleBirthDay;
    private String femaleMail;
    private String femalePassword;
    private String femaleIcon;
    private String femaleCardNo;
    private String femaleCardExpirationDate;
    private String femaleCardSecurityCode;
    private String femaleCardNominee;
    private String femaleLatitude;//緯度
    private String femaleLongitude;//経度

    private Boolean isInputChecked;

    private String femaleIdErrorMessage;
    private String femaleNameErrorMessage;
    private String femaleBirthDayErrorMessage;
    private String femaleMailErrorMessage;
    private String femalePasswordErrorMessage;
    private String femaleIconErrorMessage;
    private String femaleCardNoErrorMessage;
    private String femaleCardExpirationDateErrorMessage;
    private String femaleCardSecurityCodeErrorMessage;
    private String femaleCardNomineeErrorMessage;
    private String femaleLatitudeErrorMessage;//緯度
    private String femaleLongitudeErrorMessage;//経度

    public Female(){
        this.femaleId = "";
        this.femaleName = "";
        this.femaleBirthDay = "";
        this.femaleMail = "";
        this.femalePassword = "";
        this.femaleIcon = "icon.jpg";
        this.femaleCardNo = "";
        this.femaleCardExpirationDate = "";
        this.femaleCardSecurityCode = "";
        this.femaleCardNominee = "";
        this.femaleLatitude = "";//緯度
        this.femaleLongitude = "";//経度

        this.isInputChecked = true;

        this.femaleIdErrorMessage = "";
        this.femaleNameErrorMessage = "";
        this.femaleBirthDayErrorMessage = "";
        this.femaleMailErrorMessage = "";
        this.femalePasswordErrorMessage = "";
        this.femaleIconErrorMessage = "";
        this.femaleCardNoErrorMessage = "";
        this.femaleCardExpirationDateErrorMessage = "";
        this.femaleCardSecurityCodeErrorMessage = "";
        this.femaleCardNomineeErrorMessage = "";
        this.femaleLatitudeErrorMessage = "";//緯度
        this.femaleLongitudeErrorMessage = "";//経度
    }

    public Female(String femaleId , String femaleName , String femaleBirthDay , String femaleMail , String femalePassword , String femaleIcon , String femaleCardNo , String femaleCardExpirationDate , String femaleCardSecurityCode , String femaleCardNominee , String femaleLatitude , String femaleLongitude){
        this.femaleId = femaleId;
        this.femaleName = femaleName;
        this.femaleBirthDay = femaleBirthDay;
        this.femaleMail = femaleMail;
        this.femalePassword = femalePassword;
        this.femaleIcon = femaleIcon;
        this.femaleCardNo = femaleCardNo;
        this.femaleCardExpirationDate = femaleCardExpirationDate;
        this.femaleCardSecurityCode = femaleCardSecurityCode;
        this.femaleCardNominee = femaleCardNominee;
        this.femaleLatitude = femaleLatitude;//緯度
        this.femaleLongitude = femaleLongitude;//経度

        this.isInputChecked = true;

        this.femaleIdErrorMessage = "";
        this.femaleNameErrorMessage = "";
        this.femaleBirthDayErrorMessage = "";
        this.femaleMailErrorMessage = "";
        this.femalePasswordErrorMessage = "";
        this.femaleIconErrorMessage = "";
        this.femaleCardNoErrorMessage = "";
        this.femaleCardExpirationDateErrorMessage = "";
        this.femaleCardSecurityCodeErrorMessage = "";
        this.femaleCardNomineeErrorMessage = "";
        this.femaleLatitudeErrorMessage = "";//緯度
        this.femaleLongitudeErrorMessage = "";//経度
    }

    public Boolean getInputChecked() { return isInputChecked; }

    public String getFemaleId(){return femaleId;}
    public String getFemaleIdErrorMessage(){return this.femaleIdErrorMessage;}
    public void setFemaleId(String femaleId){
        this.femaleId = femaleId;
        if("".equals(femaleId)){
            this.femaleIdErrorMessage = "IDを入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleName(){return femaleName;}
    public String getFemaleNameErrorMessage(){return femaleNameErrorMessage;}
    public void setFemaleName(String femaleName){
        this.femaleName = femaleName;
        if("".equals(femaleName)){
            this.femaleNameErrorMessage = "氏名を入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleBirthDay(){return femaleBirthDay;}
    public String getFemaleBirthDayErrorMessage(){return femaleBirthDayErrorMessage;}
    public void setFemaleBirthDay(String femaleBirthDay){
        this.femaleBirthDay = femaleBirthDay;
        if("".equals(femaleBirthDay)){
            this.femaleBirthDayErrorMessage = "生年月日を選択してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleMail(){return femaleMail;}
    public String getFemaleMailErrorMessage(){return femaleMailErrorMessage;}
    public void setFemaleMail(String femaleMail){
        this.femaleMail = femaleMail;
        if("".equals(femaleMail)){
            this.femaleMailErrorMessage = "メールアドレスを入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemalePassword(){return femalePassword;}
    public String getFemalePasswordErrorMessage(){return femalePasswordErrorMessage;}
    public void setFemalePassword(String femalePassword){
        this.femalePassword = femalePassword;
        if("".equals(femalePassword)){
            this.femalePasswordErrorMessage = "パスワードを入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleIcon(){return femaleIcon;}
    public String getFemaleIconErrorMessage(){return femaleIconErrorMessage;}
    public void setFemaleIcon(String femaleIcon){
        this.femaleIcon = femaleIcon;
        if("".equals(femaleIcon)){
            this.femaleIconErrorMessage = "アイコン用の画像を選択してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleCardNo(){return femaleCardNo;}
    public String getFemaleCardNoErrorMessage(){return femaleCardNoErrorMessage;}
    public void setFemaleCardNo(String femaleCardNo){
        this.femaleCardNo = femaleCardNo;
        if("".equals(femaleCardNo)){
            this.femaleCardNoErrorMessage = "カードNoを入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleCardExpirationDate(){return femaleCardExpirationDate;}
    public String getFemaleCardExpirationDateErrorMessage(){return femaleCardExpirationDateErrorMessage;}
    public void setFemaleCardExpirationDate(String femaleCardExpirationDate){
        this.femaleCardExpirationDate = femaleCardExpirationDate;
        if("".equals(femaleCardExpirationDate)){
            this.femaleCardExpirationDateErrorMessage = "有効期限を入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleCardSecurityCode(){return femaleCardSecurityCode;}
    public String getFemaleCardSecurityCodeErrorMessage(){return femaleCardSecurityCodeErrorMessage;}
    public void setFemaleCardSecurityCode(String femaleCardSecurityCode){
        this.femaleCardSecurityCode = femaleCardSecurityCode;
        if("".equals(femaleCardSecurityCode)){
            this.femaleCardSecurityCodeErrorMessage = "セキュリティーコードを入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleCardNominee(){return femaleCardNominee;}
    public String getFemaleCardNomineeErrorMessage(){return femaleCardNomineeErrorMessage;}
    public void setFemaleCardNominee(String femaleCardNominee){
        this.femaleCardNominee = femaleCardNominee;
        if("".equals(femaleCardNominee)){
            this.femaleCardNominee = "名義名を入力してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleLatitude(){return femaleLatitude;}
    public String getFemaleLatitudeErrorMessage(){return femaleLatitudeErrorMessage;}
    public void setFemaleLatitude(String femaleLatitude){
        this.femaleLatitude = femaleLatitude;
        if("".equals(femaleLatitude)){
            this.femaleLatitudeErrorMessage = "登録時点を選択してください";
            this.isInputChecked = false;
        }
    }

    public String getFemaleLongitude(){return femaleLongitude;}
    public String getFemaleLongitudeErrorMessage(){return femaleLongitudeErrorMessage;}
    public void setFemaleLongitude(String femaleLongitude){
        this.femaleLongitude = femaleLongitude;
        if("".equals(femaleLongitude)){
            this.femaleLongitudeErrorMessage = "登録時点を選択してください";
            this.isInputChecked = false;
        }
    }
}
