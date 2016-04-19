import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.*;

public class Frame extends Thread{
	private Boolean mark = true;
	JFrame frame = new JFrame("FIFA2016");
	JPanel jpTop=new JPanel( );//flow layout
	JPanel jpMiddle = new JPanel();
	JPanel jpButtom = new JPanel();
	JLabel Ger = new JLabel("German Player:  ");
	JLabel Ita = new JLabel("Italian player:  ");
	JLabel numofG = new JLabel("0");//the Gpeople in the station
	JLabel numofI = new JLabel("0");//the Ipeople in the station
	JLabel image1 = new JLabel();//image german
	JLabel image2 = new JLabel();//image italian
	JTextArea jc = new JTextArea();
	JScrollPane js = new JScrollPane(jc);//need parameter maybe about the result windows
	JButton button = new JButton("Run");//the button for Run
	JButton button_suspend = new JButton("Pause");//The Button for 
	text1 w = new text1();
	
	//update the number of the German and Italian still in the station 
	public void run () {
        while (true) {
                if(w.counterG==1)
                	image1.setIcon(new ImageIcon("src/germanflag1.jpg"));
                else if(w.counterG==2)
                	image1.setIcon(new ImageIcon("src/germanflag2.jpg"));
                else if(w.counterG==3)
                	image1.setIcon(new ImageIcon("src/germanflag3.jpg"));
                else image1.setIcon(new ImageIcon("src/blank.jpg"));
                
                if(w.counterI==1)
                	image2.setIcon(new ImageIcon("src/italian1.jpg"));
                else if(w.counterI==2)
                	image2.setIcon(new ImageIcon("src/italian2.jpg"));
                else if(w.counterI==3)
                	image2.setIcon(new ImageIcon("src/italian3.jpg"));
                else image2.setIcon(new ImageIcon("src/blank.jpg"));
                numofG.setText(w.counterG + "");
                numofI.setText(w.counterI + "");
                try {
                        sleep(10);
                } catch (InterruptedException ie) {}
        }
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				new Frame();
			}
		});
	}
	
	public Frame(){
		//set frame layout
		frame.setLayout(new BorderLayout());
		//set top layout
		 jpTop.setLayout(new BoxLayout(jpTop, BoxLayout.Y_AXIS));
		 JPanel lbpanel = new JPanel();
		 lbpanel.setLayout(new BoxLayout(lbpanel , BoxLayout.X_AXIS));
		 lbpanel.add(Ger);lbpanel.add(numofG);
		 lbpanel.add(Box.createHorizontalStrut(150));// Inviable strut between label
		 lbpanel.add(Ita);lbpanel.add(numofI);
		 JPanel contain_image = new JPanel();
		 contain_image.setLayout(new BoxLayout(contain_image , BoxLayout.X_AXIS));
		 contain_image.add(image1);
		 contain_image.add(Box.createHorizontalStrut(120));
		 contain_image.add(image2);
		 jpTop.add(Box.createVerticalStrut(20));
		 jpTop.add(lbpanel);
		 jpTop.add(contain_image);				// image
		 jpTop.add(Box.createVerticalStrut(10));
		//set middle layout
		 Font fjc = new Font("Century Gothic",Font.BOLD,20);
		 jc.setFont(fjc);
		 MyPrintStream mps = new MyPrintStream(System.out, jc);
		 System.setOut(mps);  
		 System.setErr(mps);
		 js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		 jpMiddle.setLayout(new BoxLayout(jpMiddle, BoxLayout.Y_AXIS));
		 jpMiddle.add(Box.createVerticalStrut(10));
		 jpMiddle.add(js);
		 jpMiddle.add(Box.createVerticalStrut(10));
		//set bottom layout
		 jpButtom.setLayout(new BoxLayout(jpButtom, BoxLayout.Y_AXIS));
		 JPanel buttonpanel = new JPanel();
		 buttonpanel.setLayout(new BoxLayout(buttonpanel , BoxLayout.X_AXIS));
		 buttonpanel.add(button);
		 buttonpanel.add(Box.createHorizontalStrut(200));// Inviable strut between label
		 buttonpanel.add(button_suspend);
		 jpButtom.add(Box.createVerticalStrut(20));
		 jpButtom.add(buttonpanel);
		 jpButtom.add(Box.createVerticalStrut(20));
		 
		//frame
		frame.add(jpTop,BorderLayout.NORTH);
		frame.add(jpMiddle,BorderLayout.CENTER);
		frame.add(jpButtom,BorderLayout.SOUTH);
		frame.setSize(500,600);
		frame.setLocation(400, 100);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		

		//create the run button
		Font f = new Font("Times New Roman",Font.BOLD,20);
		button.addActionListener(w.construct());
		button.setFont(f);
		button_suspend.setFont(f);
		button_suspend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mark){
					button_suspend.setText("Pause");
					mark = false;
					w.main_mark=false;
				}else{
					mark = true;
					w.main_mark=true;
					button_suspend.setText("Resume");
				}
				//while(mark){}
			}
		});
		
		start();//start the thread for number of G and I
	}
	
	//output the console message

public class MyPrintStream extends PrintStream {  
 
private JTextArea text;
private StringBuffer sb = new StringBuffer();
   
   public MyPrintStream(OutputStream out, JTextArea text) {  
       super(out);  
        this.text = text;  
   }
  
    public void write(byte[] buf, int off, int len) {  
         final String message = new String(buf, off, len);   
         SwingUtilities.invokeLater(new Runnable(){
         public void run(){
          sb.append(message);
          text.setText(sb.toString());
         }
      });
   }
  }
}
