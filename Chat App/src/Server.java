import javax.swing.*;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.text.*;
import java.io.*;
public class Server  implements ActionListener {
    private JTextField text1;
     private JPanel a1;
     static Box vertical = Box.createVerticalBox();

    static JFrame f =new JFrame("Chat GUI");
    static DataOutputStream dataout;
    Server() {

        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(84, 0, 15));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        ImageIcon im1 = new ImageIcon(ClassLoader.getSystemResource("image/3.png"));
        Image im2 = im1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon im3 = new ImageIcon(im2);
        JLabel back = new JLabel(im3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);

        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        ImageIcon im4 = new ImageIcon(ClassLoader.getSystemResource("image/images.png"));
        Image im5 = im4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon im6 = new ImageIcon(im5);
        JLabel proflie = new JLabel(im6);
        proflie.setBounds(40, 10, 50, 50);
        p1.add(proflie);

        JLabel name = new JLabel("Proflie1");
        name.setBounds(110, 15, 150, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("TH SarabunPSK", Font.BOLD, 16));
        p1.add(name);

        JLabel status = new JLabel("Active");
        status.setBounds(110, 35, 150, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("TH SarabunPSK", Font.BOLD, 12));
        p1.add(status);

        JButton send = new JButton("Send");
        send.setBounds(320, 585, 111, 40);
        send.setBackground(new Color(84, 0, 15));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        f.add(send);

        text1 = new JTextField();
        text1.setBounds(5, 585, 310, 40);
        text1.setFont(new Font("Angsana New", Font.BOLD, 18));
        f.add(text1);

        text1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        });

        a1 = new JPanel();
        a1.setBounds(5, 75, 425, 500);
        f.add(a1);

        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.getContentPane().setBackground(Color.WHITE);

        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String out = text1.getText().trim();

            if (!out.isEmpty()) {

                JPanel p2 = formatLabel(out);

                a1.setLayout(new BorderLayout());

                JPanel right = new JPanel(new BorderLayout());
                right.add(p2, BorderLayout.LINE_END);
                vertical.add(right);
                vertical.add(Box.createVerticalStrut(15));

                a1.add(vertical, BorderLayout.PAGE_START);

                dataout.writeUTF(out);
                text1.setText("");

                f.repaint();
                f.invalidate();
                f.validate();

            }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setBackground(new Color(84, 0, 15));
        output.setOpaque(true);
        output.setFont(new Font("Angsana New", Font.BOLD, 18));
        output.setBorder(new EmptyBorder(10, 10, 10, 10));
        output.setForeground(Color.WHITE);

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);
        return panel;
    }

    public static void main(String[] args) {

        new Server();

        try {
            ServerSocket skt = new ServerSocket(6001);
            while(true){
                Socket s = skt.accept();
                DataInputStream data = new DataInputStream(s.getInputStream());
                dataout = new DataOutputStream(s.getOutputStream());

                while (true){
                    String msg = data.readUTF();
                    JPanel panel = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel,BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}