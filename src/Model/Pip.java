package Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import controller.ColorParser;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;

/**
 * This class represents the Pip object in Backgammon.
 * This class helps BoardComponents class to initialize the checkers for each pip object.
 * This class also adds the checkers objects to the pip object, to be drawn to the stage.
 * 
 */
public class Pip extends CheckersStorer implements ColorParser {
	private Background normalBG;
	private Background highlightedBG; 
	private int pipNum;
	private SurpriseStation surpriseStation;
	private QuestionStation questionStation;

	private ImageView surpriseIcon;
	private ImageView questionIcon;
	private boolean isSurpriseActivated = false;
	private List<QuestionStation> questionStations;
	
	
	/**
	 * Default Constructor
	 * 		- Initialize the img and imgHighlighteded instance variable of the pip.
	 * 		- Set this pip's transformation, alignment, size, etc.
	 * 		- Set that img to be the background of this pip.
	 * 		- Initialize this pip's listeners.
	 * 
	 * @param color of the pip.
	 * @param rotation either 0 or 180. 0 = pointing upwards. 180 = pointing downwards. 
	 */
	public Pip(Color color, double rotation, int pipNum) {
		super();
		this.pipNum = pipNum;
		String colorString = parseColor(color);
		InputStream input1 = getClass().getResourceAsStream("/game/img/board/" + colorString + "_point.png");
		InputStream input2 = getClass().getResourceAsStream("/game/img/board/" + colorString + "_point_highlighted.png");
		normalBG = new Background(new BackgroundImage(new Image(input1), null, null, null, null));
		highlightedBG = new Background(new BackgroundImage(new Image(input2), null, null, null, null));
		try {
			input1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setRotate(rotation);
		setAlignment(Pos.BOTTOM_CENTER);
		// don't simply set point max and pref size, this will effect how the point is drawn.
		setMinSize(GameConstants.getPipSize().getWidth(), GameConstants.getPipSize().getHeight());	// highlighted and non-highlighted should have the same width & height.
		setNormalImage();
		
	
	}
	
	public void setSurpriseStation(SurpriseStation surpriseStation) {
		this.surpriseStation = surpriseStation;	   
		if (this.surpriseIcon == null) {
	        // יצירת האייקון של תחנת ההפתעה
	        InputStream input = getClass().getResourceAsStream("/game/img/board/SurpriseIcon.png");
	        Image surpriseImage = new Image(input);

	        // יצירת ImageView והגדרות
	        surpriseIcon = new ImageView(surpriseImage);
	        surpriseIcon.setFitWidth(20); // רוחב מותאם אישית
	        surpriseIcon.setFitHeight(20); // גובה מותאם אישית
	        surpriseIcon.setTranslateY(-10); // התאמה למיקום
	        surpriseIcon.setMouseTransparent(true); // לא להגיב ללחיצות עכבר

	        // הוספת האייקון לשקע
	        this.getChildren().add(surpriseIcon);
	        
	      //  try {
	        //    input.close();
	        //} catch (IOException e) {
	          //  e.printStackTrace();
	        //}
	    }
	}

	public boolean hasSurpriseStation() {
	    return surpriseIcon != null;
	}

/*	// בודק אם יש אייקון
	public boolean hasSurpriseIcon() {
	    return surpriseIcon != null && this.getChildren().contains(surpriseIcon);
	}

	// קובע את האייקון
	public void setSurpriseIcon(ImageView icon) {
		this.surpriseIcon = icon;
	    if (!this.getChildren().contains(icon)) {
	        this.getChildren().add(icon);
	    }// הוספת האייקון לשקע אם לא קיים
	}

	// מחזיר את האייקון
	public ImageView getSurpriseIcon() {
	    return surpriseIcon;
	}
	
	public void ensureSurpriseIcon() {
	    if (surpriseIcon != null && !this.getChildren().contains(surpriseIcon)) {
	        this.getChildren().add(surpriseIcon); // הבטחה שהאייקון יישאר
	    }
	}*/
	
	public void activateSurprise(Player player) {
        if (surpriseStation != null) {
            surpriseStation.activate(player);
            this.isSurpriseActivated = true;
          //  removeSurpriseStation();
        }
    }
	  
	public boolean isSurpriseActivated1() {
	    return isSurpriseActivated;
	}
	
	/*public void removeSurpriseIcon() {
	    if (this.hasSurpriseIcon()) {
	        this.getChildren().remove(this.getSurpriseIcon());
	    }
	}*/

	
	/*public void removeSurpriseStation() {
	    if (this.surpriseIcon != null) {
	        this.getChildren().remove(surpriseIcon);
	        surpriseIcon = null;
	        this.surpriseStation = null;
	    }
	    
	}*/

	    
	// הוספת תחנת שאלה למצב של Pip
    public void setQuestionStation(QuestionStation questionStation) {
    	this.questionStation = questionStation;	   
		if (this.questionStation == null) {
	        // יצירת האייקון של תחנת ההפתעה
	        InputStream input = getClass().getResourceAsStream("/game/img/board/question_icon.png");
	        Image questionImage = new Image(input);

	        // יצירת ImageView והגדרות
	        questionIcon = new ImageView(questionImage);
	        questionIcon.setFitWidth(20); // רוחב מותאם אישית
	        questionIcon.setFitHeight(20); // גובה מותאם אישית
	        questionIcon.setTranslateY(-10); // התאמה למיקום
	        questionIcon.setMouseTransparent(true); // לא להגיב ללחיצות עכבר

	        // הוספת האייקון לשקע
	        this.getChildren().add(questionIcon);
	      //  try {
	        //    input.close();
	        //} catch (IOException e) {
	          //  e.printStackTrace();
	        //}
	    }
        ensureQuestionIcon();

    }

    // אם יש אייקון של תחנת שאלה
    public boolean hasQuestionStation() {
        return questionIcon != null;
    }
    
 // בודק אם יש אייקון
 	public boolean hasQuestionIcon() {
 	    return questionIcon != null && this.getChildren().contains(questionIcon);
 	}

    // מחזיר את אייקון תחנת השאלה
    public ImageView getQuestionIcon() {
        return questionIcon;
    }

    // פעולת הפתיחה של תחנת השאלה
	public void activateQuestion(Player player) {
        if (questionStation != null) {
        	questionStation.activate(player);
        }
        ensureQuestionIcon();
        
    }

    public boolean isSurpriseActivated() {
        return isSurpriseActivated;
    }

   
 

	// קובע את האייקון
	public void setQuestionIcon(ImageView icon) {
		this.questionIcon = icon;
	    if (!this.getChildren().contains(icon)) {
	        this.getChildren().add(icon);
	    }// הוספת האייקון לשקע אם לא קיים
	}
	
	public void ensureQuestionIcon() {
	    if (questionIcon != null && !this.getChildren().contains(questionIcon)) {
	        this.getChildren().add(questionIcon); // הבטחה שהאייקון יישאר
	    }
	}
	

	  


    
	/**
	 * Use the highlighted image.
	 */
	public void setHighlightImage() {
		setBackground(highlightedBG);
	}

	 
	/**
	 * Use the normal image (i.e. image that is not highlighted).
	 */
	public void setNormalImage() {
		setBackground(normalBG);
	}
	
	/**
	 * Returns the pointNum instance variable (the number the point represents).
	 * @return the pointNum instance variable.
	 */
	public int getPipNumber() {
		return pipNum;
	}


}
