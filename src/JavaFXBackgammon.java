import java.util.Random;
import javafx.scene.image.Image ;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JavaFXBackgammon extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        //-------------------STATEMENTS-------------------------------------
        //-------------------STATEMENTS-------------------------------------
        
        Pane pane=new Pane();
        
        FirstLayer first = new FirstLayer(primaryStage);
        
        GridPane[] gridCols = new GridPane[30];

        Pawns pawn = new Pawns();
        
        int[] blueUp = new int[12];
        int[] blueDown = new int[12];
        int[] blackUp = new int[12];
        int[] blackDown = new int[12];

        //-------------------STATEMENTS-------------------------------------
        //-------------------STATEMENTS-------------------------------------
        
        pane.getChildren().addAll(first.getBoard());

        for(int i=0; i<12; i++){
        
            SecondLayer up = new SecondLayer(); 

            gridCols[i]= new GridPane();

            gridCols[i]=up.setLayoutUp(i);
        
            pane.getChildren().add(gridCols[i]);

            //gridCols[i].setGridLinesVisible(true);

        }
        
        for(int i=12; i<24; i++){

            SecondLayer down = new SecondLayer(); 

            gridCols[i]= new GridPane();

            gridCols[i]=down.setLayoutDown(i);
     
            pane.getChildren().add(gridCols[i]);

            //gridCols[i].setGridLinesVisible(true);

        }

        GamePlay theGame = new GamePlay(gridCols,primaryStage);

        //************************************
        //************************************
        
        Button dices = new Button();
        
        boolean blue=true;
        
        boolean endOfGame=false;
        
        Label one = new Label("?");
        Label two = new Label("?");

        
        dices.setText("Blue");
        
            dices.setOnAction(new EventHandler<ActionEvent>() {

                boolean player=true;

                @Override
                public void handle(ActionEvent event) {

                    theGame.setTimes(2);   
                    
                    Random rand = new Random();                    
                    
                    int  diceOne = rand.nextInt(6) + 1;
                    int  diceTwo = rand.nextInt(6) + 1;
                    
                    one.setFont(Font.font(null, FontWeight.BOLD, 72));
                    two.setFont(Font.font(null, FontWeight.BOLD, 72));
                    
                    one.setText(String.valueOf(diceOne));
                    one.setLayoutX(528);one.setLayoutY(360);
                    one.setStyle("-fx-text-fill: white;");
                    two.setText(String.valueOf(diceTwo));
                    two.setLayoutX(528);
                    two.setLayoutY(290);
                    two.setStyle("-fx-text-fill: white;");

                    if(player){

                        if(diceOne==diceTwo){ theGame.setTimes(4);}
                        player=false;
                        dices.setText("Black");
                        theGame.reset();
                        theGame.bluePlays(gridCols,diceOne,diceTwo);

                    }else{

                        if(diceOne==diceTwo){ theGame.setTimes(4);}
                        player=true;
                        dices.setText("Blue");
                        theGame.reset();
                        theGame.blackPlays(gridCols,diceOne,diceTwo);
                    }

                }
            });

        //*****************************************************************************
        //*****************************************************************************
        
        pane.getChildren().addAll(dices,one,two);
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        Scene scene = new Scene(pane, screenBounds.getWidth(), screenBounds.getHeight());

        primaryStage.setTitle("Backgammon");
        
        primaryStage.setScene(scene);
        
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1100);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1100);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);   
    }

    
class SecondLayer{

    private final GridPane gridUp = new GridPane();
    
    private final int rows = 6;
    private final int columns = 1;
    
    private int heightC=51;
    private int heightR=50;
    
    private final double[] cols = {
        48,
        128,
        212,
        289,
        375,
        455,
        593,
        675,
        757,
        838,
        922,
        1006,   
        1006,
        922,
        838,
        757,
        675,
        593,
        455,
        375,
        289,
        212,
        128,
        48,
    };
    
    SecondLayer(){

            for(int i = 0; i < columns; i++) {
            
            ColumnConstraints column = new ColumnConstraints(heightC);
            gridUp.getColumnConstraints().add(column);

            }

            for(int i = 0; i < rows; i++) {
            
            RowConstraints row = new RowConstraints(heightR);
            gridUp.getRowConstraints().add(row);

            }

            
            
    }
    
    public GridPane setLayoutUp(int a){

            gridUp.setLayoutX(cols[a]);
            gridUp.setLayoutY(26);
            
        return gridUp;
    }
    
    public GridPane setLayoutDown(int a){

            gridUp.setLayoutX(cols[a]);
            gridUp.setLayoutY(428);

        return gridUp;
    }
    
    public void setHeightR(int value){ heightR=value;}
    public int getHeightR(){ return heightR;}

}

class FirstLayer{

