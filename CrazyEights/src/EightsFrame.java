import javax.swing.*;
import javax.swing.ImageIcon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


//EightsFrame class which encapsulates the main game GUI. Uses JFrame forms for structure. Due to the usage of JFrame forms,
//the .java file will seem incomplete
public class EightsFrame extends JFrame
{
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 800;

    private JPanel boardPanel;
    private JPanel playerPanel;
    private JPanel scorePanel;
    private JPanel centerPanel;
    private JPanel drawPanel;
    private JPanel spacePanel2;
    private JPanel playPanel;
    private JButton clubsButton;
    private JPanel changeSuitPanel;
    private JPanel handPanel;
    private JPanel actionPanel;
    private JButton drawButton;
    private JButton playButton;
    private JButton diamondsButton;
    private JButton heartsButton;
    private JButton spadesButton;
    private JPanel spacePanel1;
    private JButton passButton;
    private JLabel score1;
    private JLabel score2;
    private JLabel score3;
    private JLabel score4;
    private JLabel drawLabel;
    private JLabel playedCard;

    private List<EightsPlayer> players;

    private ArrayList<JButton> gHand;

    private Card selectedCard;

    private EightsGame game;

    public EightsFrame()
    {
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setContentPane(boardPanel);
        drawButton.addActionListener(new DrawButtonClicked());
        playButton.addActionListener(new PlayButtonClicked(this));
        passButton.addActionListener(new PassButtonClicked());
        clubsButton.addActionListener(new SuitButtonClicked(Suit.CLUBS));
        heartsButton.addActionListener(new SuitButtonClicked(Suit.HEARTS));
        diamondsButton.addActionListener(new SuitButtonClicked(Suit.DIAMONDS));
        spadesButton.addActionListener(new SuitButtonClicked(Suit.SPADES));

        //Frame deletes when window is closed
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initialize();
    }

    private void initialize()
    {
        changeSuitPanel.setVisible(false);

        playButton.setVisible(false);

        //Construct necessary data for game
        game = new EightsGame(4);
        gHand = new ArrayList<>();

        //Get list of players
        //TODO Change implementation to keep players as private members of EightsGame
        players = game.getPlayers();

        //Set Scores visible
        updateScore();
        score1.setVisible(true);
        score2.setVisible(true);
        score3.setVisible(true);
        score4.setVisible(true);

        //Paints first players hand
        paintHand();

        //Paints top card of play pile
        paintPlayPile();
    }

    public class CardButton extends JButton
    {
        private Card card;

        public CardButton(Card c)
        {
            card = c;
            this.addActionListener(new CardButtonClicked(this));
        }

        public Card bGetCard()
        {
            return card;
        }
    }

    //TODO make this common to all systems with images
    private CardButton makeGCardPlayer(Card card)
    {

        String path = "./CrazyEights/src/resources/PNG-cards-82X164/" + card.getFace().getValue() + "_of_" + card.getSuit().getValue() + ".png";
        ImageIcon cardImage = new ImageIcon(path);

        CardButton tmp = new CardButton(card);

        tmp.setIcon(cardImage);

        tmp.setPreferredSize(new Dimension(82,164));

        return tmp;

    }

    //TODO make this common to all systems
    private JLabel makeGPlayPile(Card card)
    {

        String path = "./CrazyEights/src/resources/PNG-cards-82X164/" + card.getFace().getValue() + "_of_" + card.getSuit().getValue() + ".png";
        ImageIcon cardImage = new ImageIcon(path);

        JLabel tmp = new JLabel(cardImage);

        tmp.setPreferredSize(new Dimension(100,200));

        return tmp;
    }

    //Given a player, paints their hand of card buttons on the proper panel
    private void paintHand()
    {
        handPanel.removeAll();
        List<Card> hand = game.getCurrentHand();
        for(Card card: hand){

            CardButton cButton = makeGCardPlayer(card);
            cButton.setVisible(true);
            handPanel.add(cButton);
        }
        handPanel.revalidate();
        handPanel.repaint();

        if(game.canDrawCard()) {
            drawButton.setVisible(true);
            passButton.setVisible(false);
        }
        else{
            drawButton.setVisible(false);
            passButton.setVisible(true);
        }
    }

