package marc.scp.constants;

public class Constants
{
    public final static String LOG_PREFIX = "DROID_SSH.";

    public final static String PREFERENCE_PARCEABLE = "com.whomarc.scp.PREFERENCE";
    public final static String FILE_PARCEABLE = "com.whomarc.scp.FILE";

    // terminal constants
    public final static String RIGHT = "\033[C";
    public final static String LEFT = "\033[D";
    public final static String UP = "\033[A";
    public final static String DOWN = "\033[B" ;
    public final static String TAB = "\011";
    public final static String ESC = "\033";

    //ColorScheme constants
    public static final int WHITE = 0xffffffff;
    public static final int BLACK = 0xff000000;
    public static final int BLUE = 0xff344ebd;
    public static final int GREEN = 0xff00ff00;
    public static final int AMBER = 0xffffb651;
    public static final int RED = 0xffff0113;
    public static final int HOLO_BLUE = 0xff33b5e5;

    // foreground index, foreground color, background index, background color
    public static final int[][] COLOR_SCHEMES = {
            {BLACK, WHITE},
            {WHITE, BLACK},
            {WHITE, BLUE},
            {GREEN, BLACK},
            {AMBER, BLACK},
            {RED, BLACK},
            {HOLO_BLUE, BLACK}
    };

}

