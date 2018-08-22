package b.team.works.u22.hal.u22teamb;

/**
 * 定数を集めたクラス。
 *
 * @author Taiga Hirai
 */
public class Word {
    /**
     * サーバーのURL
     */
    private static final String SERVER_URL = "http://59.106.219.240:8080/u22_team_b_web/";
//    private static final String SERVER_URL = "http://192.168.1.101:8080/U22TeamB/";//平井モバイルルータアドレス
//    private static final String SERVER_URL = "http://10.0.2.2:8080/u22_team_b_web/";//localhostアドレス
    public static final String STORE_MAP_URL = SERVER_URL + "StoreListServlet";
    public static final String STORE_DETAILS_URL = SERVER_URL + "StoreDetailsServlet";
    public static final String USER_MYPAGE_URL = SERVER_URL + "MypageJsonTestServlet";
    public static final String RESERVATION_LIST_URL = SERVER_URL + "ReservationListJsonServlet";
    public static final String RESERVATION_DATA_URL = SERVER_URL + "ReservationDataJsonServlet";
    public static final String HISTORY_LIST_URL = SERVER_URL + "HistoryListJsonServlet";
    public static final String RESERVATION_URL = SERVER_URL + "ReservationInsert";
    public static final String USER_LOGIN_URL = SERVER_URL + "UserLoginJsonServlet";
    public static final String USER_LAT_LNG_URL = SERVER_URL + "UserLatLngServlet";
    public static final String USER__URL = SERVER_URL + "RegistrationConfirmationServlet";
}
