package AvionskiSaobracaj;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.QuadCurve2D;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.net.spi.InetAddressResolver;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;





public class Main {

	private ArrayList<AvioLet> nizLetova;
	private Frame frame;
	private Dialog frame2;
	private List tabelaList;
	private List tabelaLetovi;
	private Dialog letDialog;
	private static Main instanceMain;
	private Timer timer;
	private Timer timer2;
	private Mapa mapa;
	private boolean treperi;
	public static Main GetInstance()
	{
		if(instanceMain==null)
		{
			instanceMain=new Main();
		}
		return instanceMain;
	}
	private Main() {
		// TODO Auto-generated constructor stub
		frame = new Frame("Avionski saobracaj");
		frame.setSize(1280,720);
		frame2 = new Dialog(frame,"Unos aerodroma",true);
		frame2.setSize(260,310);
		frame2.setResizable(false);
		nizLetova=new ArrayList<AvioLet>();
		tabelaList = new List(10,true);
		tabelaLetovi =new List(10);
		letDialog = new Dialog(frame,"Unesi let",true);
		letDialog.setSize(260,310);
		letDialog.setResizable(false);
		mapa = new Mapa();
		treperi=false;
	}
	//Ispisuje gresku u poseban dialog
	public void greska(String s)
	{
		Dialog errorDialog=new Dialog(frame,"Greska",true);
		errorDialog.setSize(500,200);
		Label errorLabel=new Label(s,Label.CENTER);
		errorDialog.add(errorLabel,BorderLayout.CENTER);
		errorDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				errorDialog.setVisible(false);
				timer.cancel();
				Main.GetInstance().time();
			}
		});
		errorDialog.setLocationRelativeTo(frame);
		errorDialog.setVisible(true);
		
	}
	//Kreira frame 
	public void NapraviFrame()
	{
		frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);	
		}
		});
		tabelaList.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					Aerodrom.getNizAerodrom().get((int)e.getItem()).changePrikazan(true);
				}
				else if(e.getStateChange()==ItemEvent.DESELECTED) {
					Aerodrom.getNizAerodrom().get((int)e.getItem()).changePrikazan(false);
				}
				//System.out.println((int)e.getItem());
				mapa.repaint();
			}
		});
		frame.addMouseListener(null);
		MenuBar trakaMenuBar = new  MenuBar();
		frame.setMenuBar(trakaMenuBar);
		Menu dodajMenu =new Menu("Aerodrom");
		dodajMenu.add("Dodaj aerodrom sa tastature");
		dodajMenu.add("Dodaj aerodrome iz fajla");
		dodajMenu.add("Sacuvaj aerodrome u fajl");
		dodajMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.cancel();
				//mapa.repaint();
				Main.GetInstance().time();
				if(e.getActionCommand().equals("Dodaj aerodrom sa tastature"))
				{
					frame2.setVisible(true);
				}
				else if(e.getActionCommand().equals("Sacuvaj aerodrome u fajl"))
				{
					//TODO
					FileDialog fDialog = new FileDialog(frame,"Izaberi lokaciju fajla",FileDialog.SAVE);
					fDialog.setFile("*.csv");
					fDialog.setVisible(true);
					String filename=fDialog.getFile();
					String directory= fDialog.getDirectory();
					if(filename!=null && directory!=null)
					{
						File fajl=new File(directory,filename);
						try {
							FileWriter fw=new FileWriter(fajl);
							for(int i=0;i<Aerodrom.getNizAerodrom().size();i++)
							{
								fw.write(Aerodrom.getNizAerodrom().get(i).toString()+"\n");
							}
							fw.close();
						} catch (Exception e2) {
							// TODO: handle exception
							Main.GetInstance().greska("Doslo je do greske prilikom pisanja fajla probaj drugi naziv");
						}
					
					}
				}
				else {
					// napravi za fajl
					FileDialog citac = new FileDialog(frame,"Izaberi csv fajl",FileDialog.LOAD);
					citac.setFile("*.csv");
					citac.setVisible(true);
					String filename=citac.getFile();
					String directory= citac.getDirectory();
					if(filename!=null && directory!=null)
					{
						File fajl=new File(directory,filename);
						try (Scanner reader = new Scanner(fajl)){
							while(reader.hasNextLine())
							{
								String line = reader.nextLine();
								new Aerodrom(line);
								tabelaList.add(Aerodrom.getNizAerodrom().getLast().toString());
								tabelaList.select(tabelaList.getItemCount()-1);
							}
						}
						catch (Exception e2) {
							// TODO: handle exception
							Main.GetInstance().greska(e2.getMessage());
						}
					}
				}
				mapa.repaint();
			}
		});
		trakaMenuBar.add(dodajMenu);
		Menu dodajletMenu=new Menu("Letovi");
		dodajletMenu.add("Dodaj let sa tastature");
		dodajletMenu.add("Dodaj letove iz fajla");
		dodajletMenu.add("Sacuvaj letove u fajl");
		trakaMenuBar.add(dodajletMenu);
		dodajletMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timer.cancel();
				Main.GetInstance().time();
				if(e.getActionCommand().equals("Dodaj let sa tastature"))
				{
					letDialog.setVisible(true);
				}
				else if(e.getActionCommand().equals("Sacuvaj letove u fajl"))
				{
					FileDialog fDialog = new FileDialog(frame,"Izaberi lokaciju fajla",FileDialog.SAVE);
					fDialog.setFile("*.csv");
					fDialog.setVisible(true);
					String filename=fDialog.getFile();
					String directory= fDialog.getDirectory();
					if(filename!=null && directory!=null)
					{
						File fajl=new File(directory,filename);
						try {
							FileWriter fw=new FileWriter(fajl);
							for(int i=0;i<nizLetova.size();i++)
							{
								fw.write(nizLetova.get(i).toString()+"\n");
							}
							fw.close();
						} catch (Exception e2) {
							// TODO: handle exception
							Main.GetInstance().greska("Doslo je do greske prilikom pisanja fajla probaj drugi naziv");
						}
					
					}
				}
				else
				{ // citanje iz fajla
					FileDialog citac = new FileDialog(frame,"Izaberi csv fajl",FileDialog.LOAD);
					citac.setFile("*.csv");
					citac.setVisible(true);
					String filename=citac.getFile();
					String directory= citac.getDirectory();
					if(filename!=null && directory!=null)
					{
						File fajl=new File(directory,filename);
						try (Scanner reader = new Scanner(fajl)){
							while(reader.hasNextLine())
							{
								String line = reader.nextLine();
								nizLetova.add(new AvioLet(line));
								tabelaLetovi.add(nizLetova.getLast().toString());
							}
						}
						catch (Exception e2) {
							// TODO: handle exception
							Main.GetInstance().greska(e2.getMessage());
						}
					}
				}
			}
		});
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		
		frame.add(tabelaList,BorderLayout.WEST);
		frame.add(tabelaLetovi,BorderLayout.EAST);
		}
	public void NapraviDialog()
	{
		frame2.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				timer.cancel();
				Main.GetInstance().time();
				frame2.setVisible(false);
			}
		});
		TextField nazivTextField=new TextField(30);
		Label nazivLabel = new Label("Naziv:");
		TextField kodField = new TextField(30);
		Label kodLabel =new Label("Kod:");
		TextField xField = new TextField(30);
		Label xLabel = new Label("x:");
		TextField yField = new TextField(30);
		Label yLabel =new Label("y:");
		frame2.setLayout(new FlowLayout(30));
		frame2.add(nazivLabel);
		frame2.add(nazivTextField);
		frame2.add(kodLabel);
		frame2.add(kodField);
		frame2.add(xLabel);
		frame2.add(xField);
		frame2.add(yLabel);
		frame2.add(yField);
		Button potvrdiButton = new Button("Potvrdi");
		frame2.add(potvrdiButton);
		potvrdiButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timer.cancel();
				Main.GetInstance().time();
				try{
					int x=Integer.parseInt(xField.getText());
					int y=Integer.parseInt(yField.getText());
					new Aerodrom(nazivTextField.getText(), kodField.getText(), x, y);
					tabelaList.add(Aerodrom.getNizAerodrom().getLast().toString());
					tabelaList.select(tabelaList.getItemCount()-1);
					nazivTextField.setText(null);
					kodField.setText(null);
					xField.setText(null);
					yField.setText(null);
				}
				catch(NumberFormatException err)
				{
					Main.GetInstance().greska("Polja za broj x i y ne smeju da budu prazna");
				}
				catch (Exception err) {
					//  greska se vec obradjuje u konstruktoru
				}
				
			}
		});
	}
	//Pravi dialog za aviolet
	public void NapraviAvioLetDialog()
	{
		letDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				timer.cancel();
				Main.GetInstance().time();
				letDialog.setVisible(false);
			}
		});
		TextField pocAerodromField=new TextField(30);
		Label pocAerodromLabel = new Label("Pocetni aerodromski kod:");
		TextField krajAerodromField=new TextField(30);
		Label krajAerodromLabel=new Label("Krajnji aerodromski kod:");
		TextField vremepoletanjaField=new TextField(30);
		Label vremepoletanjaLabel=new Label("Vreme poletanja:");
		TextField duzinaletaField=new TextField(30);
		Label duinaletaLabel=new Label("Duzina leta:");
		letDialog.add(pocAerodromLabel);
		letDialog.add(pocAerodromField);
		letDialog.add(krajAerodromLabel);
		letDialog.add(krajAerodromField);
		letDialog.add(vremepoletanjaLabel);
		letDialog.add(vremepoletanjaField);
		letDialog.add(duinaletaLabel);
		letDialog.add(duzinaletaField);
		letDialog.setLayout(new FlowLayout());
		Button potvrdiButton= new Button("Potrvrdi");
		potvrdiButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timer.cancel();
				Main.GetInstance().time();
				try {
					nizLetova.add(new AvioLet(pocAerodromField.getText(), krajAerodromField.getText(), vremepoletanjaField.getText(), duzinaletaField.getText()));
					tabelaLetovi.add(nizLetova.getLast().toString());
					pocAerodromField.setText(null);
					krajAerodromField.setText(null);
					vremepoletanjaField.setText(null);
					duzinaletaField.setText(null);
				} catch (Exception e2) {
					// TODO: handle exception
					Main.GetInstance().greska(e2.getMessage());
					
				}
			}
		});
		letDialog.add(potvrdiButton);
	}
	// Dialog za neaktivnost
	private void timeispis(String s)
	{
		Dialog errorDialog=new Dialog(frame,"Greska",true);
		errorDialog.setSize(600,200);
		Label errorLabel=new Label(s,Label.CENTER);
		errorDialog.add(errorLabel,BorderLayout.CENTER);
		errorDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				errorDialog.setVisible(false);
				timer.cancel();
				timer2.cancel();
				Main.GetInstance().time();
			}
		});
		errorDialog.setLocationRelativeTo(frame);
		errorDialog.setVisible(true);
		
	}
	//Pali tajmer
	public  void time() {
		timer=new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timer2=new Timer();
				timer2.schedule(new TimerTask() {
					
					@Override
					public void run() {
						System.exit(0);
					}
				}, 5000);
				Main.GetInstance().timeispis("Neaktivnost je detektovana program se gasi za 5 sekundi klikni x da bi otkazao");
			}
		}, 55000);
		
	}
	public Timer getTimer()
	{
		return timer;
	}
	public boolean getTreperi()
	{
		return treperi;
	}
	//Crta aerodrome na maoi
	public void nacrtaj_mapu()
	{
		frame.add(mapa,BorderLayout.CENTER);
		frame.validate();
		frame.repaint();
		Timer timer3=new Timer();
		timer3.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(Aerodrom.isAnyoneSelected())
				{
					timer.cancel();
					Main.GetInstance().time();
				}
				treperi=!treperi;
				mapa.repaint();
			}
		}, 0,1000);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main.GetInstance().time();
		Main.GetInstance().NapraviFrame();
		Main.GetInstance().NapraviDialog();
		Main.GetInstance().NapraviAvioLetDialog();
		Main.GetInstance().nacrtaj_mapu();
	}

}
