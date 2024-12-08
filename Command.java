public class Command {
    // Command holds a user entered command and processes its syntax
	
	
	//Command now recognises 'double', 'yes' and 'no' as commands
	//Command can also be accessed with ui.getCommand() and ui.getCommand(possiblePlays)
	//ui.getCommand() is simpler and only recognises 'yes', 'no' and 'quit'

    private String input;
    private boolean valid, move, quit, cheat,doubleCube,yes,no;
    private Play play;

    Command() {
        input = "";
        valid = true;
        move = false;
        cheat = false;
        quit = false;
        doubleCube = false;
        yes = false;
        no = false;
        
    }

    Command(String input, Plays possiblePlays) {
        // regex examples: "[a-f]", "[a-z]|a[a-g]", "[a-z]|a[a-z]|b[a-n]"
        int numberOfFirstLetters = (possiblePlays.number()-1)/26;
        char finalLetterLimit = (char) ((possiblePlays.number()-1)%26 + (int) 'a');
        String regex = "";
        char firstChar = 'a';
        for (int i=0; i<numberOfFirstLetters; i++) {
            regex = regex + "[a-z]|" + firstChar;
            firstChar++;
        }
        regex = regex + "[a-" + finalLetterLimit + "]";
        this.input = input;
        String text = input.toLowerCase().trim();
        if (text.equals("quit")) {
            valid = true;
            move = false;
            cheat = false;
            quit = true;
        } else if (text.matches(regex)) {
            int option = 0;
            if (text.length()==1) {
                option = (int) text.charAt(0) - (int) 'a';
            } else if (text.length()==2) {
                option = ((int) text.charAt(0) - (int) 'a' + 1) * 26 + (int) text.charAt(1) - (int) 'a';
            }
            play = possiblePlays.get(option);
            valid = true;
            move = true;
            cheat = false;
        } else if (text.equals("cheat")) {
            valid = true;
            move = false;
            cheat = true;
        } else if(text.equals("double")) {
        	valid = true;
        	move = false;
        	doubleCube = true;
        	cheat = false;
        }else if(text.equals("yes")) {
        	valid = true;
        	yes = true;
        	
        }else if(text.equals("no")) {
        	valid = true;
        	no = true;
        	
        }else {
            valid = false;
        }
    }
    
    Command(String input) {
       
        this.input = input;
        String text = input.toLowerCase().trim();
        if (text.equals("quit")) {
            valid = true;
            move = false;
            cheat = false;
            quit = true;
        }
        else if(text.equals("yes")) {
        	valid = true;
        	yes = true;
        	
        }else if(text.equals("no")) {
        	valid = true;
        	no = true;
        	
        }else {
            valid = false;
        }
    }
    

    public Play getPlay() {
        return play;
    }
    
    public boolean isNo() {
    	return no;
    }
    
    public boolean isYes() {
    	return yes;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isMove() {
        return move;
    }

    public boolean isQuit() {
        return quit;
    }

    public boolean isCheat() {
        return cheat;
    }
    
    public boolean isDouble() {
    	return doubleCube;
    }

    public String toString() {
        return input;
    }
}
