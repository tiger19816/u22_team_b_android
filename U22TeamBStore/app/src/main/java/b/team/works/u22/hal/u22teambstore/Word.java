package b.team.works.u22.hal.u22teambstore;

/**
 * 定数を集めたクラス。
 *
 * @author Honoka Takada
 */

public class Word {

    private static final String SERVER_URL = "http://59.106.219.240:8080/u22_team_b_web/";
//    private static final String SERVER_URL = "http://10.0.2.2:8080/u22_team_b_web/";//localhostアドレス
    public static final String USER_LOGIN_URL = SERVER_URL + "StoreLoginJsonServlet";
    public static final String RECEIVE_RESERVATION_LIST = SERVER_URL + "SendReservationListServlet";
    public static final String RECEIVE_RESERVATION_DETAIL = SERVER_URL + "SendReservationDetailServlet";
    public static final String PROCESS_OF_VISIT = SERVER_URL + "UpdateVisitFlagServlet";
}
