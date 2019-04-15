import javax.swing.*;
import javax.swing.ImageIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel playedCard;

    private ArrayList<JButton> gHand1;
    private ArrayList<JButton> gHand2;
    private ArrayList<JButton> gHand3;
    private ArrayList<JButton> gHand4;

    private Card selectedCard;


    public EightsFrame()
    {
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setContentPane(boardPanel);

        //Frame deletes when window is closed
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Construct necessary data for game
        EightsGame game = new EightsGame();
        gHand1 = new ArrayList<>();
        gHand2 = new ArrayList<>();
        gHand3 = new ArrayList<>();
        gHand4 = new ArrayList<>();

        //Get list of players
        //TODO Change implementation to keep players as private members of EightsGame
        List<EightsPlayer> players = game.getPlayers();

        Hand pHand1 = players.get(0).getHand();
        Hand pHand2 = players.get(1).getHand();
        Hand pHand3 = players.get(2).getHand();
        Hand pHand4 = players.get(3).getHand();

        //Populate graphical hand of players initially
        for(int i = 0; i < 7; i++)
        {
            gHand1.add(makeGCardPlayer(pHand1.getCard(i)));
            gHand2.add(makeGCardPlayer(pHand2.getCard(i)));
            gHand3.add(makeGCardPlayer(pHand3.getCard(i)));
            gHand4.add(makeGCardPlayer(pHand4.getCard(i)));
        }

        paintHand(players.get(0));

        drawButton.addActionListener(new DrawButtonClicked());

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

        //String path = "PNG-cards-82X164/" + card.getFace().getValue() + "_of_" + card.getSuit().getValue() + ".png";

        //ImageIcon cardImage = new ImageIcon(path);

        String title = "<html>" + card.getSuit().getValue() + "<br />" + card.getFace().getValue() + "</html>";

        CardButton tmp = new CardButton(card);

        tmp.setText(title);

        //tmp.setIcon(cardImage);

        tmp.setPreferredSize(new Dimension(82,164));

        return tmp;

    }

    //TODO make this common to all systems
    private JButton makeGCardCenter(Card card)
    {

        String path = "/resources/PNG-cards";

        ImageIcon cardImage = new ImageIcon(path + "/" + card.getFace() + "_of_" + card.getSuit() + ".png");

        JButton tmp = new JButton(cardImage);

        tmp.setPreferredSize(new Dimension(100,200));

        return tmp;

    }

    //Given a player, paints their hand of card buttons on the proper panel
    private void paintHand(EightsPlayer player)
    {
        switch(player.getID())
        {
            case "1":
            {
                for(int i = 0; i < gHand1.size(); i++)
                {
                    gHand1.get(i).setVisible(true);
                    handPanel.add(gHand1.get(i));
                }
                break;
            }

            case "2":
            {
                for(int i = 0; i < gHand1.size(); i++)
                {
                    gHand2.get(i).setVisible(true);
                    handPanel.add(gHand1.get(i));
                }
                break;
            }

            case "3":
            {
                for(int i = 0; i < gHand1.size(); i++)
                {
                    gHand3.get(i).setVisible(true);
                    handPanel.add(gHand1.get(i));
                }
                break;
            }

            case "4":
            {
                for(int i = 0; i < gHand1.size(); i++)
                {
                    gHand4.get(i).setVisible(true);
                    handPanel.add(gHand1.get(i));
                }
                break;
            }
        }
    }

    //Clears the handPanel of all card components
    private void clearHand()
    {
        handPanel.removeAll();
        handPanel.revalidate();
        handPanel.repaint();
    }

    public class DrawButtonClicked implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            clearHand();
        }
    }

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
        }
    }

}
