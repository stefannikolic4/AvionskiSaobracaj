/**
 * 
 */
package AvionskiSaobracaj;

import java.awt.Button;
import java.awt.Desktop.Action;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextField;
import java.awt.desktop.OpenFilesEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.spi.InetAddressResolver.LookupPolicy;
import java.security.Identity;
import java.util.ArrayList;
import java.util.NoSuchElementException;





/**
 * 
 */
public class Aerodrom {

	/**
	 * 
	 */
	private String naziv;
	private String kod;
	private int x;
	private int y;
	private boolean selected;
	private boolean prikazan;
	private static ArrayList<Aerodrom> listaAerodroma;
	public static ArrayList<Aerodrom> getNizAerodrom()
	{
		if(listaAerodroma==null)
		{
			listaAerodroma=new ArrayList<Aerodrom>();
		}
		return listaAerodroma;
	}
	// Dodaje aerodrom u niz i proverava kodove
	public void DodajProveri() throws IllegalArgumentException
	{
		for(int i =0;i<getNizAerodrom().size();i++)
		{
			if(this.kod.equals(getNizAerodrom().get(i).kod))
			{
				throw new IllegalArgumentException("Kod vec postoji u bazi");
			}
		}
		getNizAerodrom().add(this);
		
	}
	//Trazi aerodrom sa datim kodom
	public static Aerodrom find(String s) throws NoSuchElementException
	{
		for(int i =0;i<getNizAerodrom().size();i++)
		{
			if(s.equals(getNizAerodrom().get(i).kod))
			{
				return getNizAerodrom().get(i);
			}
		}
		throw new NoSuchElementException("Kod ne postoji u bazi aerodroma. Proveri da li je kod dobro unet ili dodaj novi aerodrom");
		//return null;
	}
	//Konstruktor 
	public Aerodrom(String naziv,String kodString ,int x,int y) throws IllegalArgumentException{
		// TODO 
		try {
			this.naziv=naziv;
			this.kod=kodString;
			this.x=x;
			this.y=y;
			selected=false;
			prikazan=true;
			if(naziv==null || naziv.length()==0 ) throw new IllegalArgumentException("Naziv ne sme da bude prazan");
			if(kodString== null || kodString.length()==0) throw new IllegalArgumentException("Kod ne sme da bude prazan");
			if(kodString.equals(kodString.toUpperCase())==false) throw new IllegalArgumentException("Kod mora da sadrzi sva velika slova");
			if(kod.length()!=3) throw new IllegalArgumentException("Duzina koda mora da ima 3 karaktera");
			DodajProveri();
			if(x<-90 || x>90) throw new IllegalArgumentException("x mora da bude izmedju -90 i 90");
			if(y<-90 || y>90) throw new IllegalArgumentException("y mora da bude izmedju -90 i 90");
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			Main.GetInstance().greska(e.getMessage());
			throw new IllegalArgumentException();
		}
		
	}
	//Konstruktor za unos iz fajla
	public Aerodrom(String line) throws IllegalArgumentException
	{
		try {
			String[] nizlineStrings=line.split(",");
			if(nizlineStrings.length!=4)
			{
				throw new IllegalArgumentException("Format linije fajla nije dobar promeni fajl ili probaj drugi");
			}
			int x=Integer.parseInt(nizlineStrings[2]);
			int y=Integer.parseInt(nizlineStrings[3]);
			this.naziv=nizlineStrings[0];
			this.kod=nizlineStrings[1];
			this.x=x;
			this.y=y;
			selected=false;
			prikazan=true;
			if(naziv==null || naziv.length()==0 ) throw new IllegalArgumentException("Naziv ne sme da bude prazan(proveri fajl)");
			if(kod== null || kod.length()==0) throw new IllegalArgumentException("Kod ne sme da bude prazan(proveri fajl)");
			if(kod.equals(kod.toUpperCase())==false) throw new IllegalArgumentException("Kod mora da sadrzi sva velika slova(proveri fajl)");
			if(kod.length()!=3) throw new IllegalArgumentException("Duzina koda mora da ima 3 karaktera(proveri fajl)");
			DodajProveri();
			if(x<-90 || x>90) throw new IllegalArgumentException("x mora da bude izmedju -90 i 90(proveri fajl)");
			if(y<-90 || y>90) throw new IllegalArgumentException("y mora da bude izmedju -90 i 90(proveri fajl)");

		}
		catch (NumberFormatException e) {
			// TODO: handle exception
			throw new IllegalArgumentException("Kordinate nisu brojevi proveri fajl");
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	public String getKod()
	{
		return kod;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public String toString()
	{
		String pomString="";
		pomString+=naziv;
		pomString+=",";
		pomString+=kod;
		pomString+=",";
		pomString+=Integer.toString(x);
		pomString+=",";
		pomString+=Integer.toString(y);
		return pomString;
	}
	public boolean getSelected()
	{
		return selected;
	}
	public void changeSelected()
	{
		selected=!selected;
	}
	public boolean getPrikazan()
	{
		return prikazan;
	}
	public void changePrikazan(boolean b)
	{
		prikazan=b;
	}
	//Proverava da li ima trenutno selektovanih aerodroma
	public static boolean isAnyoneSelected()
	{
		for(int i=0;i<Aerodrom.getNizAerodrom().size();i++)
		{
			if(Aerodrom.getNizAerodrom().get(i).getSelected())
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
