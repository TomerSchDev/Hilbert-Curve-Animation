
import java.awt.Color;
import java.util.Random;
import java.util.Vector;

public class HilbertCurve {
    public static boolean LINES_MODE ;
    public static boolean RESTART_END ;
    public static int HEIGHT;
    public static int WIDTH;
    public static int ORDER;
    public static int VELOCITY;
    public static int FPS;
    public static int COUNTER;

    public static void setSetting() {
        setGUISetting();
        ORDER = 5;
        VELOCITY = 1;
        COUNTER=1;
        LINES_MODE=true;
        RESTART_END=true;
    }

    private static void setGUISetting() {
        HEIGHT = 512;
        WIDTH = 512;
        FPS = 30;
    }

    public static Vector[] getPath(int N) {
        int total = N * N;
        Vector path[] = new Vector[total];
        for (int i = 0; i < total; i++) {
            Vector b = calculateHilbertCurve(i);
            path[i] = new Vector();
            path[i].add(((int) b.get(0) * WIDTH / N));
            path[i].add(((int) b.get(1) * HEIGHT / N));
        }
        return path;
    }

    public static Color[] getRNGColors(int N) {
        Random random = new Random();
        Color colors[] = new Color[random.nextInt(N)+1];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color(random.nextInt(230), random.nextInt(230), random.nextInt(230));
        }
        return colors;
    }

    public static void main(String[] args) {
        setSetting();
        Display display = new Display(WIDTH, HEIGHT, "Hilbert Curve");
        display.run();
    }

    public static Vector calculateHilbertCurve(int i) {
        Vector[] points = {
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector()
        };
        points[0].add(0);
        points[0].add(0);
        //---
        points[1].add(0);
        points[1].add(1);
        //---
        points[2].add(1);
        points[2].add(1);
        //---
        points[3].add(1);
        points[3].add(0);
        int index = i & 3;
        Vector v = points[index];
        for (int j = 1; j < ORDER; j++) {
            i = i >>> 2;
            index = i & 3;
            int len = (int) Math.pow(2, j);
            Vector b = new Vector();
            switch (index) {
                case 0:
                    b.add(v.get(1));
                    b.add(v.get(0));
                    break;
                case 1:
                    b.add(v.get(0));
                    b.add((int) v.get(1) + len);
                    break;
                case 2:
                    b.add((int) v.get(0) + len);
                    b.add((int) v.get(1) + len);
                    break;
                case 3:
                    b.add(2 * len - 1 - (int) v.get(1));
                    b.add(len - 1 - (int) v.get(0));
                    break;
            }
            v = b;
        }
        return v;

    }


}
