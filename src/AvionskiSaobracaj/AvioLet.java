package AvionskiSaobracaj;


public class AvioLet {
	
	private Aerodrom pocetakAerodrom;
	private Aerodrom krajAerodrom;
	private int satipoletanja;
	private int minutipoletanja;
	private int trajanje;
	//Konstruktor
	public AvioLet(String kodpocString,String kodkrajString,String vremepoletanjaString,String trajanje) throws IllegalArgumentException {
		// TODO Auto-generated constructor stub
		try {
			pocetakAerodrom=Aerodrom.find(kodpocString);
			krajAerodrom=Aerodrom.find(kodkrajString);
			String[] nizs=vremepoletanjaString.split(":");
			if(nizs.length!=2)
			{
				throw new IllegalArgumentException("Vreme poletanja mora da bude u formatu sati:minuti");
			}
			satipoletanja=Integer.parseInt(nizs[0]);
			minutipoletanja=Integer.parseInt(nizs[1]);
			this.trajanje=Integer.parseInt(trajanje);
			if(satipoletanja<0 || satipoletanja>23) throw new IllegalArgumentException("Sati poletanja moraju da budu izmedju 0 i 23");
			if(minutipoletanja<0 || minutipoletanja>59) throw new IllegalArgumentException("Minuti poletanja moraju da budu izmedju 0 i 59");
		}catch (NumberFormatException e) {
			// TODO: handle exception
			throw new IllegalArgumentException("Vreme poletanja ili duzina je u losem formatu, program prima samo brojeve i znak :");
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new IllegalArgumentException(e.getMessage());
		}		
	}
	//Konstruktor za fajl
	public AvioLet(String line) throws IllegalArgumentException
	{
		try {
			String[] nizlineStrings=line.split(",");
			if(nizlineStrings.length!=4)
			{
				throw new IllegalArgumentException("Format linije fajla nije dobar promeni fajl ili probaj drugi");
			}
			pocetakAerodrom=Aerodrom.find(nizlineStrings[0]);
			krajAerodrom=Aerodrom.find(nizlineStrings[1]);
			String[] nizs=nizlineStrings[2].split(":");
			if(nizs.length!=2)
			{
				throw new IllegalArgumentException("Vreme poletanja mora da bude u formatu sati:minuti");
			}
			satipoletanja=Integer.parseInt(nizs[0]);
			minutipoletanja=Integer.parseInt(nizs[1]);
			this.trajanje=Integer.parseInt(nizlineStrings[3]);
			if(satipoletanja<0 || satipoletanja>23) throw new IllegalArgumentException("Sati poletanja moraju da budu izmedju 0 i 23");
			if(minutipoletanja<0 || minutipoletanja>59) throw new IllegalArgumentException("Minuti poletanja moraju da budu izmedju 0 i 59");
		}
		catch (NumberFormatException e) {
			// TODO: handle exception
			throw new IllegalArgumentException("Vreme poletanja ili duzina je u losem formatu, format u fajlu treba da bude sati:minuti,trajanje");
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new IllegalArgumentException(e.getMessage());
		}
		
		
	}
	public String toString()
	{
		String pomString="";
		pomString+=pocetakAerodrom.getKod();
		pomString+=",";
		pomString+=krajAerodrom.getKod();
		pomString+=",";
		pomString+=Integer.toString(satipoletanja);
		pomString+=":";
		pomString+=Integer.toString(minutipoletanja);
		pomString+=",";
		pomString+=Integer.toString(trajanje);
		return pomString;
	}

}
