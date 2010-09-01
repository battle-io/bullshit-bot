package javawrapper;

import java.util.Random;
/**
 *  Class to take care of handling a deck of cards
 *  The class only tracks how many of each type of card you have
 *  and doesn't deal with suits
 */
public class Hand {

        //member variables
        //how many of each card you have
        private int[] m_cards;

        //how many types of card are there
        private static final int m_numCardsInDeck = 13;

        //constructor
        public Hand() {
            m_cards = new int [m_numCardsInDeck];
            ClearCards();
        }

        //How many cards there are in a deck
        public static int NumCardsInDeck()
        {
            return m_numCardsInDeck;
        }

        //Clear this hand by setting all the cards to 0
        private void ClearCards()
        {
            for( int i = 0; i < m_numCardsInDeck; i++ )
                m_cards[i] = 0;
        }

        //Pull a random card from the deck
        //Generates a random number
        //If that card slot is empty then it look to it's neighbor
        public int PullRandomCard()
        {
            Random generator = new Random();
            int card = generator.nextInt(m_numCardsInDeck);
            while( m_cards[card] == 0)
            {
                card++;
                card = card%m_numCardsInDeck;
            }
            m_cards[card]--;
            return card;
        }

        //Convert the cards in this hand to a comma seperated string
        public String GetHandAsString()
        {
            String handString = "";
            for( int i = 0; i < m_numCardsInDeck; i++ )
            {
                for( int j = 0; j < m_cards[i]; j++ )
                {
                    if( handString.equals(""))
                        handString += String.valueOf(i);
                    else
                    {
                        handString += ",";
                        handString += String.valueOf(i);
                    }
                }
            }
            return handString;
        }

        //Completly fill the hand by setting the number of each card to 4
        public void FillDeck()
        {
            for( int i = 0; i < m_numCardsInDeck; i++ )
                m_cards[i] = 4;
        }

        //Reset a hand, this clears it
        public void Reset()
        {
            ClearCards();
        }

        //Attempt to insert cards to a hand
        //This makes sure that you have the correct number of cards
        public void InsertCards( int Card, int Number ) throws Exception
        {
            m_cards[Card] += Number;
            if( m_cards[Card] > 4 )
                throw new Exception( "You have more cards in a hand than are possible");

        }

        //Attempt to remove cards from a deck
        //This makes sure you have the number of cards
        public void RemoveCards( int Card, int Number ) throws Exception
        {
            if( m_cards[Card] < Number )
                throw new Exception( "Attempted to remove more cards than you have ");
            m_cards[Card] -= Number;
        }

        //Check to make sure you have these cards
        public boolean ValidateHand( int Card, int Number )
        {
            if( m_cards[Card] < Number )
                return false;
            return true;
        }

        //Return the number of cards in a hand.
        public int GetNumCards()
        {
            int numCards = 0;
            for( int i = 0; i < m_numCardsInDeck; i++)
                numCards += m_cards[i];

            return numCards;
        }
                /*

        public static int ConvertStringToCardId( String Card )
        {
            if( Card.equals("A") )
                return 1;
            else if ( Card.equals("J") )
                return 11;
            else if( Card.equals("Q") )
                return 12;
            else if( Card.equals("K") )
                return 13;
            else
                return Integer.parseInt( Card );
        }
         *


        public static String ConvertCardIdToString( int Card )
        {
            if( Card == 1 )
                return "A";
            else if( Card == 11 )
                return "J";
            else if ( Card == 12 )
                return "Q";
            else if ( Card == 13 )
                return "K";
            else
                return String.valueOf(Card);
        }
*/
}
