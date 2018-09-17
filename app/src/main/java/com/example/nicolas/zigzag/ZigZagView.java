package com.example.nicolas.zigzag;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nicolas on 25/10/2016.
 */

public class ZigZagView extends View {
    // non des preferences des préférences
    private final static String PREFS = "PREFS";
    // couleur par defaut
    public static int DEFAULT_PATHCOLOR = Color.parseColor("#76ccff");
    public static int DEFAULT_SIDECOLOR = Color.parseColor("#3f6ea2");
    public static int DEFAULT_BALLCOLOR = Color.parseColor("#222222");
    // couleur des éléments
    private static int FONDCOLOR = Color.parseColor("#dddddd");
    private static int SIDECOLOR;
    private static int PATHCOLOR;
    private static int BALLCOLOR;
    // score
    private int score = 0;
    // permet de savoir le sens du Zigzag et sont point de départ qui sera crée
    private int dernierEmplZigZag = 5;
    private int sensZigZag = 1;
    // sens de la balle (1 par defaut) soit 1 pour la droite, soit -1 pour la gauche
    private int sensBall = 1;
    // le thread
    protected GameThread gameThread;
    // file des zigzags
    private LinkedList<ZigZag> zigzags = new LinkedList<ZigZag>();
    // balle
    private Ball ball = new Ball();
    // vitesse actuel
    private int vitesse;
    // zigzag qui se trouve au niveau de la balle
    private int zigZagActuel = 1;
    // variable définissant la forme du zigzag
    private int hauteur3d;
    private int largeur;
    // tableaux définissant zigzags et leur effet 3D
    private Path[] path;
    private Path[] path3D;
    // paint permet de definir le style lors de l'affichage
    private Paint paint;



