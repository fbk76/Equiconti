package com.cz.equiconti.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(
    tableName = "Horse",
    foreignKeys = @ForeignKey(
        entity = Owner.class,
        parentColumns = "id",
        childColumns = "ownerId",
        onDelete = ForeignKey.CASCADE
    )
)
public class Horse {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long ownerId; // FK verso Owner.id

    @NonNull
    public String name;

    public String breed; // razza
    public int age; // età in anni

    public Horse(long ownerId, @NonNull String name, String breed, int age) {
        this.ownerId = ownerId;
        this.name = name;
        this.breed = breed;
        this.age = age;
    }
}
