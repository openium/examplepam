package fr.openium.examplepam.model;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Call {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo
    public String contactName;

    @ColumnInfo
    public int length;

    @ColumnInfo
    public Date startDate;

    public Call() {

    }

    public Call(String contactName, long startDate) {
        this.contactName = contactName;
        this.startDate = new Date(startDate);
    }
}