    //Clears the handPanel of all card components
    private void clearHand()
    {
        handPanel.removeAll();
        handPanel.revalidate();
        handPanel.repaint();
    }

    //Clears the playpanel, and then paints the play pile;
    private void paintPlayPile()
    {
        Card playPile = game.getPlayPile();
        playPanel.removeAll();
        JLabel temp = makeGPlayPile(playPile);
        temp.setVisible(true);
        playPanel.add(temp);
        playPanel.revalidate();
        playPanel.repaint();
    }

    //When the drawbutton is clicked
    //TODO check if deck is empty and end game
    //TODO figure out why there is a delay
    public class DrawButtonClicked implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            game.drawCard();
            updateScore();
            paintHand();

            if (game.isDeckEmpty()){
                Player winner = game.endGame();
                endGUI(winner);
            }
        }
    }

    public class PassButtonClicked implements ActionListener
    {

        public void actionPerformed(ActionEvent e) {

            game.pass();
            handPanel.setVisible(false);
            JOptionPane.showMessageDialog(boardPanel, "Player " + game.getCurrentPlayerID() + "'s Turn!");
            handPanel.setVisible(true);
            paintHand();
            playButton.setVisible(false);
        }
    }

    public class PlayButtonClicked implements ActionListener
    {
        //Needed to perform actions on the frame
        EightsFrame frame;

        public PlayButtonClicked(EightsFrame f)
        {
            frame = f;
        }

        public void actionPerformed(ActionEvent e) {

            int resultCode = game.playCard(selectedCard);

            //Player sheds their entire hand
            if(resultCode == -1)
            {
                Player winner = game.endGame();
                frame.endGUI(winner);
            }

            //Eights card was played
            if(resultCode == 1)
            {
                changeSuitPanel.setVisible(true);
                actionPanel.setVisible(false);
            }

            //Other card
            else
            {
                updateScore();
                paintPlayPile();
                handPanel.setVisible(false);
                JOptionPane.showMessageDialog(boardPanel, "Player " + game.getCurrentPlayerID() + "'s Turn!");
                handPanel.setVisible(true);
                paintHand();
                playButton.setVisible(false);
            }
        }
    }

    //When any card button is clicked update the selected card member and check if its playable
    public class CardButtonClicked implements ActionListener
    {
        CardButton gCard;

        public CardButtonClicked(CardButton c)
        {
            gCard = c;
        }

        public void actionPerformed(ActionEvent e)
        {
            selectedCard = gCard.bGetCard();

            if(game.canPlayCard(selectedCard) || selectedCard.getFace() == Face.EIGHT)
            {
                playButton.setVisible(true);
            }
            else
            {
                playButton.setVisible(false);
            }
        }
    }

    public class SuitButtonClicked implements ActionListener
    {
        //Needed to perform actions on the frame
        Suit suit;

        public SuitButtonClicked(Suit s)
        {
            suit = s;
        }

        public void actionPerformed(ActionEvent e)
        {
            game.changeSuit(suit);
            updateScore();
            paintPlayPile();
            handPanel.setVisible(false);
            JOptionPane.showMessageDialog(boardPanel, "Player " + game.getCurrentPlayerID() + "'s Turn!");
            actionPanel.setVisible(true);
            handPanel.setVisible(true);
            paintHand();
            playButton.setVisible(false);
            changeSuitPanel.setVisible(false);
        }
    }

    //Updates players scores in their JLabels at the top of the screen
    public void updateScore()
    {
        score1.setText("Player 1's Count: " + players.get(0).getHandSize());
        score2.setText("Player 2's Count: " + players.get(1).getHandSize());
        score3.setText("Player 3's Count: " + players.get(2).getHandSize());
        score4.setText("Player 4's Count: " + players.get(3).getHandSize());
    }

    public void endGUI(Player winner)
    {
        String endMsg = "Player " + winner.getID() + " has won! Play again?";
        int a=JOptionPane.showConfirmDialog(boardPanel,endMsg);

        if(a == JOptionPane.YES_OPTION)
        {
            initialize();
        }
        else
        {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

}
