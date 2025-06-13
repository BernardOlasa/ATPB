package org.example.data;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.domain.Aluguel;
import org.example.domain.Bicicleta;
import org.example.domain.Cliente;
import org.example.domain.Usuario;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GerenciadorDeDados {

    public static List<Bicicleta> bicicletas = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();
    public static List<Aluguel> alugueis = new ArrayList<>();
    public static List<Usuario> usuarios = new ArrayList<>();

    static {
        pegarDados("Clientes", clientes, new TypeToken<List<Cliente>>() {}.getType());
        pegarDados("Bicicletas", bicicletas, new TypeToken<List<Bicicleta>>() {}.getType());
        pegarDados("Alugueis", alugueis, new TypeToken<List<Aluguel>>() {}.getType());
        pegarDados("Usuarios", usuarios, new TypeToken<List<Usuario>>() {}.getType());
    }

    public static <T> void pegarDados(String nomeArquivo, List<T> lista, Type type) {
        String filePath = ".\\src\\Txts\\" + nomeArquivo;
        Gson gson = new Gson();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                   T item = gson.fromJson(line, type);
                    if (item != null) {
                        lista.addAll((Collection<? extends T>) item);
                    } else {
                        System.err.println("Error parsing JSON line: " + line);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing JSON line: " + line + ", Exception: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public static void salvarDados(String nomeArquivo, List<?> lista){
        if(lista == null){
            return;
        }
        String filePath = ".\\src\\Txts\\" + nomeArquivo;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            Gson gson = new Gson();
            gson.toJson(lista, writer);

        }catch(Exception e){
        }
    }

    public String gerarGson(List<Object> lista){

        Gson gson = new Gson();
        return gson.toJson(lista);
    }
    public static boolean checarClienteExistente(Cliente cliente){

        for (Cliente c : clientes) {
            if(c.getCpf().equals(cliente.getCpf())){
                return true;
            }
        }
        return false;
    }
}