    public ZigZagView(Context context) {
        super(context);
        // recupération des préferences, et couleur par défaut sinon
        PATHCOLOR = context.getSharedPreferences(PREFS, 0).getInt("pathcolor", DEFAULT_PATHCOLOR);
        SIDECOLOR = context.getSharedPreferences(PREFS, 0).getInt("sidecolor", DEFAULT_SIDECOLOR);
        BALLCOLOR = context.getSharedPreferences(PREFS, 0).getInt("ballcolor", DEFAULT_BALLCOLOR);
        // création du thread
        gameThread = new GameThread(this);
        // mis en place du listener (commande de jeu)
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                   if (gameThread!=null) {
                       if (!gameThread.isRunning()) {
                           gameThread.setRunning(true);
                           gameThread.start();
                           return true;
                       }
                       score+=1;
                       // verifie le score a chauque fois qu'il est incrémenté
                       gestionNiveau();
                       if (getSensBall() == 1) {
                           setSensBall(-1);
                           return true;
                       }
                       if (getSensBall() == -1) {
                           setSensBall(1);
                           return true;
                       }
                       return false;
                   }
                }
                return false;
            }

        });
        Typeface font = Typeface.createFromAsset(context.getAssets(), "square.ttf");

        paint = new Paint();
        // defini le style, ici le style est plein (lors du dessin de forme et de polygon avec les PATH
        // et la font des textes
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(font);

    }

    // onDraw est appeler régulèrement par le thread et actualise la représentation du zigzag actuel
    @Override
    protected synchronized void onDraw(Canvas canvas) {

        //Initialisation + mise a jour du zigzag (enfilement + defilement + generation aléatoire)
        updateZigzag();

        // dessin du zigzag en parcourant la liste
        // 2 path distinct sont crée en fonction de la liste de zigzag.
        // Chaque zigzag ayant un postion, on peut le représenter en calculer les coordonnée de la forme de celui ci et de son effet 3D
        path = new Path[zigzags.size()];
        path3D = new Path[zigzags.size()];
        int i = 0;
        for (ZigZag zigzag : zigzags) {
            path3D[i] = new Path();
            path3D[i].moveTo(zigzag.getX(), zigzag.getY());
            path3D[i].lineTo(zigzag.getX() + largeur, zigzag.getY());
            if (zigzag.getSens() == 1) {
                path3D[i].lineTo(zigzag.getX() + zigzag.getSens() * zigzag.getHeight() + largeur, zigzag.getY() - zigzag.getHeight());
                path3D[i].lineTo(zigzag.getX() + zigzag.getSens() * zigzag.getHeight() + largeur, zigzag.getY() - zigzag.getHeight() + hauteur3d);
                path3D[i].lineTo(zigzag.getX() + largeur, zigzag.getY() + hauteur3d);
                path3D[i].lineTo(zigzag.getX(), zigzag.getY() + hauteur3d);

            } else {
                path3D[i].lineTo(zigzag.getX() + largeur, zigzag.getY() + hauteur3d);
                path3D[i].lineTo(zigzag.getX(), zigzag.getY() + hauteur3d);
                path3D[i].lineTo(zigzag.getX() + zigzag.getSens() * zigzag.getHeight(), zigzag.getY() - zigzag.getHeight() + hauteur3d);
                path3D[i].lineTo(zigzag.getX() + zigzag.getSens() * zigzag.getHeight(), zigzag.getY() - zigzag.getHeight());
            }
            path3D[i].moveTo(zigzag.getX(), zigzag.getY());
            i++;
        }

        i = 0;
        for (ZigZag zigzag : zigzags) {
            //Log.d("path "+i, "onDraw: "+zigzag);
            path[i] = new Path();
            path[i].moveTo(zigzag.getX(), zigzag.getY());
            path[i].lineTo(zigzag.getX() + zigzag.getSens() * zigzag.getHeight(), zigzag.getY() - zigzag.getHeight());
            path[i].lineTo(zigzag.getX() + zigzag.getSens() * zigzag.getHeight() + largeur, zigzag.getY() - zigzag.getHeight());
            path[i].lineTo(zigzag.getX() + largeur, zigzag.getY());
            path[i].lineTo(zigzag.getX(), zigzag.getY());
            i++;
        }

        canvas.drawColor(FONDCOLOR);
        // dessine chaque effet 3D de chaque zigzag avec la couleur correspondante
        paint.setColor(SIDECOLOR);
        for (i = 0; i < zigzags.size(); i++) {
            canvas.drawPath(path3D[i], paint);
        }
        // dessine chaque zigzag avec la couleur correspondante
        paint.setColor(PATHCOLOR);
        for (i = 0; i < zigzags.size(); i++) {
            canvas.drawPath(path[i], paint);
        }
        // dessine la Ball en fonction de ses coordonnée et de sa taille
        paint.setColor(BALLCOLOR);
        canvas.drawCircle(ball.getX(), ball.getY(), ball.getTaille(), paint);
        // affichage du scrore en haut a droite de l'écran
        paint.setColor(Color.parseColor("#222222"));
        canvas.drawText(String.valueOf(score),getWidth()/40,getHeight()/17, paint);

    }

    // appelée par le thread, cette méthode permet de déplacer vers le bas le chemin en deplacement chaque zigzag.
    // Elle supprime les zigzag descendu en dehors de l'écran et met a jour le zigzag a hauteur de la balle
    public synchronized void moveZigzagsToBottom() {
        for (ZigZag zigzag : zigzags)
            zigzag.setY(zigzag.getY() + vitesse);
        if (zigzags.size() < 2)
            return;
        if (zigzags.get(1).getY() > getHeight()) {
            zigzags.removeFirst();
            zigZagActuel--;
        }
    }
    //verifie si la balle se trouve bien sur le chemin et arrete le thread, simule un chute et déclare une défaite
    public void verifierBall(){
        // ajuste le zigzag actuel
        if(zigzags.get(zigZagActuel).getHeight()<=zigzags.get(zigZagActuel).getY()-ball.getY()) {
            zigZagActuel++;
        }
        // verifie la position de la balle par rapport au zigzag actuel et arrete le thread et simule une chute
        // puis déclare la défaite a l'activity
        if(ball.getX()-ball.getTaille()>zigzags.get(zigZagActuel).getX()+largeur+(zigzags.get(zigZagActuel).getY()-ball.getY())*zigzags.get(zigZagActuel).getSens() ||
                ball.getX()+ball.getTaille()<zigzags.get(zigZagActuel).getX()+(zigzags.get(zigZagActuel).getY()-ball.getY())*zigzags.get(zigZagActuel).getSens()){
            Log.d("perdu", "perdu");
            gameThread.setRunning(false);
            gameThread = null;
            while(ball.getY()-ball.getTaille()<getHeight()){
                chute();
            }
            AppZigZag app = (AppZigZag)getContext();
            app.lose(score);
        }
    }
    // Initialise le zigzag
    // Met a jour le zigzag en affilant aléatoirement des zigzag
    public void updateZigzag(){
        //Inititalisation
        if (zigzags.isEmpty()) {
            paint.setTextSize(getHeight()/17);
            vitesse = getWidth()/200;
            hauteur3d = getHeight() / 8;
            largeur = getWidth()/5;
            zigzags.add(new ZigZag(getWidth() / 2 - largeur/2, getHeight() / 4 * 3, 0, 1));
            ball.setX(getWidth() / 2);
            ball.setY(getHeight() / 4 * 3);
            ball.setTaille(getWidth() / 40);
            Log.d("vitesse :", String.valueOf(vitesse));
        }
        //maj du zigzag : crée une portion de zigzag avec une longueur aléatoire,
        // en mettant a jour les différent état(emplacement et sens)
        while (zigzags.getLast().getY() > 0) {
            int longueurDecalage;
            do {
                longueurDecalage = (int) (Math.random() * 6) + 1;
            }
            while (longueurDecalage * sensZigZag + dernierEmplZigZag < 0 || longueurDecalage * sensZigZag + dernierEmplZigZag > 10);
            dernierEmplZigZag += longueurDecalage * sensZigZag;
            ZigZag z = new ZigZag(zigzags.getLast().getX() + (zigzags.getLast().getHeight() * zigzags.getLast().getSens()), zigzags.getLast().getY() - zigzags.getLast().getHeight(), (longueurDecalage * ((getWidth() - largeur) / 10)), sensZigZag);
            zigzags.addLast(z);
            this.sensZigZag = -this.sensZigZag;
            Log.d("zigzag " + zigzags.size(), z.toString());
        }

    }
    // Appelée par le thread, cette méthode fait bouger la balle dans le bon sens
    public void moveBall() {
        ball.setX(ball.getX() + (sensBall * vitesse));
    }
    // Simile la chute de la balle lors d'une défaite
    public void chute(){
        moveBall();
        ball.setY(ball.getY()+getHeight()/300);
        try {
            Thread.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        postInvalidate();
    }
    // modifie la vitesse en fontion du score
    public void gestionNiveau(){
        switch (score){
            case 10 :
                vitesse +=1;
                break;
            case 25 :
                vitesse +=1;
                break;
            case 40 :
                vitesse +=1;
                break;
            case 70 :
                vitesse +=1;
                break;
            case 110 :
                vitesse +=1;
                break;
            default:
                break;
        }
    }

    public int getSensBall() {
        return sensBall;
    }

    public void setSensBall(int sensBall) {
        this.sensBall = sensBall;
    }
    public int getVitesse() {
        return vitesse;
    }

    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }


}