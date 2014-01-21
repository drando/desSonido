package es.uma.SSL;

//import java.math.BigInteger;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
//import java.security.Key;
import java.security.SecureRandom;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class DesFlujo {

	/*
	 * ESTA VARIACION DE LA CLASE 'DES' SERVIRA PARA CIFRAR FLUJOS DESDE UN FICHERO
	 */
	
	SecretKey clave; // <------- Aqui guardamos la clave generada
	
	private int tamanoBuf=1024;
/**************
 * 	
 */
	 public DesFlujo(){
		 this.generarClave();
	 }
	 
/************
 * 	
 * @param LongClave
 *//*RSAPublicKeySpec pub;
	
	 public Des(int LongClave) {       

		 generarClave(LongClave);
	 }*/
	 
	 
/**************
 * 	 
 * @param LongClave
 */
	 public void generarClave() {   
		 
		 Log.i("generar Clave DES","Estoy Generando clave DES");
		 
	        try{
	            KeyGenerator kg = KeyGenerator.getInstance("DES");
	            kg.init(new SecureRandom());
	            clave=kg.generateKey();
	
	        }catch(Exception e){
	        	Log.d("generar Clave", e.getMessage());
	        }
	    }
	 

	 public void actualizarClave(String claveNueva) throws InvalidKeyException{
		 // Vamos a actualizar la clave local con éste string
		 
		 Log.i("DES Sonido","La clave que le pasamos a actualizar es "+claveNueva);
		 //byte[] codificado = new BigInteger(claveNueva,16).toByteArray();
		 byte[] codificado = this.toByte(claveNueva);
		 
		 clave = new SecretKeySpec(codificado,"DES");

		 Log.i("DES Sonido","Clave guardada es "+this.toHex(clave.getEncoded().toString()));
	 }
	 
/*********
 *    
 * @param data
 * @return
 */
	 /*public String Cifrar(String data) {
		 return Cifrar(data.getBytes());
	 }*/
/*******
 * 	 
 * @param data
 * @return
 * @throws InvalidKeySpecException 
 */
	 public void Cifrar(InputStream entrada,OutputStream salida) throws IOException,InvalidKeyException, InvalidKeySpecException {
		 //byte[] cifrado=null;
		try {
			this.desEncrypt(entrada,salida);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		 
		 
   	}
/***************
 * 	 
 * @param Data
 * @return
 */
	 public void Descifrar(InputStream entra,OutputStream sale) throws IOException,InvalidKeyException{
		// byte[] ret=null;
		//byte[] data= this.toByte(Data);
		 
		try {
			this.rsaDecrypt(entra,sale);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	 }
	 
/************
 * 	 
 * @param data
 * @return
 * @throws InvalidKeyException
 * @throws IllegalBlockSizeException
 * @throws BadPaddingException
 * @throws NoSuchAlgorithmException
 * @throws InvalidKeySpecException
 * @throws IOException 
 */
	    public void desEncrypt(InputStream entrada, OutputStream sale) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
	    	 
	    	  //byte[] cipherData =null;  
	    	  
			try {
				Cipher cipher;
				cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, clave);
		        
				
				sale = new CipherOutputStream(sale,cipher);
				byte[] buffer = new byte[tamanoBuf];
				int longitud;
				while ((longitud=entrada.read(buffer))>0){
					sale.write(buffer,0,longitud);
				}
				sale.close();
				//sale=saleCifrado;
				
				//cipherData = cipher.doFinal(data);
//TODO Meter aqui la variacion par el cifrado de flujo �byte data es el manejador del fich?		    	
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}
			      	
	    	}
	    
	    
/***************
 * 
 * @param data
 * @return
 * @throws InvalidKeyException
 * @throws IllegalBlockSizeException
 * @throws BadPaddingException
 * @throws NoSuchAlgorithmException
 * @throws InvalidKeySpecException
 * @throws IOException 
 */
	    public void rsaDecrypt(InputStream entrada,OutputStream salida) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
	    	  
//	     	  KeyFactory fact = KeyFactory.getInstance("RSA");
//	    	  PrivateKey privKey = fact.generatePrivate(priv);
	    	  //byte[] cipherData =null;  
	    	  
			try {
				Cipher cipher;
				cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.DECRYPT_MODE, clave);
		        
				entrada = new CipherInputStream(entrada,cipher);
				byte[] buffer = new byte[1024];
				int longitud;
				while ((longitud=entrada.read(buffer))>0){
					salida.write(buffer,0,longitud);
				}
				salida.close();
				//salida=salidaCifrada;
				
		    	
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}
			     	
	    	}
	    
/*********
 * 	    
 * @param txt
 * @return
 */
	    public String toHex(String txt) {
            return toHex(txt.getBytes());
	    }
/******
 * 	    
 * @param hex
 * @return
 */
	    public String fromHex(String hex) {
            return new String(toByte(hex));
	    }
 /***********
  *    
  * @param hexString
  * @return
  */
    public byte[] toByte(String hexString) {
            int len = hexString.length()/2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++)
                    result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
            return result;
    }
/********
 * 
 * @param buf
 * @return
 */
    	public static String toHex(byte[] buf) {
            if (buf == null)
                    return "";
            StringBuffer result = new StringBuffer(2*buf.length);
            for (int i = 0; i < buf.length; i++) {
                    appendHex(result, buf[i]);
            }
            return result.toString();
    	}
   /********
    *  	
    */
    	private final static String HEX = "0123456789ABCDEF";
 /***********
  *    	
  * @param sb
  * @param b
  */
    	private static void appendHex(StringBuffer sb, byte b) {
            sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    	}

}
