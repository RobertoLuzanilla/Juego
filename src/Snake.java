import javax.swing.*;
import java.awt.*;
import java.awt.event.*;//Para el uso de las teclas
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public  class Snake extends JFrame {
    //Estas son variables de la ventana y su tamaño
    int widht =640, height = 480;
    Boolean gameOver= false;
    long frecuencia = 40;
    ArrayList<Point> lista = new ArrayList<Point>();
    int direccion = KeyEvent.VK_LEFT , currentDirection;
    int widhtPoint= 15, heightPoint = 15;
    ImagenSnake imagenSnake = new ImagenSnake();
    Point snake; //Esto es una variable
    Point comida;
    //ESTE CONSTRUCTOR ES LA VENTANA
    public Snake(){
        setTitle("Snake");
        setSize(widht, height);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//Toma las dimensiones de la ventana
        //this.setLocation(dim.width/2-widht/2,dim.height/2-height/2);  Esto hace que la ventana este en medio de la pantalla
        setLocationRelativeTo(null); //Pero para eso tenemos este metodo tambien
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Aqui importo la clase para cuando le oprima ESC se salga
        teclas Teclas = new teclas();
        this.addKeyListener(Teclas);
        starGame();
        //Esto es para que aparezca en medio de la pantalla, el setRelative no me funciono
        snake =  new Point(widht/2,height/2);

        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();

    }

    public void crearComida(){
        Random rnd = new Random();

        comida.x  = rnd.nextInt(widht);
        if ((comida.x % 5)>0){
            comida.x = comida.x - (comida.x % 5);
        }
        if (comida.x <5){
            comida.x=comida.x+10;
        }
        comida.y  = rnd.nextInt(height);
        if ((comida.y % 5)>0){
            comida.y = comida.y - (comida.y % 5);
        }
        if (comida.y <5){
            comida.y=comida.y+10;
        }
    }
    public void starGame(){
        gameOver=false;
    comida = new Point(200,200);
        snake =  new Point(widht/2,height/2);
        lista = new ArrayList<Point>();
        lista.add(snake);
        crearComida();
    }

    public static void main(String[] args) {
        Snake s = new Snake();
    }


    public class ImagenSnake extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            //Se le asginga color a los objetos de la pantalla
            g.setColor(new Color(0, 0, 255));
            g.fillRect(snake.x, snake.y, widhtPoint, heightPoint);
            for (int i=0;i<lista.size();i++){
                Point p = (Point)lista.get(i);
                g.fillRect(p.x, p.y, widhtPoint, heightPoint);

            }
            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x, comida.y, widhtPoint, heightPoint);

            if (gameOver){
                g.drawString("GAME OVER" , 200, 320);
            }
        }
    }

 
    //Esta clase es para el uso de las teclas
    public class teclas extends KeyAdapter{
        
    public void keyPressed(KeyEvent e){
        //Este if es para cuando oprima ESC, salga de la ventana
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
        System.exit(0);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (e.getKeyCode() != KeyEvent.VK_DOWN){
                    direccion = KeyEvent.VK_UP;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        if (e.getKeyCode() != KeyEvent.VK_UP){
            direccion = KeyEvent.VK_DOWN;
        }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT){
        if (e.getKeyCode() != KeyEvent.VK_RIGHT){
            direccion = KeyEvent.VK_LEFT;
        }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        if (e.getKeyCode() != KeyEvent.VK_LEFT){
            direccion = KeyEvent.VK_RIGHT;
        }
            }
    }
    }

    public class Momento extends Thread{
        long last = 0;

        public void run(){
            while (true){
                if ((System.currentTimeMillis() - last) > frecuencia) {
                    if (gameOver) {
                        if (direccion == KeyEvent.VK_UP && currentDirection != KeyEvent.VK_DOWN) {
                            // Mover hacia arriba
                            snake.y = snake.y - heightPoint;
                            if (snake.y > height) {
                                snake.y = 0;
                            }
                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }
                            currentDirection = direccion;
                        } else if (direccion == KeyEvent.VK_DOWN && currentDirection != KeyEvent.VK_UP) {
                            // Mover hacia abajo
                            snake.y = snake.y + heightPoint;
                            if (snake.y > height) {
                                snake.y = 0;
                            }
                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }
                            currentDirection = direccion;
                        } else if (direccion == KeyEvent.VK_LEFT && currentDirection != KeyEvent.VK_RIGHT) {
                            // Mover hacia la izquierda
                            snake.x = snake.x - widhtPoint;
                            if (snake.x > widht) {
                                snake.x = 0;
                            }
                            if (snake.x < 0) {
                                snake.x = widht - widhtPoint;
                            }
                            currentDirection = direccion;
                        } else if (direccion == KeyEvent.VK_RIGHT && currentDirection != KeyEvent.VK_LEFT) {
                            // Mover hacia la derecha
                            snake.x = snake.x + widhtPoint;
                            if (snake.x > widht) {
                                snake.x = 0;
                            }
                            if (snake.x < 0) {
                                snake.x = widht - widhtPoint;
                            }
                            currentDirection = direccion;
                        }

                    }
                    actualizar();
                last = System.currentTimeMillis();
                }
            }
        }
    }

    private void actualizar() {
        imagenSnake.repaint();
        lista.add(0,new Point(snake.x,snake.y));
        lista.remove((lista.size()-1));

        for (int i = 0; i <lista.size(); i++) {
            Point punto = lista.get(i);
            if (snake.x==punto.x && snake.y==punto.y){
            gameOver = true;
            break;
            }
        }
        if (snake.x > (comida.x -10)&&(snake.x > (comida.x +10)) && snake.y > (comida.y -10)&&(snake.y > (comida.y +10))){
            lista.add(0,new Point(snake.x,snake.y));
            crearComida();
        }
    }
}