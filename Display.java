
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Vector;

/**
 * The type Display.
 */
public class Display extends Canvas implements Runnable {
    /**
     * The Thread.
     */
    private Thread thread;
    /**
     * The Frame.
     */
    private JFrame frame;
    /**
     * The constant running.
     */
    private static boolean running = false;

    /**
     * Instantiates a new Display.
     *
     * @param width  the width
     * @param height the height
     * @param title  the title
     */
    public Display(int width, int height, String title) {
        this.frame = new JFrame();
        Dimension size = new Dimension(width, height);
        this.setPreferredSize(size);
        this.frame.setTitle(title);
        this.frame.add(this);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        running = true;
    }

    /**
     * Start.
     */
    public synchronized void start() {
        running = true;
        this.thread = new Thread(this, "render.Display");
        this.thread.start();
    }

    /**
     * Stop.
     */
    public synchronized void stop() {
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run() Thread#run()Thread#run()Thread#run()Thread#run()
     */
    @Override
    public void run() {
        long MS_PF = 1000 / HilbertCurve.FPS;
        int N = (int) Math.pow(2, HilbertCurve.ORDER);
        Vector[] path = HilbertCurve.getPath(N);
        Color[] colors = HilbertCurve.getRNGColors(N);
        while (running) {
            long startTime = System.currentTimeMillis();
            render(colors, path);
            update(path.length);
            long end = System.currentTimeMillis();
            long work = end - startTime;
            if (MS_PF > work) {
                try {
                    Thread.sleep(MS_PF - work);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        stop();
    }

    /**
     * Update.
     *
     * @param length
     */
    private void update(int length) {
        HilbertCurve.COUNTER += HilbertCurve.VELOCITY;
        if (HilbertCurve.COUNTER >= length) {
            if (HilbertCurve.RESTART_END) {
                HilbertCurve.COUNTER = 0;
            } else {
                HilbertCurve.COUNTER = length;
            }
        }
    }


    /**
     * Render.
     */
    private void render(Color[] colors, Vector[] path) {
        BufferStrategy bs = this.getBufferStrategy();
        int len = (int) (HilbertCurve.WIDTH / Math.sqrt(path.length));
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, HilbertCurve.WIDTH, HilbertCurve.HEIGHT);
        if (HilbertCurve.LINES_MODE) {
            g.setColor(Color.BLACK);
            for (int i = 1; i < HilbertCurve.COUNTER; i++) {
                g.drawLine((int) path[i].get(0)+len/2, (int) path[i].get(1)+len/2, (int) path[i - 1].get(0)+len/2,
                           (int) path[i - 1].get(1)+len/2
                          );
            }
        } else {
            for (int i = 0; i < HilbertCurve.COUNTER; i++) {
                g.setColor(colors[(i / colors.length) % colors.length]);
                g.fillRect((int) path[i].get(0), (int) path[i].get(1), (len), (len));

            }
        }
        g.dispose();
        bs.show();
    }


}
