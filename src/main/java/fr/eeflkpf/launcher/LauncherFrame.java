package fr.eeflkpf.launcher;

import fr.eeflkpf.launcher.launch.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LauncherFrame extends JFrame implements ActionListener {

    private static LauncherFrame instance;

    private JLabel titre;
    private JTextField pseudo;
    private JLabel pseudo2;
    private JPasswordField mdp;
    private JLabel mdp2;
    private JButton jouer;
    private JButton quitter;
    private JProgressBar pb;

    private String username = null;
    private String accessToken = null;
    private String id = null;

    public LauncherFrame() {

        this.setTitle("EEFLKPF 1.12.2");
        this.setSize(850, 500);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        try{
            if(new File(Launcher.EEFLK_DIR.getPath()+"/custombacks/back.png").exists()) {
                this.setContentPane(new JLabel(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/custombacks/back.png")));
            }else if (new File(Launcher.EEFLK_DIR.getPath() + "/custombacks/back.gif").exists()) {
                this.setContentPane(new JLabel(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/custombacks/back.gif")));
            }else if (new File(Launcher.EEFLK_DIR.getPath() + "/images/back.png").exists()) {
                this.setContentPane(new JLabel(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/images/back.png")));
            }else if (new File(Launcher.EEFLK_DIR.getPath() + "/images/back.gif").exists()) {
                this.setContentPane(new JLabel(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/images/back.gif")));
            } else {
                this.setContentPane(new JLabel(new ImageIcon(getClass().getResource("/images/background/noBack.gif"))));
            }
        }catch(Exception e) {

        }

        this.getContentPane().setBackground(Color.darkGray);

        titre = new JLabel("EEFLKPF", SwingConstants.CENTER);
        titre.setForeground(Color.RED);
        titre.setFont(titre.getFont().deriveFont(60f));
        titre.setBounds(0, 20, 850, 100);
        this.add(titre);

        pseudo2 = new JLabel("Pseudo", SwingConstants.CENTER);
        pseudo2.setForeground(Color.RED);
        pseudo2.setFont(pseudo2.getFont().deriveFont(18f));
        pseudo2.setBounds(253, 237, 125, 15);

        this.add(pseudo2);

        mdp2 = new JLabel("Mot De Passe", SwingConstants.CENTER);
        mdp2.setForeground(new Color(132,27,12));
        mdp2.setFont(mdp2.getFont().deriveFont(18f));
        mdp2.setBounds(227, 283, 125, 15);

        this.add(mdp2);

        pseudo = new JTextField("");
        pseudo.setBounds(350, 237, 150, 20);
        pseudo.setOpaque(true);

        this.add(pseudo);

        mdp = new JPasswordField("");
        mdp.setBounds(350, 283, 150, 20);
        mdp.setBackground(Color.GRAY);
        mdp.setForeground(Color.GRAY);
        mdp.setToolTipText("Coming Soon...");
        mdp.setEnabled(false);
        mdp.setOpaque(true);

        this.add(mdp);


        jouer = new JButton("");
        jouer.addActionListener(this);
        jouer.setBounds(350, 337, 151, 75);

        try {
            if(new File(Launcher.EEFLK_DIR.getPath()+"/custombacks/play.png").exists()) {
                jouer.setIcon(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/custombacks/play.png"));
            } else if(new File(Launcher.EEFLK_DIR.getPath()+"/custombacks/play.gif").exists()) {
                jouer.setIcon(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/custombacks/play.gif"));
            } else {
                jouer.setIcon(new ImageIcon(getClass().getResource("/images/background/play.png")));
            }
        } catch (Exception e) {

        }

        jouer.setBackground(Color.darkGray);
        jouer.setOpaque(false);

        this.add(jouer);

        quitter = new JButton("");
        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(JOptionPane.showConfirmDialog(LauncherFrame.this, "Voulez vous vraiment quitter?", "Exit?", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

                    Launcher.NormalExit();

                }

            }
        });
        quitter.setBounds(812, 8, 26, 27);

        try {
            if(new File(Launcher.EEFLK_DIR.getPath()+"/custombacks/exit.png").exists()) {
                quitter.setIcon(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/custombacks/exit.png"));
            } else if(new File(Launcher.EEFLK_DIR.getPath()+"/custombacks/exit.gif").exists()) {
                quitter.setIcon(new ImageIcon(Launcher.EEFLK_DIR.getPath() + "/custombacks/exit.gif"));
            } else {
                quitter.setIcon(new ImageIcon(getClass().getResource("/images/background/exit.png")));
            }
        } catch (Exception e) {

        }

        quitter.setBackground(Color.darkGray);
        quitter.setOpaque(false);

        this.add(quitter);

        pb = new JProgressBar();
        pb.setStringPainted(true);
        pb.setBounds(0, 480, 850, 20);
        pb.setOpaque(false);

        this.add(pb);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread t = new Thread() {
            @Override
            public void run() {
                if (pseudo.getText().replaceAll(" ", "").replaceAll(" ", "").length() != 0) {
                    pseudo.setEnabled(false);
                    mdp.setEnabled(false);
                    jouer.setEnabled(false);
                    quitter.setEnabled(false);

                    try {
                        Launcher.auth(pseudo.getText());
                    } catch (Exception e) {
                        pseudo.setEnabled(true);
                        mdp.setEnabled(true);
                        jouer.setEnabled(true);
                        quitter.setEnabled(true);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(LauncherFrame.this, "Impossible de se connecter : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }else{

                    JOptionPane.showMessageDialog(LauncherFrame.this, "Veuillez mettre un pseudo contenant au moins un character qui ne soit pas une espace (ou une espace spéciale)", "Erreur", JOptionPane.ERROR_MESSAGE);

                }
            }
        };
        t.start();
    }

    public static void main(String[] args) {
        // Astuce pour avoir le style visuel du systeme hôte
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance = new LauncherFrame();
    }

    // Retourne l'instance de LauncherFrame
    public static LauncherFrame getInstance() {
        return instance;
    }

    // Retourne l'instance de notre progress bar
    public JProgressBar getProgressBar() {
        return pb;
    }
}