    private final Image img = new Image("/backgammonBoard.png");
    private final ImageView board = new ImageView(img);

    FirstLayer(Stage primaryStage){

        board.fitWidthProperty().bind(primaryStage.widthProperty());
        board.fitHeightProperty().bind(primaryStage.heightProperty());

        board.setPreserveRatio(true);

    }
        
    public ImageView getBoard(){
    
    return board;
    
    }
}

class Pawns{

private double radius=24.0f;
private boolean color;
    
private Node aPawn(boolean white) {
    
        Circle w = new Circle();
        w.setCenterX(100.0f);
        w.setCenterY(100.0f);
        w.setRadius(radius);
        w.setFill(Color.BLUE);
        
        Circle b = new Circle();
        b.setCenterX(100.0f);
        b.setCenterY(100.0f);
        b.setRadius(radius);
        b.setFill(Color.BLACK);

        if(white)
            return w;

        return b;
    }

Pawns(){}

public Node createPawn(boolean color){

    this.color=color;
    return aPawn(color);

}

public void setRadius(double value){

    radius=value;

}
public double getRadius(){return radius;}

public Node getPawn(){
    
return aPawn(color);
}

}

class GamePlay extends Pawns{
    
    private Stage b;
    
    private final Pawns pawn = new Pawns();

    private final int[] blueUp = new int[30];
    private final int[] blueDown = new int[30];
    private final int[] blackUp = new int[30];
    private final int[] blackDown = new int[30];
    
    private int outBlue=0;
    private int outBlack=0;
    
    private int times=2;

    private int countDice=1;
    
    private int[] sevenNum=new int[30];
    
    GamePlay(GridPane[] setP,Stage a){
        
        b=a;
        
        for(int i=0;i<sevenNum.length;i++)
        {
           sevenNum[i]= 0;   
        }    
        for(int i=0;i<blueUp.length;i++)
        {
           blueUp[i]= 0;   
        }
        for(int i=0;i<blackUp.length;i++)
        {
           blackUp[i]= 0;   
        }
        for(int i=0;i<blueDown.length;i++)
        {
           blueDown[i]= 0;   
        }
        for(int i=0;i<blackDown.length;i++)
        {
           blackDown[i]= 0;   
        }

        //for blue pawns
        setDown(setP,18,1,true);setDown(setP,18,1,true);
        setDown(setP,18,1,true);setDown(setP,18,1,true);setDown(setP,18,1,true);
        
        setDown(setP,16,1,true);setDown(setP,16,1,true);setDown(setP,16,1,true);
        
        setUp(setP,11,1,true);setUp(setP,11,1,true);setUp(setP,11,1,true);
        setUp(setP,11,1,true);setUp(setP,11,1,true);
        
        setUp(setP,0,1,true);setUp(setP,0,1,true);
        
        //for black pawns
        setDown(setP,23,1,false);setDown(setP,23,1,false);
        
        setDown(setP,12,1,false);setDown(setP,12,1,false);
        setDown(setP,12,1,false);setDown(setP,12,1,false);setDown(setP,12,1,false);
        
        setUp(setP,7,1,false);setUp(setP,7,1,false);setUp(setP,7,1,false);
        
        setUp(setP,5,1,false);setUp(setP,5,1,false);setUp(setP,5,1,false);
        setUp(setP,5,1,false);setUp(setP,5,1,false);
       

    }

