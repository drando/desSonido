package es.uma.SSL;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Ayuda extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layayuda);
	    // TODO Auto-generated method stub
	    
	    TextView instrucc = (TextView) findViewById(R.id.instrucciones);
	    
	    instrucc.setMovementMethod(new ScrollingMovementMethod());
	    
	    instrucc.setText("INFORMACION DE LA PRACTICA\n\n" +
	    		"El objetivo es poder grabar un fichero de sonido .mp4, poder reproducirlo desde la " +
	    		"aplicación y poder cifrarlo y descifrarlo. El programa generará nombres del tipo " +
	    		"cifra[numero].mp4 dependiendo si el nombre ya está en uso, y guardará todos los cambios" +
	    		" en la tarjeta SD. Podemos usar cualquier otro nombre que queramos. Podremos reproducir/parar el fichero desde el boton play/stop y podremos" +
	    		" grabarlo desde el boton grabar. \n\n" +
	    		"Una vez cifrado un fichero, se generará el mismo con formato nombre_cifrado.mp4. Para descifrarlo " +
	    		"es necesario un fichero con este formato de nombre. Tras el descifrado generará otro llamado nombre_descifrado.mp4\n\n" +
	    		"Podemos copiar la clave para descifrar un fichero en cualquier momento.");
	}

}
