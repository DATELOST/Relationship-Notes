package com.jnu.relationshipnotes.dataprocesser;

import android.content.Context;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataBank {
    private ArrayList<Gift> gifts=new ArrayList<>();
    private ArrayList<Gift> giftsout=new ArrayList<>();
    private Context context;
    private final String GIFT_FILE_NAME="gifts.txt";
    private final String GIFT_OUT_FILE_NAME="gifts_out.txt";
    public DataBank(Context context) { this.context = context; }
    public ArrayList<Gift> getGifts() { return gifts; }
    public ArrayList<Gift> getGiftsOut() { return giftsout; }
    public int getGiftSum(){
        int sum=0;
        for(int i=0;i<gifts.size();++i)sum+=gifts.get(i).getMoney();
        return sum;
    }
    public int getGiftOutSum(){
        int sum=0;
        for(int i=0;i<giftsout.size();++i)sum+=giftsout.get(i).getMoney();
        return sum;
    }
    public void Save(int op) {
        ObjectOutputStream oos = null;
        try {
            if(op==0){
                oos=new ObjectOutputStream(context.openFileOutput(GIFT_FILE_NAME,Context.MODE_PRIVATE));
                oos.writeObject(gifts);
            }
            else {
                oos=new ObjectOutputStream(context.openFileOutput(GIFT_OUT_FILE_NAME,Context.MODE_PRIVATE));
                oos.writeObject(giftsout);
            }
            oos.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
    public void Load(int op) {
        ObjectInputStream ois = null;
        try {
            if(op==0){
                gifts = new ArrayList<>();
                ois=new ObjectInputStream(context.openFileInput(GIFT_FILE_NAME));
                gifts = (ArrayList<Gift>) ois.readObject();
            }
            else {
                giftsout = new ArrayList<>();
                ois=new ObjectInputStream(context.openFileInput(GIFT_OUT_FILE_NAME));
                giftsout = (ArrayList<Gift>) ois.readObject();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}