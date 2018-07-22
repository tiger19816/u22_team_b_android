package b.team.works.u22.hal.u22teamb;

import java.io.Serializable;

public class Male  implements Serializable {

    private String maleId;
    private String maleName;

    private Boolean isInputChecked;

    private String maleNameErrorMessage;

    public Male(){
        this.isInputChecked = true;
    }

    public Boolean getInputChecked() { return isInputChecked; }
    public void setInputChecked() { this.isInputChecked = true; }


    public String getMaleName(){return maleName;}
    public String getMaleNameErrorMessage(){return maleNameErrorMessage;}
    public void setMaleName(String maleName){
        this.maleName = maleName;
        if("".equals(maleName)){
            this.maleNameErrorMessage = "氏名を入力してください";
            this.isInputChecked = false;
        }
    }
}
