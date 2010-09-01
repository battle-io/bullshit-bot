/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javawrapper;
import java.io.*;
import java.util.Random;

/**
 *
 * @author Toby
 */
public class GameBoard {

        private Hand m_hand;
        public GameBoard() {
            m_hand = new Hand();
            ClearBoard();
        }

        public void ResetGame(){
            ClearBoard();
        }

        private void ClearBoard() {
            m_hand.Reset();
        }

   
        public void DebugPrintHand() {
        }

        public String PlayCards( int Card )
        {
            while( m_hand.ValidateHand( Card, 1 ) == false )
            {
                Card = (Card+1)%(Hand.NumCardsInDeck());
            }
            try{
                m_hand.RemoveCards( Card, 1);
            }
            catch( Exception ex )
            {

            }
            return String.valueOf(Card);

        }

        public String BullShitResponse( String TurnInfo )
        {

            Random generator = new Random();
            int card = generator.nextInt(10);
            if( card == 7 )
            {
               return "bullshit";
            }
            return "none";
        }

        public void AddCards( String CardList )
        {

            //Split up the list
            String[] cardArray = CardList.split(",");

            //array for storing cards / number of that card
            int [] result = new int [Hand.NumCardsInDeck()];
            for( int i = 0; i < Hand.NumCardsInDeck(); i++ )
                result[i] = 0;

            //For each card in the list bump the index
            for( int i = 0; i < cardArray.length; i++ ){
                int card = Integer.parseInt( cardArray[i] );
                result[card] = result[card]+1;
            }

            //Remove the cards found in the list
            for( int j = 0; j < Hand.NumCardsInDeck(); j++)
            {
                if( result[j] != 0 )
                {
                    try
                    {
                         m_hand.InsertCards( j, result[j]);
                    }
                    catch( Exception ex )
                    {

                    }
                }
            }


        }
}

