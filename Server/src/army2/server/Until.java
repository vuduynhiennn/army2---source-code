package army2.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Văn Tú
 */
public class Until {

    private static final Random rand;
    private static final SimpleDateFormat dateFormat;
    private static final short sinData[];
    private static final short cosData[];
    private static final int tanData[];

    static {
        rand = new Random();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sinData = (new short[]{0, 18, 36, 54, 71, 89, 107, 125, 143, 160, 178, 195, 213, 230, 248, 265, 282, 299, 316, 333, 350, 367, 384, 400, 416, 433, 449, 465, 481, 496, 512, 527, 543, 558, 573, 587, 602, 616, 630, 644, 658, 672, 685, 698, 711, 724, 737, 749, 761, 773, 784, 796, 807, 818, 828, 839, 849, 859, 868, 878, 887, 896, 904, 912, 920, 928, 935, 943, 949, 956, 962, 968, 974, 979, 984, 989, 994, 998, 1002, 1005, 1008, 1011, 1014, 1016, 1018, 1020, 1022, 1023, 1023, 1024, 1024});
        cosData = new short[91];
        tanData = new int[91];
        for (int i = 0; i <= 90; i++) {
            cosData[i] = sinData[90 - i];
            if (cosData[i] == 0) {
                tanData[i] = 0x7fffffff;
            } else {
                tanData[i] = (sinData[i] << 10) / cosData[i];
            }
        }
    }

    public static final int sin(int arg) {
        if ((arg = toArg0_360(arg)) >= 0 && arg < 90) {
            return sinData[arg];
        }
        if (arg >= 90 && arg < 180) {
            return sinData[180 - arg];
        }
        if (arg >= 180 && arg < 270) {
            return -sinData[arg - 180];
        } else {
            return -sinData[360 - arg];
        }
    }

    public static final int cos(int arg) {
        if ((arg = toArg0_360(arg)) >= 0 && arg < 90) {
            return cosData[arg];
        }
        if (arg >= 90 && arg < 180) {
            return -cosData[180 - arg];
        }
        if (arg >= 180 && arg < 270) {
            return -cosData[arg - 180];
        } else {
            return cosData[360 - arg];
        }
    }

    public static final int getArg(int cos, int sin) {
        if (cos == 0) {
            return sin == 0 ? 0 : (sin < 0 ? 270 : 90);
        }
        int arg;
        label2:
        {
            arg = Math.abs((sin << 10) / cos);
            for (int i = 0; i <= 90; i++) {
                if (tanData[i] < arg) {
                    continue;
                }
                arg = i;
                break label2;
            }
            arg = 0;
        }
        if (sin >= 0 && cos < 0) {
            arg = 180 - arg;
        }
        if (sin < 0 && cos < 0) {
            arg += 180;
        }
        if (sin < 0 && cos >= 0) {
            arg = 360 - arg;
        }
        return arg;
    }

    public static final int toArg0_360(int arg) {
        if (arg >= 360) {
            arg -= 360;
        }
        if (arg < 0) {
            arg += 360;
        }
        return arg;
    }

    public static int getSqrt(int num) {
        if (num <= 0) {
            return 0;
        }
        int newS = (num + 1) / 2;
        int oddS;
        do {
            oddS = newS;
            newS = newS / 2 + num / (newS * 2);
        } while (Math.abs(oddS - newS) > 1);
        return newS;
    }

    public static int nextInt(int x1, int x2) {
        int to = x2;
        int from = x1;
        if (x2 > x1) {
            to = x2;
            from = x1;
        }
        return from + rand.nextInt((to + 1) - from);
    }

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static int nextInt(int[] percen) {
        int next = nextInt(1000), i;
        for (i = 0; i < percen.length; i++) {
            if (next < percen[i]) {
                return i;
            }
            next -= percen[i];
        }
        return i;
    }

    public static Date getDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toDateString(Date date) {
        return dateFormat.format(date);
    }

    public static void addNumDay(Date dat, int nDays) {
        dat.setTime(dat.getTime() + nDays * 86400000L);
    }

    public static int getNumDay(Date from, Date to) {
        return (int) ((to.getTime() - from.getTime()) / 1000 / 86400);
    }

