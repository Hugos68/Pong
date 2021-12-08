public class Ball {

    int X;
    int Y;
    int W;
    int H;
    int xVelocity;
    int yVelocity;

    Ball() {
        X = 1920/2-50;
        Y = 1080/2-50;
        W = 100;
        H = 100;
        xVelocity = Math.random() > 0.5 ? 14 : -14;
        yVelocity = 0;
    }

}
