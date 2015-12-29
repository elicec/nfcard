package com.sinpo.xnfc.nfc.reader;

import java.io.IOException;

import android.nfc.tech.MifareClassic;
import android.util.Log;

import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.bean.Card;

public final class MiReader {
	
	static void readCard(MifareClassic tag, Card card) throws IOException {


		tag.connect();
		card.addApplication(readApplication(tag));
		tag.close();
	}
	
	private static Application readApplication(MifareClassic tag)
			throws IOException {
		boolean cardVerify = false;
		final Application app;
			app = new Application();
			app.setProperty(SPEC.PROP.ID, SPEC.APP.OCTOPUS);
			app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
		

		app.setProperty(SPEC.PROP.SERIAL, tag.getType());



		cardVerify = tag.authenticateSectorWithKeyA(3, MifareClassic.KEY_DEFAULT);
		int bIndex=0;
		if (cardVerify)
		{
			bIndex = tag.sectorToBlock(3) + 2;
			byte[] data = tag.readBlock(bIndex);
			float mon = getMoney(data);
			app.setProperty(SPEC.PROP.BALANCE, mon);
		}
		else
		{
				Log.i("elicec","授权失败");
		}

		return app;
	}
	private static float getMoney(byte[] data){
		float money=0;
		String temp=byte2HexString(data);
		temp=temp.substring(0, 8);
		int n=Integer.parseInt(temp);
		money=(float) (n/100.0);
		return money;
	}

	public static String byte2HexString(byte[] bytes)
	{
		String data = "";
		if (bytes != null)
		{
			for (Byte b : bytes)
			{
				data += String.format("%02X", b.intValue() & 0xFF);
			}
		}
		return data;
 	}
}