    public static String arrayToString(byte[] bArr) {
        return Arrays.toString(bArr);
    }
    
    public static String arrayToString(int[] iArr) {
        return Arrays.toString(iArr);
    }
    
    public static String arrayToString(short[] sArr) {
        return Arrays.toString(sArr);
    }
    
    public static String arrayToString(boolean[] zArr) {
        return Arrays.toString(zArr);
    }
    
    public static String arrayToString(long[] lArr) {
        return Arrays.toString(lArr);
    }
    
    public static String arrayToString(String[] Arr) {
        return Arrays.toString(Arr);
    }
    
    public static String arrayToString(Object[] objArr) {
        return Arrays.toString(objArr);
    }

    public static String getStringNumber(float num) {
        NumberFormat numf = NumberFormat.getNumberInstance();
        numf.setMaximumFractionDigits(2);
        numf.setMinimumFractionDigits(0);
        numf.setRoundingMode(RoundingMode.DOWN);
        if (num >= 1000000000) {
            return numf.format((num / 1000000000)) + "b";
        } else if (num >= 1000000) {
            return numf.format((num / 1000000)) + "m";
        } else if (num >= 1000) {
            return numf.format((num / 1000)) + "k";
        } else {
            return String.valueOf((int) num);
        }
    }

    public static String getStrungTime(long s) {
        s = Math.abs(s) / 1000L;
        if (s >= 31104000) {
            return (s / 31104000) + " năm";
        } else if (s >= 2592000) {
            return (s / 2592000) + " tháng";
        } else if (s >= 604800) {
            return (s / 604800) + " tuần";
        } else if (s >= 86400) {
            return (s / 86400) + " ngày";
        } else if (s >= 3600) {
            return (s / 3600) + " giờ";
        } else if (s >= 60) {
            return (s / 60) + " phút";
        } else {
            return String.valueOf(s) + " giây";
        }
    }

    public static short getShort(byte[] ab, int off) {
        return (short) ((ab[off] & 0xff) << 8 | ab[off + 1] & 0xff);
    }

    public static boolean inRegion(int x, int y, int x0, int y0, int w, int h) {
        return x >= x0 && x < x0 + w && y >= y0 && y < y0 + h;
    }

    public static boolean intersecRegions(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        //return x1 + w1 >= x2 && x1 <= x2 + w2 && y1 + h1 >= y2 && y1 <= y2 + h2;
        return Math.abs(x1 - x2) <= w2 + w1 && Math.abs(y1 - y2) <= h2 + h1;
    }

    public static boolean isNotAlpha(int rgb) {
        return (rgb >> 24) != 0;
    }

    public static int getTeamPoint(int TongDD, int nteam) {
        if (nteam == 1) {
            return 0;
        }
        return (TongDD - 100) / 100 + (TongDD - 100) * nteam / 1000;
    }

    public static boolean compare_Day(Date dnew, Date dold) throws ParseException {
        SimpleDateFormat FM = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = FM.parse(FM.format(dnew));
        Date date2 = FM.parse(FM.format(dold));
        if (date1.equals(date2)) {
            return false;
        } else {
            return !date1.before(date2);
        }
    }

    public static float getArgXY(float Ax, float Ay, float Bx, float By) {
        float K = Math.abs(Ay - By);
        float D = Math.abs(Ax - Bx);
        float tan = (D / K);
        float IntArg = (float) (Math.toDegrees(Math.atan(tan)));
        if (Ax >= Bx && Ay > By) {
            IntArg += 90;
        } else if (Ax > Bx && Ay <= By) {
            IntArg = (270 - IntArg);
        } else if (Ax <= Bx && Ay < By) {
            IntArg -= 90;
        } else if (Ax < Bx && Ay >= By) {
            IntArg = (270 + IntArg);
        }
        return IntArg;
    }

    public static String addNumHours(Date dat, int hours) {
        dat.setTime(dat.getTime() + hours * 3600000L);
        return dateFormat.format(dat);
    }

    public static byte[] getFile(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            byte[] ab = new byte[fis.available()];
            fis.read(ab, 0, ab.length);
            fis.close();
            return ab;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveFile(String url, byte[] ab) {
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(url);
            fos.write(ab);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
