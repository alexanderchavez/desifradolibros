/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decifradolibros;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DELL
 */
public class DecifradoLibros {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //aplicar a la inversa el cifrado cesar
                //leer archivo desencoder
        LinkedTreeMap<String,LinkedTreeMap> dencoder = cargaCipher("decode_cipher.json");

        //cargar lista de libros cifrados
        Instant inicio= Instant.now();
        String directorio;
        String llave;
        if (args.length > 0) {
            directorio = args[0];
           llave="Todos esos momentos se perderan como lagrimas en la lluvia";
        }
        else {
            directorio = "descarga_libros";
            llave="Todos esos momentos se perderan como lagrimas en la lluvia";
        }
        //List<Libro> lista = cifradoLibros(directorio, encoder);
        List<File> lista_archivos= obtenListaArchivos(directorio,"cifrado");
        
        for (File archivo : lista_archivos) {
            System.out.println(archivo.toString());
            
            
        }
        List<Libro> lista_libros=cargarLibrosSerial(lista_archivos);
        
        for (Libro libro : lista_libros) {
            System.out.println(libro.original.substring(1763,1800));
         }
        //descifrar
        desifrar(lista_libros,llave,dencoder);
        //guardar archivos descifrado
        
        guardarLibros(lista_libros,"descifrado");
        Instant fin = Instant.now();
        long tiempoComputo = Duration.between(inicio,fin).toMillis();
        System.out.println("Tiempo de computo: "+tiempoComputo+" milisegundos");
    }
    public static void guardarLibros(List<Libro> lista_libros, String extension){
    
        for (Libro libro : lista_libros) {
            libro.guardaArchivo(extension);
        }
    
    }
    public static void desifrar(List<Libro> lista_libros,String llave , LinkedTreeMap<String,LinkedTreeMap> dencoder){
        
        
       
        for (Libro libro : lista_libros) {
            
            System.out.println("Procesando  libro "+libro.titulo);
            libro.llave=llave;
            libro.diccionario_cifrado=dencoder;
            libro.cifrar();
           
        }
      
    }
    public static List<Libro> cargarLibrosSerial(List<File> lista_archivos){
    
    List<Libro> lista_libros =new ArrayList();
       
        try {
           
            for (File archivo : lista_archivos) {
                Libro libro = new Libro(archivo.toString());
                libro.cargaLibro();
                lista_libros.add(libro);
               // System.out.println(libro.original.substring(1763,1800));
               
            }
            
        } catch (Exception e) {
            System.out.println("cargandoArchivosSerial"+e.getMessage());
        }
    return lista_libros;
    }
    public static List<File> obtenListaArchivos(String ruta, String ext) {
      List<File> lista_archivos = new ArrayList<>();
      File directorio = new File(ruta);
      File[] lista =directorio.listFiles();
      
        for (File archivo: lista) {
            String nombre_archivo=archivo.toString();
            int indice =nombre_archivo.lastIndexOf(".");
            if (indice> 0) {
                String extencion= nombre_archivo.substring(indice+1);
                if (extencion.equals(ext)) {
                    lista_archivos.add(archivo);
                }
            }
                    
        }
      
      return lista_archivos;
    }
    
    
    public static LinkedTreeMap cargaCipher(String archivo) {
        LinkedTreeMap map=null;
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String linea;
            String texto="";
            while ( (linea= reader.readLine()) !=null ){
                texto += linea;
            }
            map = gson.fromJson(texto, LinkedTreeMap.class);
        } catch (IOException e) {
            System.out.println("cargaCipher "+e.getMessage());
        }
        return map;
    } 
}
