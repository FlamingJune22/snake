import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static int DELAY;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    boolean text = true;
    Timer timer;
    Random random;
    JButton easy;
    JButton medium;
    JButton hard;

    GamePanel() {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        easy = new JButton("Легко");
        medium = new JButton("Средне");
        hard = new JButton("Сложно");
        this.add(easy);
        this.add(medium);
        this.add(hard);
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DELAY = 140;
                text = false;
                running = true;
                startGame();
                easy.hide();
                medium.hide();
                hard.hide();
            }
        });
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DELAY = 100;
                text = false;
                running = true;
                startGame();
                easy.hide();
                medium.hide();
                hard.hide();
            }
        });
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DELAY = 60;
                text = false;
                running = true;
                startGame();
                easy.hide();
                medium.hide();
                hard.hide();
            }
        });
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
        direction = 'R';
        bodyParts = 6;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Segoe Print", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Счет: " +applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Счет: " +applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            if (DELAY == 140) {
                applesEaten += 3;
            } else if (DELAY == 100) {
                applesEaten += 2;
            } else applesEaten += 1;
            newApple();
        }
    }

    public void checkCollisions() {
        // проверка на столкновение головы змейки с телом
        for (int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i] ) && (y[0] == y[i])) {
                running = false;
            }
        }
        // проверка на столкновение с левой границей
        if (x[0] < 0) {
            running = false;
        }
        // проверка на столкновение с правой границей
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // проверка на столкновение с верхней границей
        if (y[0] < 0) {
            running = false;
        }
        // проверка на столкновение с нижней границей
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Segoe Print", Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Счет: " +applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Счет: " +applesEaten))/2, g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font("Segoe Print", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Игра закончена", (SCREEN_WIDTH - metrics2.stringWidth("Игра закончена"))/2, SCREEN_HEIGHT/4);
        g.setColor(Color.red);
        g.setFont(new Font("Segoe Print", Font.BOLD, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Нажмите пробел для продолжения", (SCREEN_WIDTH - metrics3.stringWidth("Нажмите пробел для продолжения"))/2, SCREEN_HEIGHT/3);
        g.setColor(Color.red);
        g.setFont(new Font("Segoe Print", Font.BOLD, 30));
        FontMetrics metrics4= getFontMetrics(g.getFont());
        g.drawString("или выберите сложность", (SCREEN_WIDTH - metrics4.stringWidth("или выберите сложность"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        } repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
            if(!running&&!text) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startGame();
                    for (int i = bodyParts; i > 0; i--) {
                        x[i] = bodyParts * -1;
                        y[i] = 0;
                    }
                    x[0] = 0;
                    y[0] = 0;
                    repaint();
                    applesEaten = 0;
                }
            }
        }
    }
}
