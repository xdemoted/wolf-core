package com.wolfco.velocity.types;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class userData extends File {

    public userData(File parent, String child) {
        super(parent, child);
        //TODO Auto-generated constructor stub
    }
    public File getUser(UUID uuid) {
        File userFile = new File(this, uuid.toString()+".yml");
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return userFile;
    }
    public void removeUser(UUID uuid) {
        File userFile = new File(this, uuid.toString()+".yml");
        if (!userFile.exists()) {
            userFile.delete();
        }
    }
}
