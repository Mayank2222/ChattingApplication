import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.DataInputStream;
import java.util.*;
import java.text.*;
import java.net.*; // For Establishing Connection
import java.io.*; // For reading the messages

public class Server implements ActionListener{

    JTextField text; //Globally declared
    JPanel a1;
    static Box vertical= Box.createVerticalBox();//For vertical layout of conversations during chat.
    static JFrame f= new JFrame();
    static DataOutputStream dout;

    Server(){ //Constructor for Frame
        f.setLayout(null);

        JPanel p1= new JPanel();
        p1.setBackground(new Color(7, 94, 84 ));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);
        // Back Button Functionality
        ImageIcon i1= new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2= i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3= new ImageIcon(i2);
        JLabel back= new JLabel(i3);
        back.setBounds(5,20,25,25);
        p1.add(back);

        back.addMouseListener(new MouseAdapter (){
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });

        //Profile Picture
        ImageIcon i4= new ImageIcon(ClassLoader.getSystemResource("icons/1.png"));
        Image i5= i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6= new ImageIcon(i5);
        JLabel profile= new JLabel(i6);
        profile.setBounds(40,10,50,50);
        p1.add(profile);

        //Video Call Icon
        ImageIcon i7= new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8= i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9= new ImageIcon(i8);
        JLabel video= new JLabel(i9);
        video.setBounds(300,20,30,30);
        p1.add(video);

        //Call Icon
        ImageIcon i10= new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11= i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i12= new ImageIcon(i11);
        JLabel phone= new JLabel(i12);
        phone.setBounds(360,20,30,30);
        p1.add(phone);


        //Morevert Icon
        ImageIcon i13= new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14= i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i15= new ImageIcon(i14);
        JLabel morevert= new JLabel(i15);
        morevert.setBounds(420,20,10,25);
        p1.add(morevert);

        //Name of Person can be written using JLabel
        JLabel name= new JLabel("Mayank");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF",Font.BOLD, 18));
        p1.add(name);

        //Activity Status: ACTIVE NOW
        JLabel status= new JLabel("Active Now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF",Font.BOLD, 14));
        p1.add(status);

        //New Frame to display Text Messages
        a1= new JPanel();
        a1.setBounds(5, 75, 440, 570);
        f.add(a1);

        //Text Dialog Box
        text= new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SARIF", Font.PLAIN,16));
        f.add(text);

        //Send Button Functionality
        JButton send= new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SARIF", Font.PLAIN,16));
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        String out= text.getText();
        JPanel p2= formatLabel(out);
        a1.setLayout(new BorderLayout());

        //Messages will align vetically on right side.
        JPanel right= new JPanel(new BorderLayout());
        right.add(p2, BorderLayout.LINE_END);
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15)); //Space between each consecutive messages.

        a1.add(vertical, BorderLayout.PAGE_START);

        try {
            dout.writeUTF(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        text.setText("");


        f.repaint();
        f.invalidate();
        f.validate();
    }

    public static JPanel formatLabel(String out){
        JPanel panel= new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output= new JLabel("<html><p style=\"width: 150px\">"+ out +"</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);

        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);

        Calendar cal= Calendar.getInstance();
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");

        JLabel time= new JLabel();
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);
        return panel;
    }

    public static void main(String[] args){
        new Server(); //Object of class

        try{
            ServerSocket skt= new ServerSocket(6001); //Providing the port number
            while (true) {
                Socket s = skt.accept(); // Accept is used to establish connection with the client
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while(true){
                //To receive messages infinitely we will use Read UTF protocol
                String msg = din.readUTF();
                //To display messages from client on server side
                JPanel panel = formatLabel(msg);
                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START); // TO display received messages on the left.
                vertical.add(left);
                f.validate();
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}