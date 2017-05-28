package com.siehuai.smartdrugbox.User.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.siehuai.smartdrugbox.Generic.data.IDbData;

@IgnoreExtraProperties
public class MedicineDetails implements IDbData, Parcelable {

    private String id;
    private String medicineName;
    private String drugstore;
    private String description;
    private String medicineMoreInfo;
    private String frequencyOfTaking;
    private String medicineImg;
    private double price;

    public MedicineDetails() {
    }

    public MedicineDetails(String id,
                           String medicineName,
                           String drugstore,
                           String description,
                           String medicineMoreInfo,
                           String frequencyOfTaking,
                           String medicineImg,
                           double price
                           ) {
        this.id = id;
        this.medicineName = medicineName;
        this.drugstore = drugstore;
        this.description = description;
        this.medicineMoreInfo = medicineMoreInfo;
        this.frequencyOfTaking = frequencyOfTaking;
        this.medicineImg = medicineImg;
        this.price = price;
    }

    public String getDrugstore() {
        return drugstore;
    }

    public void setDrugstore(String drugstore) {
        this.drugstore = drugstore;
    }

    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedicineMoreInfo() {
        return medicineMoreInfo;
    }

    public void setMedicineMoreInfo(String medicineMoreInfo) {
        this.medicineMoreInfo = medicineMoreInfo;
    }

    public String getFrequencyOfTaking() {
        return frequencyOfTaking;
    }

    public void setFrequencyOfTaking(String frequencyOfTaking) {
        this.frequencyOfTaking = frequencyOfTaking;
    }

    public String getMedicineImg() {
        return medicineImg;
    }

    public void setMedicineImg(String medicineImg) {
        this.medicineImg = medicineImg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    protected MedicineDetails(Parcel in) {
        //the sequence of this need to correspond to writeToParcel variable sequence
        id = in.readString();
        medicineName = in.readString();
        drugstore = in.readString();
        description = in.readString();
        medicineMoreInfo = in.readString();
        frequencyOfTaking = in.readString();
        medicineImg = in.readString();
        price = in.readDouble();

    }

    public static final Creator<MedicineDetails> CREATOR = new Creator<MedicineDetails>() {
        @Override
        public MedicineDetails createFromParcel(Parcel in) {
            return new MedicineDetails(in);
        }

        @Override
        public MedicineDetails[] newArray(int size) {
            return new MedicineDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(medicineName);
        dest.writeString(drugstore);
        dest.writeString(description);
        dest.writeString(medicineMoreInfo);
        dest.writeString(frequencyOfTaking);
        dest.writeString(medicineImg);
        dest.writeDouble(price);
    }

}
