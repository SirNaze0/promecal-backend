package com.sanmarcos.promecal.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class DriveService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoodleCredentials();

    private static String getPathToGoodleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "cred2.json");
        return filePath.toString();
    }
    public String uploadPdfToDrive(File file, String opcion) {
        try{
            String folderId="";
            if(opcion.equals("remision")){
                folderId = "1p_KqybjG3WgddLfK6U0Mw-b4PGuET5DG";
            }else if(opcion.equals("observaciones")){
                folderId = "1iFipXi4mW2tbVvovY2Xf4SKTuj5ubQov";
            }else if(opcion.equals("informe")){
                folderId="1YrxIkWLnoRbvX5CkJ_AyCke1jScYnO5O";
            }else if(opcion.equals("proforma")){
                folderId="1_snyf6g5xgq_pdhETJi3eyRJaTVk0EOp";
            }

            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("application/pdf", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            String imageUrl = "https://drive.google.com/uc?export=view&id="+uploadedFile.getId();
            System.out.println("PDF URL: " + imageUrl);
            file.delete();
            return imageUrl;
        }catch (Exception e){
            // Registrar el error en el log
            System.err.println("Error al subir el archivo a Google Drive: " + e.getMessage());
            // O retornar null si prefieres manejar la respuesta directamente
            // return null;
            return null;
        }
    }
    public void eliminarArchivoEnDrive(String fileId) {
        try {
            Drive drive = createDriveService();
            drive.files().delete(fileId).execute();  // Elimina el archivo usando el ID
            System.out.println("Archivo con ID " + fileId + " eliminado de Google Drive.");
        } catch (IOException e) {
            System.err.println("Error al eliminar el archivo en Google Drive: " + e.getMessage());
            // Opcionalmente, puedes manejar el error de otra forma o lanzar una excepción
            throw new RuntimeException("Error al eliminar archivo en Drive: " + e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }


    private Drive createDriveService() throws GeneralSecurityException, IOException {

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .build();

    }

}
