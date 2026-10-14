package AvionskiSaobracaj;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.Year;
import java.util.Timer;
import java.util.TimerTask;




public class Mapa extends Canvas {
	private static final int velicina_kvadrata=20;
	public Mapa() {
		// TODO Auto-generated constructor stub
		setSize(600,600);
		this.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e)
		{
			//TODO
			Main.GetInstance().getTimer().cancel();
			Main.GetInstance().time();
			for(int i=0;i<Aerodrom.getNizAerodrom().size();i++)
			{
				int x=dekartx(Aerodrom.getNizAerodrom().get(i).getX());
				int y=dekarty(Aerodrom.getNizAerodrom().get(i).getY());
			
				if((x<=e.getX())&&(e.getX()<=x+velicina_kvadrata)&&(y<=e.getY())&&(e.getY()<=y+velicina_kvadrata)) {
					Aerodrom.getNizAerodrom().get(i).changeSelected();
			    }
			}

		}
		});
	}
	//Pretvara iz dekartovog sistema u kompljuterski x osu
	public int dekartx(int x)
	{
		x+=90;
		int x2=x*(getWidth()-velicina_kvadrata)/180;
		return x2;
	}
	//Pretvara y osu
	public int dekarty(int y)
	{
		y=-y;
		y+=90;
		int y2=y*(getHeight()-velicina_kvadrata-10)/180;
		return y2;
	}
	//crta aerodrome na mapi
	public void paint(Graphics g)
	{
		g.setColor(Color.gray);
		for(int i=0;i<Aerodrom.getNizAerodrom().size();i++)
		{
			if(Aerodrom.getNizAerodrom().get(i).getPrikazan())
			{
				
				if(Aerodrom.getNizAerodrom().get(i).getSelected()&&Main.GetInstance().getTreperi())
				{
					g.setColor(Color.red);
				
				}
				else {
					g.setColor(Color.gray);
				
				}
				g.fillRect(dekartx(Aerodrom.getNizAerodrom().get(i).getX()), dekarty(Aerodrom.getNizAerodrom().get(i).getY()), velicina_kvadrata, velicina_kvadrata);
				g.drawString(Aerodrom.getNizAerodrom().get(i).getKod(),dekartx(Aerodrom.getNizAerodrom().get(i).getX()) , dekarty(Aerodrom.getNizAerodrom().get(i).getY())+10+velicina_kvadrata);
			}
		}
	}
}
