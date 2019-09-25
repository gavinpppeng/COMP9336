import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import sound.singlesound;
import sound.dualsound;
import sound.codingsound;

public class GUI extends JFrame {

    private JPanel jp1, jp2, jp3, jp4;
    private JLabel jlb1, jlb12, jlb2, jlb3, jlb4;
    private static JRadioButton jrb1;
	private JRadioButton jrb2;
    private static JComboBox jcb;
    private static JComboBox jcbtask3;
	private static JButton jb1, jb2, jb3, jb4;
    private static JTextField jtf1, jtf4;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        GUI d1 = new GUI();
        jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					int freq = Integer.valueOf(jtf1.getText());
					singlesound.tone(freq,1000);
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			}
		});
        
        jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int freq_choice = Integer.valueOf((String) jcb.getSelectedItem());
				DefaultButtonModel model = (DefaultButtonModel) jrb1.getModel();
				
				if (model.getGroup().isSelected(model))
				{
					 //Audible: 1:400-500, 2:500-600, 3:600-700, 4:700-800, 5:800-900, 6:900-1000, 7:1000-1100,
	                // 8:1100-1200, 9:1200-1300
					if (freq_choice == 1) {
						try {
							singlesound.tone(450, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 2) {
						try {
							singlesound.tone(550, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 3) {
						try {
							singlesound.tone(650, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 4) {
						try {
							singlesound.tone(750, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 5) {
						try {
							singlesound.tone(850, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 6) {
						try {
							singlesound.tone(950, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 7) {
						try {
							singlesound.tone(1050, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 8) {
						try {
							singlesound.tone(1150, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 9) {
						try {
							singlesound.tone(1250, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
				else
				{ 
					//Inaudible: 1:13100-13200, 2:13200-13300, 3:13300-13400, 4:13400-13500, 5:13500-13600, 6:13600-13700, 7:13700-13800,
                    //8:13800-13900, 9:13900-14000
					if (freq_choice == 1) {
						try {
							singlesound.tone(13150, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 2) {
						try {
							singlesound.tone(13250, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 3) {
						try {
							singlesound.tone(13350, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 4) {
						try {
							singlesound.tone(13450, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 5) {
						try {
							singlesound.tone(13550, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 6) {
						try {
							singlesound.tone(13650, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 7) {
						try {
							singlesound.tone(13750, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 8) {
						try {
							singlesound.tone(13850, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(freq_choice == 9) {
						try {
							singlesound.tone(13950, 1000);
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					}
               
				System.out.println(freq_choice); 
			}
		});
        
        jb3.addActionListener(new ActionListener() {
 			@Override
 			public void actionPerformed(ActionEvent e) {
 				int freq_choice_task3 = Integer.valueOf((String) jcbtask3.getSelectedItem());
 				if(freq_choice_task3 == 1) {
 					try {
						dualsound.tone(697, 1209, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 2) {
 					try {
						dualsound.tone(697, 1336, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 3) {
 					try {
						dualsound.tone(697, 1477, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 4) {
 					try {
						dualsound.tone(770, 1209, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 5) {
 					try {
						dualsound.tone(770, 1336, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 6) {
 					try {
						dualsound.tone(770, 1477, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 7) {
 					try {
						dualsound.tone(852, 1209, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 8) {
 					try {
						dualsound.tone(852, 1336, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 				else if(freq_choice_task3 == 9) {
 					try {
						dualsound.tone(852, 1477, 1000);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				}
 			}
 			});
        
        jb4.addActionListener(new ActionListener() {
 			@Override
 			public void actionPerformed(ActionEvent e) {
 				String msg = jtf4.getText();
 				try {
					codingsound.Hammingcoding(msg);
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
 				}
 			});
        }

    
    public GUI() {

        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();

        jlb1 = new JLabel("Task1: input a transmitting frequency:");
        jlb12 = new JLabel("Range:300Hz-20000Hz");
        jb1 = new JButton("Send");
        jtf1 = new JTextField(10);
        //jpf1 = new JPasswordField(10);
        
        jlb2 = new JLabel("Task2: input a number:"); 
        jb2 = new JButton("Send");
        jcb = new JComboBox();
        ButtonGroup grp = new ButtonGroup();
        jrb1 = new JRadioButton("Audible", true);
        jrb2 = new JRadioButton("Inaudible");
        
        jlb3 = new JLabel("Task3: input a number (DTMF):");
        jcbtask3 = new JComboBox();
        jb3 = new JButton("Send");
        
        jlb4 = new JLabel("Task4/5: input a text:");
        jtf4 = new JTextField(20);
        jb4 = new JButton("Send");
        
        
        this.setLayout(new GridLayout(4,1,5,20));
        

        // Add the all module
        
        // Task 1
        // Add the panel
        jp1.add(jlb1);
        // Add the TextField
        jp1.add(jtf1);
        // Add the Label
        jp1.add(jlb12);
        // Add the button
        jp1.add(jb1);
        
        //Task 2
        jp2.add(jlb2);
        grp.add(jrb1);
        grp.add(jrb2);
        jp2.add(jrb1);
        jp2.add(jrb2);
        jp2.add(jcb);
        jcb.addItem("1");
        jcb.addItem("2");
        jcb.addItem("3");
        jcb.addItem("4");
        jcb.addItem("5");
        jcb.addItem("6");
        jcb.addItem("7");
        jcb.addItem("8");
        jcb.addItem("9");
        jp2.add(jb2);

        //Task 3
        jp3.add(jlb3);
        jp3.add(jcbtask3);
        jcbtask3.addItem("1");
        jcbtask3.addItem("2");
        jcbtask3.addItem("3");
        jcbtask3.addItem("4");
        jcbtask3.addItem("5");
        jcbtask3.addItem("6");
        jcbtask3.addItem("7");
        jcbtask3.addItem("8");
        jcbtask3.addItem("9");
        jp3.add(jb3);
        
        //Task 4
        jp4.add(jlb4);
        jp4.add(jtf4);
        jp4.add(jb4);

        // Add to the JFrame
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);

        this.pack();
        this.setLocation(300, 200);
        this.setTitle("9336 Assignment transmitter");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