    public void setUp(GridPane[] col,int column,int howMany,boolean color){
        
        int[] oneBlue=oneBlue();
        int[] oneBlack=oneBlack();

        int contains=howManyContains(col,column);
        
        if(contains>5){ sevenNum[column]++; }
        
        if(sevenNum[column]>0){

        Label title = new Label(Integer.toString(sevenNum[column]+6));
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-text-fill: white;"+
                       "-fx-font-family: \"Times New Roman\";" +
                       "-fx-font-style: italic;" +
                       "-fx-font-size: 45px;"
                       );

        col[column].add(pawn.createPawn(color), 0,5);
        col[column].add(title,0,5);
        
        if(color){  blueUp[column]+=1;  }
        
        else{   blackUp[column]+=1; }
        
        }else{
        
            if(color){
                
                    if( (oneBlack[column]==1)){
                            removeUp(col,column,false);
                            col[column].add(pawn.createPawn(color), 0, contains-1);
                            outBlack++;
                    }else{
   
                    col[column].add(pawn.createPawn(color), 0, contains);
                    
                    }

                    blueUp[column]+=1;
            
            }else{

                    if( (oneBlue[column]==1)){

                        removeUp(col,column,true);
                        col[column].add(pawn.createPawn(color), 0, contains-1);
                        outBlue++;
                    
                    }else{

                        col[column].add(pawn.createPawn(color), 0, contains);
                    }

                   blackUp[column]+=1;
                }
        }
    }
    
    public void setDown(GridPane[] col,int colNumber,int howMany,boolean color){
    
        int[] oneBlue=oneBlue();
        int[] oneBlack=oneBlack();
        int contains = howManyContains(col,colNumber);

        int put=5-contains;

        if(contains>5){ sevenNum[colNumber]++;  }
        
        if(sevenNum[colNumber]>0){

            Label title = new Label(Integer.toString(sevenNum[colNumber]+6));
            title.setMaxWidth(Double.MAX_VALUE);
            title.setAlignment(Pos.CENTER);
            title.setStyle("-fx-text-fill: white;"+
                           "-fx-font-family: \"Times New Roman\";" +
                           "-fx-font-style: italic;" +
                           "-fx-font-size: 45px;"
                           );

            col[colNumber].add(pawn.createPawn(color), 0,0);
            col[colNumber].add(title,0,0);
        
            if(color){  blueDown[colNumber]+=1;   }
            else{   blackDown[colNumber]+=1;    }
        
        }else{
            
            if(color){

                    if( (oneBlack[colNumber]==1)){
                        removeDown(col,colNumber,false);
                        col[colNumber].add(pawn.createPawn(color), 0, put+1);
                        outBlack++;
                    }else{

                    col[colNumber].add(pawn.createPawn(color), 0, put);
                    }
                                        
                    blueDown[colNumber]+=1;
            
            }else{

                    if( (oneBlue[colNumber]==1)){

                        removeDown(col,colNumber,true);
                        col[colNumber].add(pawn.createPawn(color), 0, put+1);
                        outBlue++;
                    
                    }else{
                    
                        col[colNumber].add(pawn.createPawn(color), 0, put);
                    
                    }
                    
                    blackDown[colNumber]+=1;
            }
        }//if-else
    }

    public void removeUp(GridPane[] col,int colNumber,boolean color){
        
        if(sevenNum[colNumber]>0){
            
        sevenNum[colNumber]-=1;
        
        Label title = new Label(Integer.toString(sevenNum[colNumber]+6));
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-text-fill: white;"+
                       "-fx-font-family: \"Times New Roman\";" +
                       "-fx-font-style: italic;" +
                       "-fx-font-size: 45px;"
                       );

        col[colNumber].add(pawn.createPawn(color), 0,5);
        
        if(sevenNum[colNumber]>0){
            col[colNumber].add(title,0,5);
        }
            if(sevenNum[colNumber]==0){
            col[colNumber].getChildren().clear();
            for(int k=0;k<6;k++){col[colNumber].add(pawn.createPawn(color), 0,k);}
            }
        }
        else{
        int contains=howManyContains(col,colNumber);    
        col[colNumber].getChildren().remove(contains-1);
        }
        if(color){
                
            blueUp[colNumber]-=1;
        }
        else{
        blackUp[colNumber]-=1; 
        }
    }
    public void removeDown(GridPane[] col,int colNumber,boolean color){
    if(sevenNum[colNumber]>0){
            
        sevenNum[colNumber]-=1;
        
        Label title = new Label(Integer.toString(sevenNum[colNumber]+6));
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-text-fill: white;"+
                       "-fx-font-family: \"Times New Roman\";" +
                       "-fx-font-style: italic;" +
                       "-fx-font-size: 45px;"
                       );

        col[colNumber].add(pawn.createPawn(color), 0,0);
        
        if(sevenNum[colNumber]>0){
            col[colNumber].add(title,0,0);
        }
            if(sevenNum[colNumber]==0){
            col[colNumber].getChildren().clear();
            for(int k=5;k>-1;k--){col[colNumber].add(pawn.createPawn(color), 0,k);}
            }
        }
        else{
        int contains=howManyContains(col,colNumber);

        col[colNumber].getChildren().remove(contains-1);
        }
    if(color){
                
                    blueDown[colNumber]-=1;
                }
                else{
                
                   blackDown[colNumber]-=1;
                
                }
    
    }
    
    public int howManyContains(GridPane[] grid,int column){
    
        int counter=0; 
        
        ObservableList<Node> childrens = grid[column].getChildren();

        for(Node node : childrens) {
        
            if(node instanceof Circle)
            counter++;
        }
        
        if(sevenNum[column]>0){return sevenNum[column]+6;}
        
        return counter;
    }

    public int[] getBlueUp(){
    
    return blueUp;
    
    }
    
    public int[] getBlueDown(){
    
    return blueDown;
    
    }
    
    public int[] getBlackUp(){
    
    return blackUp;
    
    }
    
    public int[] getBlackDown(){
    
    return blackDown;
    
    }

    public void playBlue(GridPane[] grid,int column,int a,int b){

        int[] oneBlack = new int[30];
        int[] blues = new int[30];
        int[] empty = new int[30];
        
        oneBlack= oneBlack();
        blues=whereBlue();
        empty=emptyCols();
        
        for(int hh=0; hh<24; hh++){
        
            grid[hh].setOnMouseClicked(null);
            grid[hh].setStyle(null);
            
        }

        if((column+a<24) && (column+b<24) ){
        
            if( (oneBlack[column+a] == 1) || (blues[column+a] !=0) || (empty[column+a]==1) ){
            
            grid[column+a].setStyle("-fx-border-color:yellow;");
            grid[column+a].setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {

                blueMoves(grid,column,a);
                
                grid[column+a].setOnMouseClicked(null);
                grid[column+b].setOnMouseClicked(null);
                grid[column+a].setStyle(null);
                grid[column+b].setStyle(null);


                if(countDice<times){
                
                    bluePlays(grid,b,b);
                
                    countDice++;
                }
            }
            });
            
            }
        }
        if((column+a<24) && (column+b<24) ){
        
            if( (oneBlack[column+b] == 1) ||(blues[column+b] !=0) || (empty[column+b]==1) ){
            
            grid[column+b].setStyle("-fx-border-color:yellow;");
            grid[column+b].setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {

                blueMoves(grid,column,b);
                
                grid[column+a].setOnMouseClicked(null);
                grid[column+b].setOnMouseClicked(null);
                grid[column+a].setStyle(null);
                grid[column+b].setStyle(null);
                
                if(countDice<times){
                    
                    countDice++;                
                    bluePlays(grid,a,a);
                }
            }
            });
            
            }
        }
    }
    
    public void blueMoves(GridPane[] grid,int column,int dice){

                
                if( (column < 12) && (column+dice<12) ){
                
                    removeUp(grid,column,true);
                    setUp(grid,column+dice,1,true);
            
                
                }else if( (column < 12) && (column+dice>=12) ){
                
                    removeUp(grid,column,true);
                    setDown(grid,column+dice,1,true);
                
                }else{
                
                    removeDown(grid,column,true);
                    setDown(grid,column+dice,1,true);

                }              
    grid[column+dice].setOnMouseClicked(null);

    }
    private boolean played=true;private boolean workIt=true;
    public void takeIt(GridPane[] grid,int[] blue,int dice){
    boolean biggest=false;
    for(int a=18;a<24;a++){
                
                if( (a<24-dice) && (blue[a]!=0) ){
                final int b=a;biggest=true;
                    grid[a].setStyle("-fx-border-color:orange;");
                    grid[a].setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e) {
                    removeDown(grid,b,true);
                    setDown(grid,b+dice,1,true);


                    for(int a=18;a<24;a++){
                    
                    grid[a].setOnMouseClicked(null);
                    grid[a].setStyle(null);
                    
                    }
                    
                    }                
                    });
                }
                else if( (a>24-dice) && (blue[a]!=0) && !biggest && (howManyContains(grid,a)>0) ){

                    final int b=a;    
                    grid[a].setStyle("-fx-border-color:orange;");
                    grid[a].setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e) {
                    
                        removeDown(grid,b,true);
                       for(int a=18;a<24;a++){
                    
                    grid[a].setOnMouseClicked(null);
                    grid[a].setStyle(null);
                    
                    }
                        
                    }                
                    });
                
                }
    }
    }
    public void takeItB(GridPane[] grid,int[] black,int dice){
    boolean biggest=false;
    for(int a=5;a>-1;a--){
                
                if( (a>dice-1) && (black[a]!=0) ){
                final int b=a;biggest=true;
                    grid[a].setStyle("-fx-border-color:orange;");
                    grid[a].setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e) {
                    removeUp(grid,b,false);
                    setUp(grid,b-dice,1,false);


                    for(int a=5;a>-1;a--){
                    
                    grid[a].setOnMouseClicked(null);
                    grid[a].setStyle(null);
                    
                    }
                    
                    }                
                    });
                }
                else if( (a<dice-1) && (black[a]!=0) && !biggest && (howManyContains(grid,a)>0)){

                    final int b=a;    
                    grid[a].setStyle("-fx-border-color:orange;");
                    grid[a].setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e) {
                    
                        removeUp(grid,b,false);
                       for(int a=5;a>-1;a--){
                    
                    grid[a].setOnMouseClicked(null);
                    grid[a].setStyle(null);
                    
                    }
                        
                    }                
                    });
                
                }
    }
    }
    public void bluePlays(GridPane[] grid,int dOne,int dTwo){
                                        
        int[] blue=new int[30];
        int[] oneBlack= new int[30];
        int[] empty=new int[30];
        
        blue=whereBlue();
        oneBlack=oneBlack();
        empty=emptyCols();
        
        if(blueGameE(blue)){

            try {
                
                Stage stage=new Stage(); 
                    
                Group group = new Group();
                Scene scene = new Scene(group, 500, 500, Color.BLUE);
                stage.setTitle("BLUE WINS");
                stage.setScene(scene);
                stage.show();
                b.close();
    
            } catch (Exception ex) {}

        }
        
        if( blueEnds(blue) ){
            
            for(int x=18;x<24;x++){
            
            if( (blue[x]!=0) && (x+dOne<25 || x+dTwo<25) && countDice<times){
                final int xx=x;workIt=false;
                grid[xx].setStyle("-fx-border-color:red;");
                grid[xx].setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent e) {
                                        
                grid[xx].setStyle(null);
                grid[xx].setOnMouseClicked(null);

                    if(xx+dOne<24){
                        
                                grid[xx+dOne].setStyle("-fx-border-color:yellow;");
                                grid[xx+dOne].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    played=false;
                                    removeDown(grid,xx,true);
                                    setDown(grid,xx+dOne,1,true);

                                    for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                                    
                                    if(countDice<times){
                                        countDice++;workIt=true;
                                        bluePlays(grid,dTwo,dTwo);
                                        }
                                    
                                }                
                                });
                    }else if(xx+dOne==24 ){
                        removeDown(grid,xx,true);
                                played=false;
                                    for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                                    
                                        if(countDice<times){
                                        countDice++;workIt=true;
                                        bluePlays(grid,dTwo,dTwo);
                                        }
                        
                        
                    }
                    if(xx+dTwo<24 && played){
                        
                                grid[xx+dTwo].setStyle("-fx-border-color:yellow;");
                                grid[xx+dTwo].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    
                                                                                removeDown(grid,xx,true);
                                        setDown(grid,xx+dTwo,1,true);
                                        
                                        for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                        }

                                        if(countDice<times){
                                        countDice++;workIt=true;
                                        bluePlays(grid,dOne,dOne);
                                        }

                                }                
                                });
                    }else if(xx+dTwo==24 && played){
                        removeDown(grid,xx,true);
                        
                                    for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                                      
                                        if(countDice<times){
                                        countDice++;workIt=true;
                                        bluePlays(grid,dOne,dOne);
                                        }
                    }            
                }                
                });
            }//if      
            else if( (blue[x]!=0) && (x+dOne<25 || x+dTwo<25) ){
            
            final int xx=x;workIt=false;
                grid[xx].setStyle("-fx-border-color:red;");
                grid[xx].setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent e){
 
                grid[xx].setStyle(null);
                grid[xx].setOnMouseClicked(null);

                    if(xx+dOne<24){
                        
                                grid[xx+dOne].setStyle("-fx-border-color:yellow;");
                                grid[xx+dOne].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    played=false;
                                    removeDown(grid,xx,true);
                                    setDown(grid,xx+dOne,1,true);

                                    for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }

                                }                
                                });
                    }else if(xx+dOne==24 ){
                        removeDown(grid,xx,true);
                                played=false;
                                    for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                    }
                    if(xx+dTwo<24 && played){
                        
                                grid[xx+dTwo].setStyle("-fx-border-color:yellow;");
                                grid[xx+dTwo].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    
                                        removeDown(grid,xx,true);
                                        setDown(grid,xx+dTwo,1,true);
                                        
                                        for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                        }

                                }                
                                });
                    }else if(xx+dTwo==24 && played){
                        removeDown(grid,xx,true);
                        
                                    for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                    }            
                }                
                });
            
            }//else
            else if( (blue[x]!=0) && ( ( x+dOne>24 ) || ( x+dTwo>24 ) ) && workIt){
            
            int xx=x;x=24;
                                grid[xx].setStyle("-fx-border-color:yellow;");
                                grid[xx].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    
                                    for(int off=18;off<24;off++){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                        }
                                    
                                    removeDown(grid,xx,true);

                                        if(countDice<times){
                                        countDice++;
                                        bluePlays(grid,dOne,dOne);
                                        }
                                        
                                        

                                }
                                });
            
            
            }
            }//for

                        
                        
        }//if blueEnds
        else{
        
            if(outBlue>0){
            
                if(outBlue==1){

                if( (blue[dOne-1] !=0) || (oneBlack[dOne-1]==1) || (empty[dOne-1]==1) ){
                
                grid[dOne-1].setStyle("-fx-border-color:red;");
                grid[dOne-1].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                setUp(grid,dOne-1,1,true);
                grid[dOne-1].setStyle(null);
                outBlue--;countDice++;grid[dOne-1].setOnMouseClicked(null);
                grid[dTwo-1].setStyle(null);grid[dTwo-1].setOnMouseClicked(null);
                bluePlays(grid,dTwo,dTwo);
                }                
                });
            }
            if( (blue[dTwo-1] !=0) || (oneBlack[dTwo-1]==1) || (empty[dTwo-1]==1) ){
                
                grid[dTwo-1].setStyle("-fx-border-color:red;");
                grid[dTwo-1].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                setUp(grid,dTwo-1,1,true);
                grid[dTwo-1].setStyle(null);
                outBlue--;countDice++;grid[dTwo-1].setOnMouseClicked(null);
                grid[dOne-1].setStyle(null);grid[dOne-1].setOnMouseClicked(null);
                bluePlays(grid,dOne,dOne);
                }                
                });
                
            }
            }//if(outBlue==1)
            else{
                if( (blue[dOne-1] !=0) || (oneBlack[dOne-1]==1) || (empty[dOne-1]==1) ){
                
                grid[dOne-1].setStyle("-fx-border-color:yellow;");
                grid[dOne-1].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                setUp(grid,dOne-1,1,true);
                grid[dOne-1].setStyle(null);
                outBlue--;
                }                
                });
            }
            if( (blue[dTwo-1] !=0) || (oneBlack[dTwo-1]==1) || (empty[dTwo-1]==1) ){
                
                grid[dTwo-1].setStyle("-fx-border-color:yellow;");
                grid[dTwo-1].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                setUp(grid,dTwo-1,1,true);
                grid[dTwo-1].setStyle(null);
                outBlue--;
                }                
                });
            }//if
            }//else
        }//if(outBlue>0)
        else{
            
        for(int gg=0; gg<24; gg++){        
        if((gg+dOne<24) && (gg+dTwo<24) ){    
        final int l=gg;        
        if( (blue[gg] !=0) && ( (oneBlack[gg+dOne]==1) || (oneBlack[gg+dTwo]==1) || (empty[gg+dOne]==1) || (empty[gg+dTwo]==1) || (blue[gg+dOne] !=0) || (blue[gg+dTwo] !=0) ) ){
            
            grid[gg].setStyle("-fx-border-color:pink;");
            grid[gg].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                playBlue(grid,l,dOne,dTwo);
                
            }
            });
        
        }
        }
        }//for
        }//else
        }//else
        
    }

    public void blackPlays(GridPane[] grid,int dOne,int dTwo){
    
        
        int[] black=new int[30];
        int[] oneBlue= new int[30];
        int[] empty=new int[30];
        
        
        black=whereBlack();
        oneBlue=oneBlue();
        empty=emptyCols();
        
                if(blackGameE(black)){
    
                    try {       Stage stage=new Stage(); 

                    Group group = new Group();
                    Scene scene = new Scene(group, 500, 500, Color.BLACK);
                    stage.setTitle("BLACK WINS");
                    stage.setScene(scene);
                    stage.show();
                    b.close();
        
                    } catch (Exception ex) { }

                }
                                
            if( blackEnds(black) ){

            for(int x=5;x>-1;x--){
            
            if( (black[x]!=0) && (x-dOne>-2 || x-dTwo>-2) && countDice<times){
                final int xx=x;workIt=false;
                grid[xx].setStyle("-fx-border-color:red;");
                grid[xx].setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent e) {
                                        
                grid[xx].setStyle(null);
                grid[xx].setOnMouseClicked(null);

                    if(xx-dOne>-1){
                        
                                grid[xx-dOne].setStyle("-fx-border-color:yellow;");
                                grid[xx-dOne].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    played=false;
                                    removeUp(grid,xx,false);
                                    setUp(grid,xx-dOne,1,false);

                                    for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                                    
                                    if(countDice<times){
                                        countDice++;workIt=true;
                                        blackPlays(grid,dTwo,dTwo);
                                        }
                                    
                                }                
                                });
                    }else if(xx-dOne==-1 ){
                        removeUp(grid,xx,false);
                                played=false;
                                    for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                                    
                                        if(countDice<times){
                                        countDice++;workIt=true;
                                        blackPlays(grid,dTwo,dTwo);
                                        }
                        
                        
                    }
                    if(xx-dTwo>-1 && played){
                        
                                grid[xx-dTwo].setStyle("-fx-border-color:yellow;");
                                grid[xx-dTwo].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    
                                        removeUp(grid,xx,false);
                                        setUp(grid,xx-dTwo,1,false);
                                        
                                        for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                        }

                                        if(countDice<times){
                                        countDice++;workIt=true;
                                        blackPlays(grid,dOne,dOne);
                                        }

                                }                
                                });
                    }else if(xx-dTwo==-1 && played){
                        removeUp(grid,xx,false);
                        
                                    for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                                      
                                        if(countDice<times){
                                        countDice++;workIt=true;
                                        blackPlays(grid,dOne,dOne);
                                        }
                    }            
                }                
                });
            }//if      
            else if( (black[x]!=0) && (x-dOne>-2 || x-dTwo>-2) ){
            
            final int xx=x;workIt=false;
                grid[xx].setStyle("-fx-border-color:pink;");
                grid[xx].setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent e){
 
                grid[xx].setStyle(null);
                grid[xx].setOnMouseClicked(null);

                    if(xx-dOne>-1){
                        
                                grid[xx-dOne].setStyle("-fx-border-color:yellow;");
                                grid[xx-dOne].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    played=false;
                                    removeUp(grid,xx,false);
                                    setUp(grid,xx-dOne,1,false);

                                    for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }

                                }                
                                });
                    }else if(xx-dOne==-1 ){
                        removeUp(grid,xx,false);
                                played=false;
                                    for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                    }
                    if(xx-dTwo>-1 && played){
                        
                                grid[xx-dTwo].setStyle("-fx-border-color:yellow;");
                                grid[xx-dTwo].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    
                                        removeUp(grid,xx,false);
                                        setUp(grid,xx-dTwo,1,false);
                                        
                                        for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                        }

                                }                
                                });
                    }else if(xx-dTwo==-1 && played){
                        removeUp(grid,xx,false);
                        
                                    for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                    }
                    }            
                }                
                });
            
            }//else
            else if( (black[x]!=0) && ( ( x-dOne<-1 ) || ( x-dTwo<-1 ) ) && workIt){
            
            int xx=x;x=-1;
                                grid[xx].setStyle("-fx-border-color:white;");
                                grid[xx].setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                public void handle(MouseEvent e) {
                                    
                                    for(int off=5;off>-1;off--){
                                    
                                        grid[off].setStyle(null);
                                        grid[off].setOnMouseClicked(null);
                                        }
                                    
                                    removeUp(grid,xx,false);

                                        if(countDice<times){
                                        countDice++;
                                        blackPlays(grid,dOne,dOne);
                                        }
                                }
                                });
            }
            }//for
        }//else
        else{

            if(outBlack>0){
            if(outBlack==1){
                
                if( (black[24-dOne] !=0) || (oneBlue[24-dOne]==1) || (empty[24-dOne]==1) ){
                int[] oneB=oneBlue;
                grid[24-dOne].setStyle("-fx-border-color:red;");
                grid[24-dOne].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                    
                setDown(grid,24-dOne,1,false);

                outBlack--;countDice++;grid[24-dOne].setStyle(null);grid[24-dOne].setOnMouseClicked(null);
                grid[24-dTwo].setStyle(null);grid[24-dTwo].setOnMouseClicked(null);
                blackPlays(grid,dTwo,dTwo);
                }                
                });
            }
            if( (black[24-dTwo] !=0) || (oneBlue[24-dTwo]==1) || (empty[24-dTwo]==1) ){
                int[] oneB=oneBlue;
                grid[24-dTwo].setStyle("-fx-border-color:red;");
                grid[24-dTwo].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                    
                setDown(grid,24-dTwo,1,false);
                
                outBlack--;countDice++;grid[24-dTwo].setStyle(null);grid[24-dTwo].setOnMouseClicked(null);
                grid[24-dOne].setStyle(null);grid[24-dOne].setOnMouseClicked(null);
                blackPlays(grid,dOne,dOne);
                }                
                });

            }
            }//if(outBlue==1)
            else{
            
            if( (black[24-dOne] !=0) || (oneBlue[24-dOne]==1) || (empty[24-dOne]==1) ){
                
                grid[24-dOne].setStyle("-fx-border-color:yellow;");
                grid[24-dOne].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                setDown(grid,24-dOne,1,false);
                grid[24-dOne].setStyle(null);
                outBlack--;
                }                
                });
            }
            if( (black[24-dTwo] !=0) || (oneBlue[24-dTwo]==1) || (empty[24-dTwo]==1) ){
                
                grid[24-dTwo].setStyle("-fx-border-color:yellow;");
                grid[24-dTwo].setOnMouseClicked(new EventHandler<MouseEvent>(){
                
                @Override
                public void handle(MouseEvent e) {
                setDown(grid,24-dTwo,1,false);
                grid[24-dTwo].setStyle(null);
                outBlack--;
                }                
                });
            } 
            }//else
        }//if(outBlack>0)
        else{
                            
        for(int gg=0; gg<24; gg++){
        
            if( (gg-dOne>-1) && (gg-dTwo>-1) ){
            final int l=gg;
        
                if( (black[gg] !=0) && ( (oneBlue[gg-dOne]==1) || (oneBlue[gg-dTwo]==1) || (empty[gg-dOne]==1) || (empty[gg-dTwo]==1) || (black[gg-dOne] !=0) || (black[gg-dTwo] !=0) ) ){

                    grid[gg].setStyle("-fx-border-color:green;");
                    grid[gg].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {

                        playBlack(grid,l,dOne,dTwo);

                    }
                    });

                }
            }//if
        }//for
        }//else
        }//else
    }
    
    public void playBlack(GridPane[] grid,int column,int a,int b){

        int[] oneBlue = new int[30];
        int[] black = new int[30];
        int[] empty = new int[30];
        
        oneBlue= oneBlue();
        black=whereBlack();
        empty=emptyCols();
        
        for(int hh=0; hh<24; hh++){
        
            grid[hh].setOnMouseClicked(null);
            grid[hh].setStyle(null);
            
        }
        if(column-a>-1){
        if( (oneBlue[column-a] == 1) ||(black[column-a] !=0) || (empty[column-a]==1) ){
            
            grid[column-a].setStyle("-fx-border-color:yellow;");
            grid[column-a].setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {

                blackMoves(grid,column,a);
                
                grid[column-a].setOnMouseClicked(null);
                grid[column-b].setOnMouseClicked(null);
                grid[column-a].setStyle(null);
                grid[column-b].setStyle(null);


                if(countDice<times)
                blackPlays(grid,b,b);
                
                countDice++;
            }
            });
            
        }
        }
        if(column-b>-1){
        if( (oneBlue[column-b] == 1) ||(black[column-b] !=0) || (empty[column-b]==1) ){
            
            grid[column-b].setStyle("-fx-border-color:yellow;");
            grid[column-b].setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {

                blackMoves(grid,column,b);
                
                grid[column-a].setOnMouseClicked(null);
                grid[column-b].setOnMouseClicked(null);
                grid[column-a].setStyle(null);
                grid[column-b].setStyle(null);
                
                if(countDice<times)
                blackPlays(grid,a,a);
                
                countDice++;
            }
            });
            
        }
        }

    }
    
    public void blackMoves(GridPane[] grid,int column,int dice){

                
                if( (column > 11) && (column-dice>11) ){
                
                    removeDown(grid,column,false);
                    setDown(grid,column-dice,1,false);
            
                
                }else if( (column >11) && (column-dice<=11) ){
                
                    removeDown(grid,column,false);
                    setUp(grid,column-dice,1,false);
                
                }else{
                
                    removeUp(grid,column,false);
                    setUp(grid,column-dice,1,false);

                }              
    grid[column-dice].setOnMouseClicked(null);

    }
        
    public int[] whereBlue(){
    
        int[] numbero = new int[30];
        
        for(int a=0; a<24; a++){
    
        if(blueUp[a] != 0){
            numbero[a]=blueUp[a];
        }    
        if(blueDown[a] != 0){
            numbero[a]=blueDown[a];
        }

        }

        return numbero;  
    }
    
    public int[] whereBlack(){
    
        int[] numbero = new int[30];
        
        for(int a=0; a<24; a++){
    
        if(blackUp[a] != 0){
            numbero[a]=blackUp[a];
        }    
        if(blackDown[a] != 0){
            numbero[a]=blackDown[a];
        }

        }
        return numbero;  
    }

    public int[] oneBlue(){
    
    int[] numbero=new int[30];
    
    for(int a=0; a<24; a++){
    
        if(blueUp[a] == 1){
            numbero[a]=blueUp[a];
        }    
        if(blueDown[a] == 1){
            numbero[a]=blueDown[a];
        }

        }
    
    return numbero;
    
    }

    public int[] oneBlack(){
    
    int[] numbero=new int[30];
    
    for(int a=0; a<24; a++){
    
        if(blackUp[a] == 1){
            numbero[a]=blackUp[a];
        }    
        if(blackDown[a] == 1){
            numbero[a]=blackDown[a];
        }

        }
    return numbero;
    
    }

    public int[] emptyCols(){
    
            int[] emptyCols= new int[30];
            
            for(int a=0; a<24; a++){

                    if( (blueUp[a] == 0) && (blueDown[a] == 0) && (blackUp[a] == 0) && (blackDown[a] == 0)){
                    
                        emptyCols[a]=1;
                    
                    }else{emptyCols[a]=0;}
            }
    return emptyCols;
    }

    public void setTimes(int times){ this.times=times;}
    public int getTimes(){return times;}
    public void reset(){countDice=1;played=true;workIt=true;}

    public boolean blueEnds(int[] blue){

     for(int q=17; q>0;q--){

     if(blue[q]!=0)
         return false;
     }
         return true;

    }

    public boolean blackEnds(int[] black){

     for(int q=6; q<23;q++){

     if(black[q]!=0)
         return false;
     }
         return true;

    }
    
    public boolean blueGameE(int[] blue){
    
        for(int q=0; q<24;q++){

        if(blue[q]!=0)
            return false;
        }
         return true;
    }
    public boolean blackGameE(int[] blue){
    
        for(int q=23; q>-1;q--){

        if(blue[q]!=0)
            return false;
        }
         return true;
    } 
}
}