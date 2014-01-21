package es.uma.SSL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DesSonidoActivity extends Activity {
    /** Called when the activity is first created. */
    
	private String mFileName = null;
    private String rutaBase = null;
	private String nombreFichero=null;
	private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;

    private static EditText cajaFichero=null;
       
    public DesFlujo cifrado;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
		Toast.makeText(this.getApplicationContext(), "INFORMACION DE LA PRACTICA EN MENU", Toast.LENGTH_LONG).show();

    // EMPEZAMOS GRABAR DESDE EL MICROFONO
        
        /**
         * Preparamos la caja con el nombre del fichero y un nombre por defecto primero.
         */	
		rutaBase=Environment.getExternalStorageDirectory().getAbsolutePath();
        nombreFichero="cifra1.mp4";
        cajaFichero=(EditText) findViewById(R.id.ficheroAudio);
        cajaFichero.setText(""); //limpiamos por si acaso
        
        mFileName=rutaBase+"/"+nombreFichero;
        File fichero = new File(Environment.getExternalStorageDirectory(), "cifra1.mp4");
  
        int contaFichero = 1;
        while (fichero.exists()){
        	// Mientras exista el fichero vamos incrementando el valor
        	contaFichero++;
        	nombreFichero="cifra"+contaFichero+".mp4";
            fichero = new File(Environment.getExternalStorageDirectory(), nombreFichero);
        }
        mFileName=rutaBase+"/"+nombreFichero;

        // Ya tenemos en mFileName el fichero que está libre, colocamos este nombre en el
        // cuadro de texto. Si le parece bien al usuario, se queda, si no, el usuario puede
        // cambiar el nombre
              
        EditText cajaAudio = (EditText) findViewById(R.id.ficheroAudio);
        cajaAudio.setText(nombreFichero);
        
        //Log.i("SSL Cert","Nombre Fichero al salir del split vale"+nombreFichero);
        
        cifrado=new DesFlujo(); //Creamos el objeto para codificar en DES
        
        EditText cajaClave = (EditText) findViewById(R.id.clave);
        cajaClave.setText("");
        cajaClave.setText(cifrado.toHex(cifrado.clave.getEncoded()).toString());
        
    }
    
        public void PonteGrabar(View v){
        	
        	cajaFichero = (EditText) findViewById(R.id.ficheroAudio);
            mFileName=Environment.getExternalStorageDirectory().getAbsolutePath();
        	mFileName+="/"+cajaFichero.getText().toString();

        	mRecorder = new MediaRecorder();
        	mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //entrada de sonido
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);          
            mRecorder.setOutputFile(mFileName); //luego metemos el path al fichero que grabaremos
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            
            try{
            	mRecorder.prepare();
            } catch (IOException e){
            	Log.e("DES Audio","Fallo prepare()");
            }
            
            try{
            	mRecorder.start();
            } catch (IllegalStateException e){
            	Log.e("DES Audio","Fallo en start()");
            }
           
            /**
             * ALERTBOX Para parar de grabar
             */

            // Caja Alerta para mostrar boton parar
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setMessage("Grabando!");
            alertbox.setNeutralButton("Parar", new DialogInterface.OnClickListener() {
 
                // listener para el boton del Alert
                public void onClick(DialogInterface arg0, int arg1) {
                	mRecorder.stop();
                	mRecorder.release();
                	mRecorder=null;
                }
            });

              alertbox.show();
  
        }
    
        public boolean onCreateOptionsMenu(Menu menu){
        	
        	menu.add(1,menu.FIRST,menu.FIRST,"Informacion de la practica");
        	return super.onCreateOptionsMenu(menu);
        	
        }
        
        public boolean onOptionsItemSelected(MenuItem menu){
        	switch (menu.getItemId()){
        	
        	case 1:
        		Intent ayuda = new Intent(this.getApplicationContext(),Ayuda.class);
        		startActivity(ayuda);
        		return true;
        	default:
        		break;
        	}
        	
        	return super.onOptionsItemSelected(menu);
        	
        }

       public void Play(View v){
    	   cajaFichero = (EditText) findViewById(R.id.ficheroAudio);
           mFileName=Environment.getExternalStorageDirectory().getAbsolutePath();
       		mFileName+="/"+cajaFichero.getText().toString();
       			try{
       				if (mPlayer != null){
       					if (mPlayer.isPlaying()){
       						mPlayer.stop();
       						mPlayer.release();
       						mPlayer=null;
       						Toast.makeText(this.getApplicationContext(), "Parado...", Toast.LENGTH_SHORT).show();
       				}       					}
       				else{
       					mPlayer = new MediaPlayer();
       					mPlayer.setDataSource(mFileName);
       					mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

							public void onCompletion(MediaPlayer mp) {
								mPlayer.release();
								mPlayer=null;
							}
       					}
       					);
       					mPlayer.prepare();
       					mPlayer.start();
       					Toast.makeText(this.getApplicationContext(), "Reproduciendo...", Toast.LENGTH_SHORT).show();
       				}
       			}catch(IOException e){
       				Toast.makeText(this.getApplicationContext(), "Fichero no existe para Reproducir...", Toast.LENGTH_SHORT).show();
       			} 
       	}

       public void PararPlay(View v){
    	   mPlayer.release();
    	   mPlayer=null;
       }
        
       public void CifrarFichero(View v){
    	   try {
    		nombreFichero=cajaFichero.getText().toString();
    		mFileName=rutaBase+"/"+nombreFichero;
    		FileInputStream fichero = new FileInputStream(mFileName);
			
			String[] soloNombre = nombreFichero.split(".mp4");
			
			FileOutputStream salida = new FileOutputStream(rutaBase+"/"+soloNombre[0]+"_cifrado.mp4");
			
			cifrado.Cifrar(fichero, salida);
			Toast.makeText(this.getApplicationContext(), "Fichero "+soloNombre[0]+"_cifrado.mp4"+" generado", Toast.LENGTH_SHORT).show();
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this.getApplicationContext(), "Fichero no encontrado para cifrar", Toast.LENGTH_SHORT).show();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this.getApplicationContext(), "Clave inválida para cifrar", Toast.LENGTH_SHORT).show();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this.getApplicationContext(), "Error en lectura de fichero, ¿Existe este fichero para cifrar?", Toast.LENGTH_SHORT).show();
		}
       }
       
       public void DescifraFichero(View v){
    	   //Desciframos el fichero
    	   try{
    		   EditText descifrar = (EditText) findViewById(R.id.ficheroAudio);
    		   nombreFichero=descifrar.getText().toString();
    		   
    		   String[] soloNombreTemp = nombreFichero.split(".mp4");
    		   String[] soloNombre = soloNombreTemp[0].split("_cifrado");
    		   FileInputStream fichero = new FileInputStream(rutaBase+"/"+nombreFichero);
    		   
   				FileOutputStream salida = new FileOutputStream(rutaBase+"/"+soloNombre[0]+"_descifrado.mp4");
   				Log.i("DES Sonido","Nombre del fichero a descifrar, solo el nombre es "+soloNombre[0]);
   				EditText claveDesdeActivity = (EditText) findViewById(R.id.clave);
   				cifrado.actualizarClave(claveDesdeActivity.getText().toString());

   				cifrado.Descifrar(fichero, salida);
   				
   				//Todo bien in excepciones, ponemos un Toast en pantalla
   				Toast.makeText(this.getApplicationContext(), "Fichero "+soloNombre[0]+"_descifrado.mp4"+" generado", Toast.LENGTH_LONG).show();
   				
 
    	   }catch(FileNotFoundException e){
    		   Toast.makeText(this.getApplicationContext(), "Fichero no encontrado para descifrar", Toast.LENGTH_LONG).show();
    	   } catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
    		   Toast.makeText(this.getApplicationContext(), "Clave inválida para descifrar", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this.getApplicationContext(), "Esperaba un fichero de la forma nombre_cifrado.mp4", Toast.LENGTH_LONG).show();
		}
       }
 }