/*
Java Wrapper v1.0

This wrapper allows Java to connect and communicate with a code-wars
server. Use this code and add your own to participate in a code-wars
game or challenge. Information about this wrapper and how it works can 
be found at www.code-wars.com/downloads.php

Each challenge or game may use different command types and metadata to
communicate with the code-wars server. Be sure to refer to the
game-specific protocol page for these details.

Note: If your bot is disconnected from the server, the java wrapper
will automatically attempt to reconnect.
*/

package javawrapper;

public class Main {

    public static void main(String[] args) {
        
        String[] msg;
        String messageType;
        String metaData;
        GameBoard myBoard = new GameBoard();

        /* LOGIN_INFORM variables (botName, authenticationKey)
           These variables will be sent to the server to verify your and your
           bot's identity. This information must be correct or your connection
           will be refused.  Data found in online profile.  */
        
        BotConnector myBot = new BotConnector();
        
        myBot.setEmail("jones_Toby@yahoo.com");
        myBot.setBotName("BS1Toby");
        myBot.setAuthenticationKey("codewars");

        /* myBot.connect()
           This command packages your login credentials and sends a
           LOGIN_INFORM command to the code-wars game server. */
        myBot.connect();

        Integer myMove = 3;

        while(true){
            /* myBot.getMsg()
               Retrieves incoming commands sent by the server. The command is
               returned as an array: [String commandType, String metaData]
            */
            msg = myBot.getMsg();
            messageType = msg[0];
            metaData = msg[1];
            
            /*
             * Game Messages that require responses
             */
            if(messageType.equals("GAME_INITIALIZE")){
                myBoard.ResetGame();
                System.out.println( "New game started" );
            }
            else if(messageType.equals("PLAYCARDS_REQUEST")){
                int card = Integer.parseInt(metaData);
                String cards = myBoard.PlayCards( card );
                myBot.sendReply("PLAYCARDS_REPLY", cards  );
            }    
            else if(messageType.equals("TURN_SUMMARY")){
                myBot.sendReply("TURN_REPLY", myBoard.BullShitResponse( metaData ));
            }
            
            /*
             * Games Messages that don't require a response
             */
            else if(messageType.equals("BULLSHIT_RESULT")){
            }
            else if(messageType.equals("CARD_MESSAGE")){
                myBoard.AddCards( metaData );
            }
            else if(messageType.equals("GAME_ORDER")){
                System.out.println("Playing against bots : " + metaData);
            }
            else if(messageType.equals("GAME_WINNER")){
                // View Incoming Message:
                System.out.println("The winner of the Game was : " + metaData);

            }
            else if(messageType.equals("DISCONNECT_BOT_REMOTE")){
            }
            /*
             * Server Messages that don't require response
             */
            else if(messageType.equals("SERVER_MESSAGE")){
            }
            else if(messageType.equals("GAME_REPORT")){                
                /* www.code-wars.com has provided you a game report.
                   Please reference www.code-wars.com to obtain the format of
                   this variable for the game you are playing. */                
            }
            else if(messageType.equals("GAME_ABORT")){
                /* www.code-wars.com has provided you a game abort command.
                   Please reference www.code-wars.com to obtain the format of
                   this variable for the game you are playing. */
            }               
        }   
    }
}
