package b.team.works.u22.hal.u22teambstore;

/**
 * 定数を集めたクラス。
 *
 * @author Honoka Takada
 */

public class Word {

    //    private static final String SERVER_URL = "http://192.168.1.20:8080/U22TeamB/";
    private static final String SERVER_URL = "http://10.0.2.2:8080/u22_team_b_web/";
    public static final String USER_LOGIN_URL = SERVER_URL + "StoreLoginJsonServlet";

    //ReservationListActivityとReservationDetailActivityのURL。 by Yuki Yoshida TODO:適切なURLに変更。
    private static final String MY_URL = "http://10.0.2.2:8080/team_b_web/";
    public static final String RECEIVE_RESERVATION_LIST = MY_URL + "SendReservationListServlet";
    public static final String RECEIVE_RESERVATION_DETAIL = MY_URL + "SendReservationDetailServlet";
    public static final String PROCESS_OF_VISIT = MY_URL + "UpdateVisitFlagServlet";
}
